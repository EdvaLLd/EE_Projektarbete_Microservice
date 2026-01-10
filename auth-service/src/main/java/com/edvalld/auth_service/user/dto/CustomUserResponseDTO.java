package com.edvalld.auth_service.user.dto;



import java.util.List;
import java.util.Set;
import java.util.UUID;

public record CustomUserResponseDTO(
        String username,
        List<String> roles
) {
}
