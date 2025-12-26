package com.edvalld.auth_service.controller;

import com.edvalld.JwtUtils;
import com.edvalld.auth_service.user.CustomUser;
import com.edvalld.auth_service.user.CustomUserDetails;
import com.edvalld.auth_service.user.CustomUserDetailsService;
import com.edvalld.auth_service.user.CustomUserRepository;
import com.edvalld.auth_service.user.dto.CustomUserResponseDTO;
import com.edvalld.auth_service.user.dto.LoginDTO;
import com.edvalld.auth_service.user.dto.RegisterUserDTO;
import com.edvalld.auth_service.user.mapper.CustomUserMapper;
import com.edvalld.role.UserRole;
import com.edvalld.role.UserRoleName;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class GlobalController {
    // TODO - Replace with Service in the future
    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserMapper customUserMapper;
    private final AuthenticationManager authenticationManager;
    private final String keyValue;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    @Autowired
    public GlobalController(
            CustomUserRepository customUserRepository,
            PasswordEncoder passwordEncoder,
            CustomUserMapper customUserMapper,
            AuthenticationManager authenticationManager,
            @Value("${base64.secret.key}")String base64EncodedSecretKey,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserMapper = customUserMapper;
        this.authenticationManager = authenticationManager;
        keyValue = base64EncodedSecretKey;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtils = JwtUtils.getInstance();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.username(),
                            loginDTO.password()
                    )
            );
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            if (user == null) {
                return ResponseEntity.status(401).build();
            }

            List<String> authorities = user.getAuthorities().stream().map(Object::toString).toList();

            String jwt = JwtUtils.getInstance().generateJwtToken(
                    authorities,
                    user.getUsername(),
                    keyValue

            );

            // Skicka JWT i header
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(Map.of(
                            "username", user.getUsername(),
                            "roles", user.getAuthorities()
                                    .stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList())
                    ));

        } catch (
                UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
          catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        if (customUserRepository.findUserByUsername(registerUserDTO.username()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "USERNAME_EXISTS",
                            "message", "A user with that username already exists"
                    ));
        }

        CustomUser user = customUserMapper.toEntity(registerUserDTO);
        user.setPassword(registerUserDTO.password(), passwordEncoder);

        customUserRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "User registered successfully"
                ));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        Cookie cookie = new Cookie("authToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logged out successfully");
    }



    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteUser(
            @RequestParam UUID userId,
            @RequestHeader(name = "Authorization", required = false) String authHeader
    ){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        List<String> roles = jwtUtils.getRolesFromJwtToken(token, keyValue);
        if (!roles.contains(UserRole.ADMIN.name())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(customUserRepository.findById(userId).isPresent()) {
            customUserRepository.deleteById(userId);
            return ResponseEntity.status(200).body("User deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User not found");
    }

    @GetMapping("/me")
    public ResponseEntity<CustomUserResponseDTO> getCurrentUser(
            @RequestHeader(name = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        String username = jwtUtils.getUsernameFromJwtToken(token, keyValue);
        UserDetails user = customUserDetailsService.loadUserByUsername(username);
        CustomUserResponseDTO dto = new CustomUserResponseDTO(user.getUsername(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
        return ResponseEntity.ok(dto);
    }



    /*

    @GetMapping("/admin")
    public String adminPage(Model model, Principal principal) {
        //skickar in alla användare förutom den som är inloggad
        model.addAttribute("users", customUserRepository.findAll()
                .stream()
                .filter(user -> !user.getId().equals(customUserRepository.findUserByUsername(principal.getName()).get().getId()))
                .toList());
        return "adminpage"; // Must Reflect .html document name
    }

    @GetMapping("/user")
    public String userPage() {

        return "userpage";
    }

    @DeleteMapping("/removeUser")
    public String removeUser(@RequestParam UUID userId) {
        customUserRepository.deleteById(userId);
        return "redirect:/admin";
    }


    */
}
