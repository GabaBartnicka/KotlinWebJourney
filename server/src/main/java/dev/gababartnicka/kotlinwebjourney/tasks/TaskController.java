package dev.gababartnicka.kotlinwebjourney.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repository;

    @GetMapping
    public List<TaskDto> getAll() {
		log.info("GET /api/tasks");
        var tasks = repository.findAll();
        return tasks.stream().map(TaskDto::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable Long id) {
		log.info("GET /api/tasks/{}", id);
        var maybe = repository.findById(id).map(TaskDto::fromEntity);
        return maybe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto dto) {
        // Accept DTO; id must be provided since Task has no @GeneratedValue
        var toSave = TaskDto.toEntity(dto);
        var saved = repository.save(toSave);
        var body = TaskDto.fromEntity(saved);
        var location = URI.create("/api/tasks/" + saved.getId());
        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable Long id, @RequestBody TaskDto dto) {
        // Update existing if present; otherwise create with given id
        var existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            var toCreate = Task.builder()
                    .id(id)
                    .name(dto.name())
                    .done(dto.done())
                    .build();
            var created = repository.save(toCreate);
            return ResponseEntity.ok(TaskDto.fromEntity(created));
        }
        var existing = existingOpt.get();
        existing.setName(dto.name());
        existing.setDone(dto.done());
        var updated = repository.save(existing);
        return ResponseEntity.ok(TaskDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/done")
    public ResponseEntity<TaskDto> markDone(@PathVariable Long id) {
        var existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var task = existingOpt.get();
        task.markAsDone();
        var saved = repository.save(task);
        return ResponseEntity.ok(TaskDto.fromEntity(saved));
    }
}
