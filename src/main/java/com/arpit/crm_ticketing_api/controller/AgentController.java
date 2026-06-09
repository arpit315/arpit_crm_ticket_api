package com.arpit.crm_ticketing_api.controller;

import com.arpit.crm_ticketing_api.dto.AgentRequest;
import com.arpit.crm_ticketing_api.dto.AgentResponse;
import com.arpit.crm_ticketing_api.service.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @PostMapping
    public ResponseEntity<AgentResponse> create(@Valid @RequestBody AgentRequest request) {
        return ResponseEntity.ok(agentService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AgentResponse>> findAll() {
        return ResponseEntity.ok(agentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(agentService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentResponse> update(@PathVariable Long id, @Valid @RequestBody AgentRequest request) {
        return ResponseEntity.ok(agentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
