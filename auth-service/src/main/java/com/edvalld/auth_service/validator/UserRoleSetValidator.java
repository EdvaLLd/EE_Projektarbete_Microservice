package com.edvalld.auth_service.validator;

import com.edvalld.auth_service.user.role.UserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;



public class UserRoleSetValidator implements ConstraintValidator<ValidRoles, Set<UserRole>> {

    //dessa behöver matcha rollerna som finns
    private static final Set<UserRole> ALLOWED = Set.of(UserRole.GUEST, UserRole.USER, UserRole.ADMIN);

    @Override
    public boolean isValid(Set<UserRole> roles, ConstraintValidatorContext context) {
        return roles != null && ALLOWED.containsAll(roles);
    }
}
