package com.murphy1.serviced.serviced.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(){
        super();
    }

    public ForbiddenException(String message){
        super(message);
    }

    public ForbiddenException(String message, Throwable cause){
        super(message, cause);
    }

}
