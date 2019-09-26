package com.emadmessiha.eventviewer.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  public String getExceptionResponseBody(Exception ex) {
    return ex.getClass().getName() + ": " + ex.getMessage();
  }
 
  @ExceptionHandler({ Exception.class })
  protected ResponseEntity<Object> handleServerException(
    Exception ex, WebRequest request) {
      ex.printStackTrace();
      HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
      return handleExceptionInternal(ex, statusCode.name() + " " + getExceptionResponseBody(ex),
        new HttpHeaders(), statusCode, request);
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  protected ResponseEntity<Object> handleBadRequestException(
    Exception ex, WebRequest request) {
      ex.printStackTrace();
      return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST.name() + " " + getExceptionResponseBody(ex), 
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
}
