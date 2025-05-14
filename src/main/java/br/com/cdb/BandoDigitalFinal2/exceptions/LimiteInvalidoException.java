package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus()
public class LimiteInvalidoException extends RuntimeException {
    public LimiteInvalidoException(String message) {
        super(message);
    }
    public LimiteInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
