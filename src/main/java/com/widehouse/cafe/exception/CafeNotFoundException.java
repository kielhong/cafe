package com.widehouse.cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by kiel on 2017. 3. 7..
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Cafe")
public class CafeNotFoundException extends RuntimeException {
}
