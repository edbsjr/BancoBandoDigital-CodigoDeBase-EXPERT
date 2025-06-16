package br.com.cdb.BandoDigitalFinal2.domain.model;

import br.com.cdb.BandoDigitalFinal2.enums.TipoConta;

import java.math.BigDecimal;

public class ContaEntity {

    private Long idConta;
    private Long idCliente;
    private BigDecimal saldo;
    private TipoConta tipoConta;
    private BigDecimal rendimento;
    private BigDecimal manutencao;

    public ContaEntity()
    {}

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public BigDecimal getRendimento() {
        return rendimento;
    }

    public void setRendimento(BigDecimal rendimento) {
        this.rendimento = rendimento;
    }

    public BigDecimal getManutencao() {
        return manutencao;
    }

    public void setManutencao(BigDecimal manutencao) {
        this.manutencao = manutencao;
    }

    public void debitar(BigDecimal valor) {

        if(valor.compareTo(getSaldo()) <= 0)
            setSaldo(getSaldo().subtract(valor));
        else
            throw new RuntimeException("Saldo insuficiente"); // TODO VERIFICAR MELHOR COMO TRATAR ESSE ERRO

    }

       public void creditar(BigDecimal valor) {
        setSaldo(getSaldo().add(valor));}
}
