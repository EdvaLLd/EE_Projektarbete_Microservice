package com.edvalld.role;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.edvalld.role.UserPermission.*;

public enum UserRole {
    GUEST(
            UserRoleName.GUEST.getRoleName(),
            Set.of() // 0 Permissions // READ permission could be available here!
    ),

    USER(
            UserRoleName.USER.getRoleName(),
            Set.of(
                    READ,
                    WRITE
            )
    ),

    ADMIN(
            UserRoleName.ADMIN.getRoleName(),
            Set.of(
                    READ,
                    WRITE,
                    DELETE
            )
    );

    private final String roleName;
    private final Set<UserPermission> userPermissions;

    UserRole(String roleName, Set<UserPermission> userPermissions) {
        this.roleName = roleName;
        this.userPermissions = userPermissions;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }


}
