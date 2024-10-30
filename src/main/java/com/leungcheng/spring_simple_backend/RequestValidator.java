package com.leungcheng.spring_simple_backend;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.springframework.validation.Validator;

public class RequestValidator implements Validator {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final jakarta.validation.Validator validator = factory.getValidator();


    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, org.springframework.validation.Errors errors) {
        var constraintViolations = validator.validate(target);
        for (var violation : constraintViolations) {
            errors.reject(violation.getPropertyPath().toString(), violation.getMessage()); // FIXME: Make the api respond a proper error message
        }
    }
}
