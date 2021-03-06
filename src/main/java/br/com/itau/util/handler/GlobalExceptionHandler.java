package br.com.itau.util.handler;

import br.com.itau.util.model.ErrorResponse;
import br.com.itau.util.model.builder.ErrorResponseBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(value = {CardSystemException.class})
//    protected ResponseEntity<Object> handleCardSystemException(CardSystemException e) {
//        return ResponseEntity.status(e.getHttpStatus()).body(createErrorBody(e.getMessage()));
//    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorBody(e.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorBody(e.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> collect = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorBody(collect));
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorBody(e.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {FeignException.FeignClientException.class, FeignException.FeignServerException.class})
    protected ResponseEntity<Object> handleFeignException(FeignException e) {
        return ResponseEntity.status(e.status()).body(getFeignError(e));
    }

    private String getFeignError(FeignException e) {
        String error = e.getLocalizedMessage();
        Optional<ByteBuffer> body = e.responseBody();
        if (body.isPresent()) {
            error = StandardCharsets.UTF_8.decode(body.get()).toString();
        }
        return error;
    }

    private Object createErrorBody(String error) {
        List<String> errorList = new ArrayList<>();
        errorList.add(error);
        return createErrorBody(errorList);
    }

    private Object createErrorBody(List<String> errorList) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        ErrorResponse errorResponse = ErrorResponseBuilder.anErrorResponse().messages(errorList).timestamp(LocalDateTime.now()).build();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorResponse);
        } catch (JsonProcessingException e) {
            return errorList;
        }
    }
}