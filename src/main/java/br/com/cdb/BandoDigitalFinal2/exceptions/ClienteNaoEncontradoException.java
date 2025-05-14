package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNaoEncontradoException extends RuntimeException{

    public ClienteNaoEncontradoException (String message)
    {
        super(message);
    }

    public ClienteNaoEncontradoException (String message, Throwable cause)
    {
        super(message, cause);
    }
}
