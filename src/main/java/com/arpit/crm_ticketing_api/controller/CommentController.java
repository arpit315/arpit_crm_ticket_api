package com.arpit.crm_ticketing_api.controller;

import com.arpit.crm_ticketing_api.entity.Comment;
import com.arpit.crm_ticketing_api.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> create(
            @Valid @RequestBody Comment comment) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.create(comment));
    }

    @GetMapping
    public ResponseEntity<List<Comment>> findAll() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                commentService.findById(id)
        );
    }
}