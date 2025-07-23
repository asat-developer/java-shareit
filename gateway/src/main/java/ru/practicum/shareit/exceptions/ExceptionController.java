package ru.practicum.shareit.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

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