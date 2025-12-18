package com.edvalld.task_service.task;

import jakarta.validation.constraints.NotBlank;

public record TaskDTO(@NotBlank String name, @NotBlank String description) {
}
