package com.capgemini.test.infrastructure.clients.dni;

import com.capgemini.test.domain.ports.DniValidationPort;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class DniValidationAdapter implements DniValidationPort {

    private final DniFeignClient dniFeignClient;

    public DniValidationAdapter(DniFeignClient dniFeignClient) {
        this.dniFeignClient = dniFeignClient;
    }

    @Override
    public boolean validateDni(String dni) {
        try {
            Map<String, String> request = new HashMap<>();
            request.put("dni", dni);
            dniFeignClient.checkDni(request); // <- realiza el PATCH
            return true; // 200 OK = válido
        } catch (FeignException.Conflict e) {
            return false; // 409 = inválido
        } catch (FeignException e) {
            throw new RuntimeException("Error al validar DNI: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isValid(String dni) {
        try {
            Map<String, String> request = new HashMap<>();
            request.put("dni", dni);
            dniFeignClient.checkDni(request);
            return true;
        } catch (FeignException.Conflict e) {
            // 409 → DNI inválido
            return false;
        } catch (FeignException e) {
            // Cualquier otro error → propagar
            throw new RuntimeException("Error al validar DNI: " + e.getMessage(), e);
        }
    }
}
