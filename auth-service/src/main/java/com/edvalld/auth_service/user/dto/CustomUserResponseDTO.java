package com.edvalld.auth_service.user.dto;



import java.util.List;

public record CustomUserResponseDTO(
        String username,
        List<String> roles
) {
}
