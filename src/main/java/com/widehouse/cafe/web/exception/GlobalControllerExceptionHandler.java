package com.widehouse.cafe.web.exception;

import com.widehouse.cafe.exception.NoAuthorityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by kiel on 2017. 2. 26..
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NoAuthorityException.class)
    public void handleNoAuthority() {
    }
}
