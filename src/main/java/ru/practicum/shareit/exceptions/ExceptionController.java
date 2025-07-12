package ru.practicum.shareit.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ExceptionResponse handleNotFoundException(NotFoundException exc) {
        log.error("Ошибка при поиске: {}", exc.getMessage());
        return new ExceptionResponse(exc.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException exc) {
        log.error("Ошибка при валидации: {}", exc.getMessage());

        return ResponseEntity
                .status(exc.getStatus())
                .body(new ExceptionResponse(exc.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleConstraintViolationException(ConstraintViolationException exc) {
        log.error("Некорректный ввод данных в запросе: {}", exc.getMessage());
        return new ExceptionResponse("Передаваемые параметры должны быть больше 0");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        String message = exc.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Ошибка валидации");
        log.error("Некорректный ввод данных в запросе: {}", message);
        return new ExceptionResponse(message);
    }
}
