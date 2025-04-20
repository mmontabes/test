package com.capgemini.test.infrastructure.clients.dni;

import com.capgemini.test.domain.ports.DniValidationPort;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DniValidationAdapterTest {

    private DniFeignClient dniFeignClient;
    private DniValidationPort dniValidationAdapter;

    @BeforeEach
    void setUp() {
        dniFeignClient = mock(DniFeignClient.class);
        dniValidationAdapter = new DniValidationAdapter(dniFeignClient);
    }

    @Test
    void shouldReturnTrueWhenDniIsValid_validateDni() {
        Map<String, String> request = new HashMap<>();
        request.put("dni", "12345678Z");

        doNothing().when(dniFeignClient).checkDni(request);

        boolean result = dniValidationAdapter.validateDni("12345678Z");

        assertTrue(result);
        verify(dniFeignClient).checkDni(request);
    }

    @Test
    void shouldReturnFalseWhenDniIsInvalid_validateDni() {
        Map<String, String> request = new HashMap<>();
        request.put("dni", "99999999X");

        doThrow(mock(FeignException.Conflict.class)).when(dniFeignClient).checkDni(request);

        boolean result = dniValidationAdapter.validateDni("99999999X");

        assertFalse(result);
        verify(dniFeignClient).checkDni(request);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenOtherErrorOccurs_validateDni() {
        Map<String, String> request = new HashMap<>();
        request.put("dni", "1234ERROR");

        doThrow(mock(FeignException.InternalServerError.class)).when(dniFeignClient).checkDni(request);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dniValidationAdapter.validateDni("1234ERROR");
        });

        assertTrue(exception.getMessage().contains("Error al validar DNI"));
    }

    @Test
    void shouldReturnTrueWhenDniIsValid_isValid() {
        Map<String, String> request = new HashMap<>();
        request.put("dni", "87654321X");

        doNothing().when(dniFeignClient).checkDni(request);

        boolean result = dniValidationAdapter.isValid("87654321X");

        assertTrue(result);
        verify(dniFeignClient).checkDni(request);
    }

    @Test
    void shouldReturnFalseWhenDniIsInvalid_isValid() {
        Map<String, String> request = new HashMap<>();
        request.put("dni", "99999999W");

        doThrow(mock(FeignException.Conflict.class)).when(dniFeignClient).checkDni(request);

        boolean result = dniValidationAdapter.isValid("99999999W");

        assertFalse(result);
        verify(dniFeignClient).checkDni(request);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenOtherErrorOccurs_isValid() {
        Map<String, String> request = new HashMap<>();
        request.put("dni", "BADFORMAT");

        doThrow(mock(FeignException.InternalServerError.class)).when(dniFeignClient).checkDni(request);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dniValidationAdapter.isValid("BADFORMAT");
        });

        assertTrue(exception.getMessage().contains("Error al validar DNI"));
    }
}
