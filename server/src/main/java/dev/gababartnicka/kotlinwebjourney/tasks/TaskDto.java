package dev.gababartnicka.kotlinwebjourney.tasks;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Task entity. Returned to clients and accepted on create/update.
 */
public record TaskDto(
        Long id,
        String name,
        LocalDateTime created,
        LocalDateTime updated,
        boolean done
) {
    // Mapper methods between entity and DTO
    public static TaskDto fromEntity(Task task) {
        if (task == null) return null;
        return new TaskDto(task.getId(), task.getName(), task.getCreated(), task.getUpdated(), task.isDone());
    }

    public static Task toEntity(TaskDto dto) {
        if (dto == null) return null;
        return Task.builder()
                .id(dto.id())
                .name(dto.name())
                .created(dto.created())
                .updated(dto.updated())
                .done(dto.done())
                .build();
    }
}
