package ru.practicum.shareit.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    //Для валидации State в BookingController
    @ExceptionHandler(InvalidStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleInvalidStateException(InvalidStateException exc) {
        return new ExceptionResponse(exc.getMessage());
    }

    //Для валидации Boolean approvedStatus в BookingController
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exc) {
        String paramName = exc.getName();
        String requiredType;
        if (exc.getRequiredType() != null) {
            requiredType = exc.getRequiredType().getSimpleName();
        } else {
            requiredType = "Неизвестный тип";
        }
        String message = String.format("Параметр '%s' должен быть типа %s", paramName, requiredType);
        return new ExceptionResponse(message);
    }
}