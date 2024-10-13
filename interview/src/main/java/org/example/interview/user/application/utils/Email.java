package org.example.interview.user.application.utils;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = Email.EmailValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Email {
    String message() default "is invalid email format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class EmailValidator implements ConstraintValidator<Email, String> {

        @Deprecated
        public static boolean isValid(String email) {
            return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            if (email != null) {
                return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
            }
            return true;
        }
    }
}
