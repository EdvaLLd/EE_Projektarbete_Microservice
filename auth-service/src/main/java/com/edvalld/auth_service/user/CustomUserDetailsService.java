package com.edvalld.auth_service.user;

import com.edvalld.auth_service.user.dto.CustomUserResponseDTO;
import com.edvalld.role.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    @Autowired
    public CustomUserDetailsService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomUser customUser = customUserRepository.findUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username " + username + " Was not found")
                );

        return new CustomUserDetails(customUser);
    }

    public List<CustomUserResponseDTO> getAllUsers(){

        List<CustomUserResponseDTO> allUsers = customUserRepository
                .findAll()
                .stream()
                .map(u -> new CustomUserResponseDTO(
                        u.getUsername(),
                        u.getRoles().stream()
                                .map(UserRole::getRoleName)
                                .toList()
                )).toList();

        return allUsers;
    }
}
