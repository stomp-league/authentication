package com.stompleague.authentication.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  public ResponseEntity<Object> handle(ConstraintViolationException violations) {
    List<Violation> validationErrors = violations.getConstraintViolations().stream()
      .map(violation -> {
        String fieldName = null;
        for (Path.Node node : violation.getPropertyPath()) {
          fieldName = node.getName();
        }
        return new Violation(fieldName, violation.getMessage());
      })
      .collect(Collectors.toList());
    return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UnauthorizedException.class)
  public void handle() {}

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handle(BadRequestException badRequestException) {
    return new ResponseEntity<>(badRequestException.getMessage(), HttpStatus.BAD_REQUEST);
  }

}
