package br.com.cdb.BandoDigitalFinal2.application.port.in;

import br.com.cdb.BandoDigitalFinal2.domain.model.Conta;
import br.com.cdb.BandoDigitalFinal2.enums.TipoConta;

import java.math.BigDecimal;
import java.util.List;

public interface ContaInputPort {
    public void addConta(Long clienteId, TipoConta tipoConta);
    public List<Conta> listarTodos();
    public Conta buscarConta(Long idConta);
    public BigDecimal consultarSaldo(Long idConta);
    public void transferirValor(Long idOrigem, BigDecimal valor, Long idDestino);
    public void pagarPix(Long idConta, BigDecimal valor);
    public void depositar(Long idConta, BigDecimal valor);
    public void sacar(Long idConta, BigDecimal valor);
    public void manutencao(Long idConta);
    public void rendimento(Long idConta);
}
