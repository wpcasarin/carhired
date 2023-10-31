package org.ifsul.carhired.api.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    private final TestEntityRepository repository;

    @GetMapping
    public ResponseEntity<List<TestEntity>> getAll() {
        var entities = repository.findAll();
        if (!entities.isEmpty()) {
            return ResponseEntity.ok(entities);
        }
        return ResponseEntity.noContent().build();
    }
    @PostMapping
    public ResponseEntity<TestEntity> add() {
        var id = UUID.randomUUID();
        var value = UUID.randomUUID().toString();
        var entity = TestEntity.builder().id(id).input(value).build();
        return ResponseEntity.status(201).body(repository.save(entity));
    }
}
