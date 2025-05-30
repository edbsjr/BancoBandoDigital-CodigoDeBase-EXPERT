package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegistroNaoAtualizadoException extends RuntimeException {
    public RegistroNaoAtualizadoException(String message) {
        super(message);
    }
    public RegistroNaoAtualizadoException(String message, Throwable cause) {
    super(message, cause);
  }
}
