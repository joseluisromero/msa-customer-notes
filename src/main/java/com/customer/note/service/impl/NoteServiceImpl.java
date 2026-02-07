package com.customer.note.service.impl;

import com.customer.note.dto.NoteDTO;
import com.customer.note.dto.NoteDetailDTO;
import com.customer.note.dto.error.DetailError;
import com.customer.note.dto.error.ProblemDetail;
import com.customer.note.exception.CustomServiceException;
import com.customer.note.helper.SecretKeyHelper;
import com.customer.note.helper.TraceabilityHelper;
import com.customer.note.model.Note;
import com.customer.note.model.NoteDetail;
import com.customer.note.model.enums.CodeValidationEnum;
import com.customer.note.repository.NoteRepository;
import com.customer.note.service.CryptoService;
import com.customer.note.service.NoteService;
import com.customer.note.util.ConverterErrorUtil;
import com.customer.note.util.NoteDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository repository;
private final TraceabilityHelper traceabilityHelper;
    private final CryptoService cryptoService;
    private final SecretKeyHelper secretKeyHelper;

    private NoteDTO toDTO(Note n) {
        List<NoteDetailDTO> details = n.getDetails() == null ? List.of() : n.getDetails().stream().map(d -> NoteDetailDTO.builder().key(d.getKey()).value(d.getValue()).sensitive(d.getSensitive()).build()).collect(Collectors.toList());

        return NoteDTO.builder().noteId(n.getNoteId()).title(n.getTitle()).details(details).createdAt(n.getCreatedAt()).build();
    }

    private Note toEntity(NoteDTO dto) {
        List<NoteDetail> details = dto.getDetails() == null ? List.of() : dto.getDetails().stream().map(d -> NoteDetail.builder().key(d.getKey()).value(d.getValue()).sensitive(d.getSensitive()).build()).collect(Collectors.toList());

        return Note.builder().noteId(dto.getNoteId()).title(dto.getTitle()).details(details).createdAt(dto.getCreatedAt()).build();
    }

    @Override
    public NoteDTO create(NoteDTO dto) {
        // Inyectamos el holder. Spring sabe que debe darte los datos de la petición actual.
        Note e = toEntity(dto);
        e.getDetails().stream().forEach(d -> {
            if (d.getSensitive() != null && d.getSensitive()) {
                String encryptedValue = cryptoService.encrypt(d.getValue(), secretKeyHelper.vaultKey());
                d.setValue(encryptedValue);
                log.info("El valor de la nota es sensible value {}, encriptada es {}", d.getValue(), encryptedValue);
            }
        });
        if (e.getCreatedAt() == null) e.setCreatedAt(LocalDateTime.now());
        e = repository.save(e);
        return toDTO(e);
    }

    @Override
    public NoteDTO update(Long noteId, NoteDTO dto) {
        if (dto.getNoteId() == null || !dto.getNoteId().equals(noteId)) {
            DetailError error = ConverterErrorUtil.convertToDetailError(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_EQUAL.getCode(),
                    CodeValidationEnum.VALIDATION_NOTE_ID_NOT_EQUAL.getMessage(),
                    String.format(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_EQUAL.getBusinessMessage(), dto.getNoteId(), noteId));
            ProblemDetail problemDetail = ConverterErrorUtil.getProblemDetail(String.format(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_EQUAL.getBusinessMessage(), dto.getNoteId(), noteId), NoteDataUtil.getCurrentMethodName(), List.of(error));
            log.error("Nota no encontrada o con id diferentes , problemDetail={}, traceability={}", problemDetail, traceabilityHelper.getTraceability());
            throw new CustomServiceException(problemDetail, CodeValidationEnum.VALIDATION_NOTE_ID_NOT_EQUAL.getHttpStatus());
        }
        Optional<Note> existing = repository.findById(noteId);
        if (!existing.isPresent()) {
            DetailError error = ConverterErrorUtil.convertToDetailError(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getCode(),
                    CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getMessage(),
                    String.format(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getBusinessMessage(), noteId));
            ProblemDetail problemDetail = ConverterErrorUtil.getProblemDetail(String.format(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getBusinessMessage(), noteId), NoteDataUtil.getCurrentMethodName(), List.of(error));
            log.error("Nota no encontrada, problemDetail={}, traceability={}", problemDetail, traceabilityHelper.getTraceability());
            throw new CustomServiceException(problemDetail, CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getHttpStatus());
        }
        Note existingNote = existing.get();
        existingNote.setTitle(dto.getTitle());
        existingNote.setNoteId(dto.getNoteId());
        existingNote.setDetails(dto.getDetails() == null ? List.of() : dto.getDetails().stream().map(d -> NoteDetail.builder().key(d.getKey()).value(d.getValue()).sensitive(d.getSensitive()).build()).collect(Collectors.toList()));
        // keep createdAt
        existingNote = repository.save(existingNote);
        log.info("Nota con id {} actualizada exitosamente, traceability {}", noteId, traceabilityHelper.getTraceability());
        return toDTO(existingNote);
    }

    @Override
    public NoteDTO findById(Long noteId) {
        log.info("Buscando nota con id {}, traceability {}", noteId, traceabilityHelper.getTraceability());
        Optional<Note> note = repository.findById(noteId);
        if (!note.isPresent()) {
            DetailError error = ConverterErrorUtil.convertToDetailError(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getCode(),
                    CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getMessage(),
                    String.format(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getBusinessMessage(), noteId));
            ProblemDetail problemDetail = ConverterErrorUtil.getProblemDetail(String.format(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getBusinessMessage(), noteId), NoteDataUtil.getCurrentMethodName(), List.of(error));
            log.error("Nota no encontrada, problemDetail={}, traceability={}", problemDetail, traceabilityHelper.getTraceability());
            throw new CustomServiceException(problemDetail, CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getHttpStatus());
        }
        return note.map(this::toDTO).get();
    }

    @Override
    public List<NoteDTO> findByTitle(String title) {
        List<NoteDTO> response = repository.findByTitleContainingIgnoreCase(title).stream().map(this::toDTO).collect(Collectors.toList());
        response.stream().forEach(n -> {
            n.getDetails().stream().forEach(d -> {
                if (d.getSensitive() != null && d.getSensitive()) {
                    String decryptedValue = cryptoService.decrypt(d.getValue(), secretKeyHelper.vaultKey());
                    d.setValue(decryptedValue);
                    log.info("El valor de la nota es sensible value {}, desencripytada es {}", d.getValue(), decryptedValue);
                }
            });
        });
        log.info("informacion de todas  las  notas", response.toString());
        return response ;
    }

    @Override
    public List<NoteDTO> findAll() {
        List<NoteDTO> response = repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
        response.stream().forEach(n -> {
            n.getDetails().stream().forEach(d -> {
                if (d.getSensitive() != null && d.getSensitive()) {
                    String decryptedValue = cryptoService.decrypt(d.getValue(), secretKeyHelper.vaultKey());
                    d.setValue(decryptedValue);
                    log.info("El valor de la nota es sensible value {}, desencripytada es {}", d.getValue(), decryptedValue);
                }
            });
        });
        log.info("informacion de todas  las  notas", response.toString());
        return response ;
    }

    @Override
    public void delete(Long noteId) {
        if (!repository.existsById(noteId)) {
            DetailError error = ConverterErrorUtil.convertToDetailError(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getCode(),
                    CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getMessage(),
                    String.format(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getBusinessMessage(), noteId));
            ProblemDetail problemDetail = ConverterErrorUtil.getProblemDetail(String.format(CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getBusinessMessage(), noteId), NoteDataUtil.getCurrentMethodName(), List.of(error));
            log.error("Nota no encontrada, problemDetail={}, traceability={}", problemDetail, traceabilityHelper.getTraceability());
            throw new CustomServiceException(problemDetail, CodeValidationEnum.VALIDATION_NOTE_ID_NOT_FOUND.getHttpStatus());
        }
        repository.deleteById(noteId);
        log.info("Nota con id {} eliminada exitosamente, traceability {}", noteId, traceabilityHelper.getTraceability());
    }
}
