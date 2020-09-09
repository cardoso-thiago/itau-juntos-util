package br.com.itau.util.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ItauSystemException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
