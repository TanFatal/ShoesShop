package com.example.learningAPISpring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundEX extends RuntimeException {
    public ResourceNotFoundEX(String s) {
        super(s);
    }

    public ResourceNotFoundEX(String s, Throwable cause){
        super(s,cause);
    }
}
