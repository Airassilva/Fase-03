package dev.aira.agendamento.exceptions.handler;

import dev.aira.agendamento.exceptions.BadRequestBusinessException;
import dev.aira.agendamento.exceptions.NotFoundBusinessException;
import dev.aira.agendamento.exceptions.UnauthorizedAccessBusinessException;
import dev.aira.agendamento.exceptions.UnavailableBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(NotFoundBusinessException.class)
    public ResponseEntity<ApiError> handleNotFoundBusinessException(NotFoundBusinessException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(e.getMessage()));
    }

    @ExceptionHandler(BadRequestBusinessException.class)
    public ResponseEntity<ApiError> handleBadRequestBusinessException(BadRequestBusinessException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessBusinessException.class)
    public ResponseEntity<ApiError> handleUnauthorizedAccessBusinessException(UnauthorizedAccessBusinessException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(e.getMessage()));
    }

    @ExceptionHandler(UnavailableBusinessException.class)
    public ResponseEntity<ApiError> handleUnavailable(UnavailableBusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(erros));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeral(Exception ex) {

        log.error("Erro interno", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("Erro interno do sistema"));
    }
}
