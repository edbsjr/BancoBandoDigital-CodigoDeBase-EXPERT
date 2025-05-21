package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegistroEmDuplicidadeException extends RuntimeException {
    public RegistroEmDuplicidadeException(String message) {
        super(message);
    }
    public RegistroEmDuplicidadeException(String message, Throwable cause) {
        super(message, cause);
    }
}
