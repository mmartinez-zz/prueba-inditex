package com.inditex.pricing.infrastructure.rest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debe retornar 404 con ErrorResponse cuando no existe precio aplicable")
    void shouldReturn404WithErrorResponseWhenNoPriceFound() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2025-01-01T10:00:00")
                        .param("productId", "99999")
                        .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Debe retornar 400 con ErrorResponse cuando applicationDate es null")
    void shouldReturn400WhenApplicationDateIsNull() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar 400 con ErrorResponse cuando productId es null")
    void shouldReturn400WhenProductIdIsNull() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar 400 con ErrorResponse cuando brandId es null")
    void shouldReturn400WhenBrandIdIsNull() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar 400 con ErrorResponse cuando applicationDate tiene formato inválido")
    void shouldReturn400WhenApplicationDateHasInvalidFormat() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "invalid-date")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }
}
