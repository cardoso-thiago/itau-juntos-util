package br.com.itau.util.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ItauSystemException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
