package dev.aira.agendamento.exceptions.handler;

import dev.aira.agendamento.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError("Usuário ou senha inválidos"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiError("Acesso negado: você não tem permissão para acessar este recurso"));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiError> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiError("Acesso negado: você não tem permissão para acessar este recurso"));
    }

    @ExceptionHandler(UnavailableBusinessException.class)
    public ResponseEntity<ApiError> handleUnavailable(UnavailableBusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(InactiveException.class)
    public ResponseEntity<ApiError> handleInactive(InactiveException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
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
