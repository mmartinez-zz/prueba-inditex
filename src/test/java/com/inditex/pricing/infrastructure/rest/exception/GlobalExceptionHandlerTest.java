package com.inditex.pricing.infrastructure.rest.exception;

import com.inditex.pricing.infrastructure.rest.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Debe manejar IllegalArgumentException y retornar 400 Bad Request")
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid parameter");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Bad Request", response.getBody().error());
        assertEquals("Invalid parameter", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    @DisplayName("Debe manejar NoSuchElementException y retornar 404 Not Found")
    void shouldHandleNoSuchElementException() {
        NoSuchElementException exception = new NoSuchElementException("Price not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNoSuchElementException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Not Found", response.getBody().error());
        assertEquals("Price not found", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    @DisplayName("Debe manejar Exception genérica y retornar 500 Internal Server Error")
    void shouldHandleGenericException() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertEquals("Internal Server Error", response.getBody().error());
        assertEquals("An unexpected error occurred", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }
}
