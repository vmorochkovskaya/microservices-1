package com.training.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidCsvStringValidator implements ConstraintValidator<ValidCsvString, String> {

    private static final Pattern CSV_PATTERN = Pattern.compile("^([a-zA-Z0-9]+,)*[a-zA-Z0-9]+$");

    @Override
    public void initialize(ValidCsvString constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.length() >= 200) {
            return false;
        }

        return CSV_PATTERN.matcher(value).matches();
    }
}
