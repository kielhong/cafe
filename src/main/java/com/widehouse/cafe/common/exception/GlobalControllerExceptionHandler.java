package com.widehouse.cafe.common.exception;

import com.widehouse.cafe.common.exception.BoardNotExistsException;
import com.widehouse.cafe.common.exception.CafeNotFoundException;
import com.widehouse.cafe.common.exception.NoAuthorityException;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({CafeNotFoundException.class, BoardNotExistsException.class})
    public void handleResourceNotExistsException() {
    }
}
