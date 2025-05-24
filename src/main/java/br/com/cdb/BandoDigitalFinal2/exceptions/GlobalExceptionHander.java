package br.com.cdb.BandoDigitalFinal2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHander extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<Object> handleRegistroNaoEncontradoException(RegistroNaoEncontradoException ex)
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

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<Object> handleSenhaInvalidaException(SenhaInvalidaException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TipoCartaoInvalidoException.class)
    public ResponseEntity<Object> handleTipoCartaoInvalidoException(TipoCartaoInvalidoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LimiteInvalidoException.class)
    public ResponseEntity<Object> handleLimiteInvalidoException(LimiteInvalidoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartaoSituacaoInvalidaException.class)
    public ResponseEntity<Object> handleCartaoSituacaoInvalidaException(CartaoSituacaoInvalidaException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistroNaoSalvoException.class)
    public ResponseEntity<Object> handleRegistroNaoSalvoException(RegistroNaoSalvoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RegistroNaoAtualizadoException.class)
    public ResponseEntity<Object> handleRegistroNaoAtualizadoException(RegistroNaoAtualizadoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistroNaoDeletadoException.class)
    public ResponseEntity<Object> handleRegistroNaoDeletadoException(RegistroNaoDeletadoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistroEmDuplicidadeException.class)
    public ResponseEntity<Object> handleRegistroEmDuplicidadeException(RegistroEmDuplicidadeException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FalhaAoAcessarBaseException.class)
    public ResponseEntity<Object> handleFalhaAoAcessarBaseException(FalhaAoAcessarBaseException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CampoInvalidoException.class)
    public ResponseEntity<Object> handleCampoInvalidoException(CampoInvalidoException ex)
    {
        String mensagem = ex.getMessage();
        return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
    }
}
