package com.customer.note.service;

import com.customer.note.dto.NoteDTO;
import java.util.List;

public interface NoteService {
    NoteDTO create(NoteDTO dto);
    NoteDTO update(Long nodeId, NoteDTO dto);
    NoteDTO findById(Long nodeId);
    List<NoteDTO> findAll();
    void delete(Long nodeId);
}   
