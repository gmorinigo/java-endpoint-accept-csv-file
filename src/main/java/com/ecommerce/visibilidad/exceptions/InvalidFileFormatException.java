package com.ecommerce.visibilidad.exceptions;

import com.ecommerce.visibilidad.constants.ErrorConstants;
import org.springframework.http.HttpStatus;

public class InvalidFileFormatException extends RuntimeException {

    public InvalidFileFormatException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return ErrorConstants.INVALID_FILE_NAME_ERROR_CODE;
    }

    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
