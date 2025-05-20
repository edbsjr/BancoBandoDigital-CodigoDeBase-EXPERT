package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClienteNaoAtualizadoException extends RuntimeException {
    public ClienteNaoAtualizadoException(String message) {
        super(message);
    }
    public ClienteNaoAtualizadoException(String message, Throwable cause) {
    super(message, cause);
  }
}
