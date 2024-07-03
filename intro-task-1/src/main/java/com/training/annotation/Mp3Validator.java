package com.training.annotation;

import org.apache.tika.Tika;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Mp3Validator implements ConstraintValidator<ValidMp3, byte[]> {
    private static final Tika tika = new Tika();

    @Override
    public void initialize(ValidMp3 constraintAnnotation) {
    }

    @Override
    public boolean isValid(byte[] fileData, ConstraintValidatorContext context) {
        if (fileData == null || fileData.length == 0) {
            return false;
        }
        String mimeType = tika.detect(fileData);
        return "audio/mpeg".equals(mimeType);
    }
}