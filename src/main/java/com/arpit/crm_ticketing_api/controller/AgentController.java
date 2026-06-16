package com.arpit.crm_ticketing_api.controller;

import com.arpit.crm_ticketing_api.entity.Agent;
import com.arpit.crm_ticketing_api.service.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping
    public ResponseEntity<Agent> create(
            @Valid @RequestBody Agent agent) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agentService.create(agent));
    }

    @GetMapping
    public ResponseEntity<List<Agent>> findAll() {
        return ResponseEntity.ok(agentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agent> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                agentService.findById(id)
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Agent> update(
            @PathVariable Long id,
            @Valid @RequestBody Agent agent) {

        return ResponseEntity.ok(
                agentService.update(id, agent)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        agentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}