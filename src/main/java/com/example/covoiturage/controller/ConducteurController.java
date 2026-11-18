package com.example.covoiturage.controller;

import com.example.covoiturage.dto.request.ConducteurRequest;
import com.example.covoiturage.dto.response.ConducteurResponse;
import com.example.covoiturage.service.ConducteurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conducteurs")
@RequiredArgsConstructor
public class ConducteurController {
    private final ConducteurService conducteurService;

    @GetMapping
    public ResponseEntity<Page<ConducteurResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy
    ){
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortBy));
        return ResponseEntity.ok(conducteurService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConducteurResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(conducteurService.findById(id));
    }

    @GetMapping("/serach")
    public ResponseEntity<Page<ConducteurResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy
    ){
        Pageable pageable=PageRequest.of(page,size,Sort.by(sortBy));
        return ResponseEntity.ok(conducteurService.search(q,pageable));
    }

    //Modification
    @PutMapping("/{id}")
    public ResponseEntity<ConducteurResponse> update(@PathVariable Long id,@Valid @RequestBody ConducteurRequest request){
        return ResponseEntity.ok(conducteurService.update(id, request));
    }

    //suppression
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        conducteurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
