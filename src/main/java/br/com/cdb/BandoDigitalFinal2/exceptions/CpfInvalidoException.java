package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CpfInvalidoException extends RuntimeException {

    public CpfInvalidoException(String message)
    {
        super(message);
    }
    public CpfInvalidoException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
