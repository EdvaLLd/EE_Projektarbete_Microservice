package com.edvalld.auth_service.user.mapper;

import com.edvalld.auth_service.user.CustomUser;
import com.edvalld.auth_service.user.dto.CustomUserCreationDTO;
import com.edvalld.auth_service.user.dto.RegisterUserDTO;
import com.edvalld.role.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CustomUserMapper {

    private static final Logger log = LoggerFactory.getLogger(CustomUserMapper.class);

    public CustomUser toEntity(CustomUserCreationDTO customUserCreationDTO) {

        return new CustomUser(
                customUserCreationDTO.username(),
                customUserCreationDTO.password(),
                customUserCreationDTO.roles()
        );
    }

    @Value("${admin.pass}")
    private String adminPass;

    public CustomUser toEntity(RegisterUserDTO registerUserDTO) {

        if(registerUserDTO.adminPass().equals(adminPass))
        {
            log.info("Created user '{}' as an admin", registerUserDTO.username());
            return new CustomUser(
                    registerUserDTO.username(),
                    registerUserDTO.password(),
                    Set.of(UserRole.USER, UserRole.ADMIN)
            );
        }
        log.info("Created user '{}' as a user", registerUserDTO.username());
        return new CustomUser(
                registerUserDTO.username(),
                registerUserDTO.password(),
                Set.of(UserRole.USER)
        );
    }

}