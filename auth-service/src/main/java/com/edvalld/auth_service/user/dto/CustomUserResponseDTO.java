package com.edvalld.auth_service.user.dto;



import java.util.List;
import java.util.Set;

public record CustomUserResponseDTO(
        String username,
        List<String> roles
) {
}
