package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegistroNaoDeletadoException extends RuntimeException {
    public RegistroNaoDeletadoException(String message) {
        super(message);
    }
    public RegistroNaoDeletadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
