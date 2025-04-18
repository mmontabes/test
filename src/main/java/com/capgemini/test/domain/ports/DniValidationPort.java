package com.capgemini.test.domain.ports;

public interface DniValidationPort {
    boolean validateDni(String dni);
    boolean isValid(String dni);

}