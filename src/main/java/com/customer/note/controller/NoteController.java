package com.customer.note.controller;

import com.customer.note.dto.NoteDTO;
import com.customer.note.helper.TraceabilityHelper;
import com.customer.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Validated
@Slf4j
public class NoteController {

    private final NoteService service;
    private final TraceabilityHelper traceabilityHelper;

    private String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    @PostMapping
    public ResponseEntity<NoteDTO> create(@Valid @RequestBody NoteDTO noteDTO) {
        String methodName = getMethodName();
        log.info("Service {},Component {}, Request body={}, Traceability={}", "NoteController", methodName, noteDTO, traceabilityHelper.getTraceability());
        NoteDTO created = service.create(noteDTO);
        log.info("Nota creada exitosamente Service {},Component {}, Response ={}, Traceability={}", "NoteController", methodName, noteDTO, traceabilityHelper.getTraceability());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDTO> update(@PathVariable Long noteId, @Valid @RequestBody NoteDTO noteDTO) {
        String methodName = getMethodName();
        log.info("Service {},Component {}, Request ={}, Traceability={}", "NoteController", methodName, noteDTO, traceabilityHelper.getTraceability());
        NoteDTO updated = service.update(noteId, noteDTO);
        log.info("Nota updated exitosamente Service {},Component {}, Response ={}, Traceability={}", "NoteController", methodName, noteDTO, traceabilityHelper.getTraceability());
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> all() {
        String methodName = getMethodName();
        log.info("Obteniendo todas las notas, Method={}, traceability {}", methodName, traceabilityHelper.getTraceability());
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteDTO> getById(@PathVariable Long noteId) {
        String methodName = getMethodName();
        log.info("Service {},Component {}, Request ={}, Traceability={}", "NoteController", methodName, noteId, traceabilityHelper.getTraceability());
        return ResponseEntity.ok(service.findById(noteId));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> delete(@PathVariable Long noteId) {
        String methodName = getMethodName();
        log.info("Service {},Component {}, Request={}, Traceability={}", "NoteController", methodName, noteId, traceabilityHelper.getTraceability());
        service.delete(noteId);
        return ResponseEntity.noContent().build();
    }
}
