package com.customer.note.service.impl;

import com.customer.note.dto.NoteDTO;
import com.customer.note.dto.NoteDetailDTO;
import com.customer.note.helper.TraceabilityHelper;
import com.customer.note.model.Note;
import com.customer.note.model.NoteDetail;
import com.customer.note.repository.NoteRepository;
import com.customer.note.service.NoteService;
import com.customer.note.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository repository;
    private final TraceabilityHelper traceabilityHelper;

    private NoteDTO toDTO(Note n) {
        List<NoteDetailDTO> details = n.getDetails() == null ? List.of() : n.getDetails().stream()
                .map(d -> NoteDetailDTO.builder().key(d.getKey()).value(d.getValue()).sensitive(d.getSensitive()).build())
                .collect(Collectors.toList());

        return NoteDTO.builder()
                .noteId(n.getNoteId())
                .title(n.getTitle())
                .details(details)
                .createdAt(n.getCreatedAt())
                .build();
    }

    private Note toEntity(NoteDTO dto) {
        List<NoteDetail> details = dto.getDetails() == null ? List.of() : dto.getDetails().stream()
                .map(d -> NoteDetail.builder().key(d.getKey()).value(d.getValue()).sensitive(d.getSensitive()).build())
                .collect(Collectors.toList());

        return Note.builder()
                .noteId(dto.getNoteId())
                .title(dto.getTitle())
                .details(details)
                .createdAt(dto.getCreatedAt())
                .build();
    }

    @Override
    public NoteDTO create(NoteDTO dto) {
        // Inyectamos el holder. Spring sabe que debe darte los datos de la petición actual.
       log.info("informacion request {}, traceability {}",dto, traceabilityHelper.getTraceability());

        Note e = toEntity(dto);
        if (e.getCreatedAt() == null) e.setCreatedAt(LocalDateTime.now());
        e = repository.save(e);
        return toDTO(e);
    }

    @Override
    public NoteDTO update(Long nodeId, NoteDTO dto) {
        Note existing = repository.findById(nodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with nodeId: " + nodeId));

        existing.setTitle(dto.getTitle());
        existing.setNoteId(dto.getNoteId());
        existing.setDetails(dto.getDetails() == null ? List.of() : dto.getDetails().stream()
                .map(d -> NoteDetail.builder().key(d.getKey()).value(d.getValue()).sensitive(d.getSensitive()).build())
                .collect(Collectors.toList()));
        // keep createdAt
        existing = repository.save(existing);
        return toDTO(existing);
    }

    @Override
    public NoteDTO findById(Long nodeId) {
        return repository.findById(nodeId).map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with nodeId: " + nodeId));
    }

    @Override
    public List<NoteDTO> findAll() {
        List<NoteDTO> response=repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
        log.info("informacion de todas  las  notas",response.toString());
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void delete(Long nodeId) {
        if (!repository.existsById(nodeId)) throw new ResourceNotFoundException("Note not found with nodeId: " + nodeId);
        repository.deleteById(nodeId);
    }
}
