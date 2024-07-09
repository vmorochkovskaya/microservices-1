package com.training.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Mp3Validator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMp3 {
    String message() default "Validation failed or request body is invalid MP3";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
