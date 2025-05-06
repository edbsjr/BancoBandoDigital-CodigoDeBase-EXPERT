package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHander extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<Object> handleClienteNaoEncontradoException(ClienteNaoEncontradoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<Object> handleCpfInvalidoException(CpfInvalidoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NomeInvalidoException.class)
    public ResponseEntity<Object> handleNomeInvalidoException(NomeInvalidoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IdadeInvalidaException.class)
    public ResponseEntity<Object> handleIdadeInvalidaException(IdadeInvalidaException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<Object> handleContaNaoEncontradaException(ContaNaoEncontradaException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValorNegativoNaoPermitidoException.class)
    public ResponseEntity<Object> handleValorNegativoNaoPermitidoException(ValorNegativoNaoPermitidoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Object> handleSaldoInsuficienteException(SaldoInsuficienteException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }
}
