package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TipoCartaoInvalidoException extends RuntimeException {
    public TipoCartaoInvalidoException(String message) {super(message);}
    public TipoCartaoInvalidoException(String message, Throwable cause) {
    super(message, cause);
  }
}
