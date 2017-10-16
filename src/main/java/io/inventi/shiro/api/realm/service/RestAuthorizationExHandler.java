package io.inventi.shiro.api.realm.service;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestAuthorizationExHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> authorizationErrorHandler(AuthorizationException e) throws Exception {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
