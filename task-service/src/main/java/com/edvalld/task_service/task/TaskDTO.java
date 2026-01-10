package com.edvalld.task_service.task;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record TaskDTO(UUID id, @NotBlank String name, @NotBlank String description) {
}
