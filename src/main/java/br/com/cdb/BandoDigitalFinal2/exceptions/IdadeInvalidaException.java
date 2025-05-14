package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdadeInvalidaException extends RuntimeException {
  public IdadeInvalidaException(String message) {super(message);}
  public IdadeInvalidaException(String message, Throwable cause) {super(message, cause);}
}
