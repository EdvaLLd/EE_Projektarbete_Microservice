package com.edvalld.auth_service.user;

import com.edvalld.role.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {

    private final CustomUser customUser;

    public CustomUserDetails(CustomUser customUser) {
        this.customUser = customUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        final Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        customUser.getRoles().forEach(

                userRole -> authorities.addAll(getUserAuthorities(userRole)) // Merge arrays
        );

        return Collections.unmodifiableSet(authorities); // Make List 'final' through 'unmodifiable'
    }

    // Get a LIST that Spring understands - containing both ROLE + PERMISSION
    public List<SimpleGrantedAuthority> getUserAuthorities(UserRole userRole) {

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // this == the choice made after UserRole. (e.g: UserRole.ADMIN)
        authorityList.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        authorityList.addAll(
                userRole.getUserPermissions().stream().map(
                        userPermission -> new SimpleGrantedAuthority(userPermission.getUserPermission())
                ).toList()
        );

        return authorityList;
    }

    public CustomUser getCustomUser() {
        return customUser;
    }

    @Override
    public String getPassword() {
        return customUser.getPassword();
    }

    @Override
    public String getUsername() {
        return customUser.getUsername();
    }
}
