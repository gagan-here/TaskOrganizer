package interview.taskorganizer.exceptions;

import lombok.Getter;

/** Custom exception for throwing business logic related errors. */
@Getter
public final class BusinessLogicException extends RuntimeException {

  private static final String ERROR_CODE_PREFIX = "BLE";

  private final String errorCode;

  public BusinessLogicException(String errorCode, String message) {
    super(message);
    this.errorCode = setErrorCode(errorCode);
  }

  public BusinessLogicException(String errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = setErrorCode(errorCode);
  }

  private String setErrorCode(String errorCode) {
    return String.format("%s-%s", ERROR_CODE_PREFIX, errorCode);
  }
}
