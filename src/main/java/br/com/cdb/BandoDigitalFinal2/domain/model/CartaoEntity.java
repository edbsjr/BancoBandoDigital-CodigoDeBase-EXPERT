package br.com.cdb.BandoDigitalFinal2.domain.model;

import br.com.cdb.BandoDigitalFinal2.enums.Situacao;
import br.com.cdb.BandoDigitalFinal2.enums.TipoCartao;

import java.math.BigDecimal;

public class CartaoEntity {

    private Long idCartao;
    private String senha;
    private TipoCartao tipo;
    private Situacao situacao;
    private Long idConta;
    private BigDecimal valorFatura;
    private BigDecimal limite;
    private BigDecimal limiteUsado;

    public CartaoEntity()
    {}

    public Long getIdCartao() {
        return idCartao;
    }

    public void setIdCartao(Long idCartao) {
        this.idCartao = idCartao;
    }

    public BigDecimal getLimiteUsado() {
        return limiteUsado;
    }

    public void setLimiteUsado(BigDecimal limiteUsado) {
        this.limiteUsado = limiteUsado;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoCartao getTipo() {
        return tipo;
    }

    public void setTipo(TipoCartao tipo) {
        this.tipo = tipo;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    public BigDecimal getValorFatura() {
        return valorFatura;
    }

    public void setValorFatura(BigDecimal valorFatura) {
        this.valorFatura = valorFatura;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }
}
