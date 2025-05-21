package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RegistroNaoSalvoException extends RuntimeException {
    public RegistroNaoSalvoException(String message) {
        super(message);
    }
    public RegistroNaoSalvoException(String message, Throwable cause) {
        super(message, cause);
    }
}
