package org.safetrust.managecontacts.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger =
      LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    /**
     * Handles exceptions of type MethodArgumentNotValidException, which are thrown
     * when method argument validation fails due to constraint violations.
     *
     * @param ex the MethodArgumentNotValidException that contains details about the validation errors.
     * @return a ResponseEntity containing a detailed error message and an HTTP 400 (Bad Request) status.
     */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
    StringBuilder errorMessage = new StringBuilder("Validation failed:");
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              errorMessage
                  .append(" Field '")
                  .append(error.getField())
                  .append("': ")
                  .append(error.getDefaultMessage())
                  .append(";");
              logger.error(
                  "Validation error on field '{}': {}",
                  error.getField(),
                  error.getDefaultMessage());
            });
    return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
  }

    /**
     * Handles exceptions of type ConstraintViolationException, which are thrown
     * when validation constraints are violated, typically for parameters annotated
     * with validation annotations in methods.
     *
     * @param ex the ConstraintViolationException containing details about the violated constraints.
     * @return a ResponseEntity with a detailed error message and an HTTP 400 (Bad Request) status code.
     */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(
      ConstraintViolationException ex) {
    StringBuilder errorMessage = new StringBuilder("Constraint violations:");
    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
    violations.forEach(
        violation -> {
          errorMessage
              .append(" ")
              .append(violation.getPropertyPath())
              .append(" ")
              .append(violation.getMessage())
              .append(";");
          logger.error(
              "Validation error: {}, Invalid value: {}",
              violation.getMessage(),
              violation.getInvalidValue());
        });
    return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception ex) {
    logger.error("Unhandled exception: " + ex.getMessage());
    return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
