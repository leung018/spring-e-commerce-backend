package com.leungcheng.spring_simple_backend.controller;

import com.leungcheng.spring_simple_backend.validation.ObjectValidator.ObjectValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ExceptionHandlerAdvice {
  @ExceptionHandler(ProductNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String productNotFoundHandler(ProductNotFoundException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  String usernameAlreadyExistsHandler(UsernameAlreadyExistsException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(ObjectValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String objectValidationHandler(ObjectValidationException ex) {
    return formatValidationMessage(ex.getFirstErrorField(), ex.getFirstErrorMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
    // FIXME: This part is without any test coverage because I cannot trigger this handler in unit
    // tests. But I can manually test it when running the app.

    BindingResult bindingResult = ex.getBindingResult();
    FieldError firstError = bindingResult.getFieldError();
    if (firstError == null) {
      return "Invalid request";
    }
    return formatValidationMessage(firstError.getField(), firstError.getDefaultMessage());
  }

  private static String formatValidationMessage(String errorField, String errorMessage) {
    return errorField + ": " + errorMessage;
  }
}
