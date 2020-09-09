package br.com.itau.util.exception;

import org.springframework.http.HttpStatus;

public class ItauSystemException extends RuntimeException {

    private HttpStatus httpStatus;

    ItauSystemException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatusCode(){
        return this.httpStatus;
    }
}
