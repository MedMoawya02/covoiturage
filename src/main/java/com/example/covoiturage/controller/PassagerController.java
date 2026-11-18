package com.example.covoiturage.controller;

import com.example.covoiturage.dto.request.PassagerRequest;
import com.example.covoiturage.dto.response.PassagerResponse;
import com.example.covoiturage.dto.response.ReservationResponse;
import com.example.covoiturage.service.PassagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passagers")
@RequiredArgsConstructor
public class PassagerController {
    private final PassagerService passagerService;

    //All passagers
    @GetMapping
    public ResponseEntity<Page<PassagerResponse>> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "nom") String sortedBy
    ){
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortedBy));
        return ResponseEntity.ok(passagerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassagerResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(passagerService.findById(id));
    }
    //serach
    @GetMapping("/search")
    public ResponseEntity<Page<PassagerResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0")Integer page,
            @RequestParam(defaultValue = "10")Integer size
    ){
        Pageable pageable=PageRequest.of(page,size,Sort.by("nom"));
        return ResponseEntity.ok(passagerService.search(q,pageable));
    }

    //Modifier passager
    @PutMapping("/{id}")
    public ResponseEntity<PassagerResponse> update(@PathVariable Long id, @Valid @RequestBody PassagerRequest request){
        return ResponseEntity.ok(passagerService.update(id,request));
    }
    //suppression
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        passagerService.delete(id);
        return ResponseEntity.noContent().build();
    }
    //Historique des reservations
    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationResponse>> findReservations(@PathVariable Long id){
        return ResponseEntity.ok(passagerService.findReservations(id));
    }
}
