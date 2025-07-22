package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException exc) {
        log.error("Ошибка при поиске: {}", exc.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(exc.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException exc) {
        log.error("Ошибка при валидации: {}", exc.getMessage());
        return ResponseEntity
                .status(exc.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(exc.getError()));
    }
}