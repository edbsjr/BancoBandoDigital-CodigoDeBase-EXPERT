package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FalhaAoAcessarBaseException extends RuntimeException {
    public FalhaAoAcessarBaseException(String message) {
        super(message);
    }
    public FalhaAoAcessarBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
