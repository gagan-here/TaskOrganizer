package interview.taskorganizer.exceptions;

import jakarta.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Global exception handler. */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String ERROR_MSG = "Exception caught at the handler. Exception message: {}";

  /** handles all 400 - Bad Request related exceptions here. */
  @ExceptionHandler(value = {BusinessLogicException.class})
  public ProblemDetail handleBusinessLogicExceptions(BusinessLogicException exception) {
    log.error(ERROR_MSG, exception.getMessage(), exception);
    final var problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    problemDetail.setProperty("code", exception.getErrorCode());

    return problemDetail;
  }

  /** handles all 404 - Not Found related exceptions here. */
  @ExceptionHandler(
      value = {
        NoSuchElementException.class,
        EntityNotFoundException.class,
        ResourceNotFoundException.class
      })
  public ProblemDetail handleNotFoundExceptions(RuntimeException exception) {
    log.error(ERROR_MSG, exception.getMessage(), exception);
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  /** handles all 500 - Internal Server Error related exceptions here. */
  @ExceptionHandler(value = {RuntimeException.class})
  public ProblemDetail handleRuntimeExceptions(RuntimeException exception) {
    log.error(ERROR_MSG, exception.getMessage(), exception);
    final var problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());

    final var cause = exception.getCause();
    if (!Objects.isNull(cause)) {
      problemDetail.setProperty("cause", cause.getMessage());
    }

    return problemDetail;
  }
}
