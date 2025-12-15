package com.edvalld.auth_service.user.dto;

import com.edvalld.auth_service.user.role.UserRole;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

public record CustomUserResponseDTO(
        String username,
        List<String> roles
) {
}
