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

    @PostMapping
    public ResponseEntity<NoteDTO> create(@Valid @RequestBody NoteDTO dto) {
        NoteDTO created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{nodeId}")
    public ResponseEntity<NoteDTO> update(@PathVariable Long nodeId, @Valid @RequestBody NoteDTO dto) {
        return ResponseEntity.ok(service.update(nodeId, dto));
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> all() {
        log.info("Obteniendo todas las notas, traceability {}", traceabilityHelper.getTraceability());
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{nodeId}")
    public ResponseEntity<NoteDTO> getById(@PathVariable Long nodeId) {
        return ResponseEntity.ok(service.findById(nodeId));
    }

    @DeleteMapping("/{nodeId}")
    public ResponseEntity<Void> delete(@PathVariable Long nodeId) {
        service.delete(nodeId);
        return ResponseEntity.noContent().build();
    }
}
