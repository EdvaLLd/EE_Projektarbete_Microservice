package com.edvalld.auth_service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserRoleSetValidator.class) // Validator-klassen
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoles {
    String message() default "Invalid role(s)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}