package com.arpit.crm_ticketing_api.controller;

import com.arpit.crm_ticketing_api.entity.Ticket;
import com.arpit.crm_ticketing_api.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> create(
            @Valid @RequestBody Ticket ticket) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticketService.create(ticket));
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> findAll() {
        return ResponseEntity.ok(ticketService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ticketService.findById(id)
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Ticket> update(
            @PathVariable Long id,
            @Valid @RequestBody Ticket ticket) {

        return ResponseEntity.ok(
                ticketService.update(id, ticket)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        ticketService.delete(id);

        return ResponseEntity.noContent().build();
    }
}