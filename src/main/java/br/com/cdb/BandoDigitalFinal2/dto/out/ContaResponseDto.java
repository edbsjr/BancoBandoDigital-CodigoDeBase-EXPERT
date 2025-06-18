package br.com.cdb.BandoDigitalFinal2.dto.out;

import br.com.cdb.BandoDigitalFinal2.enums.TipoConta;

import java.math.BigDecimal;

public class ContaResponseDto {
    private Long idConta;
    private Long idCliente;
    private BigDecimal saldo;
    private TipoConta tipoConta;
    private BigDecimal rendimento;
    private BigDecimal manutencao;

    public ContaResponseDto() {}

    public ContaResponseDto(Long idConta, Long idCliente, BigDecimal saldo, TipoConta tipoConta, BigDecimal rendimento,
                            BigDecimal manutencao){
        this.idConta = idConta;
        this.idCliente = idCliente;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
        this.rendimento = rendimento;
        this.manutencao = manutencao;
    }

    public Long getIdConta(){return idConta;}
    public void setIdConta(Long idConta) {this.idConta = idConta;}
    public Long getIdCliente(){return idCliente;}
    public void setIdCliente(Long idCliente){this.idCliente = idCliente;}
    public BigDecimal getSaldo(){return saldo;}
    public void setSaldo(BigDecimal saldo){this.saldo = saldo;}
    public TipoConta getTipoConta(){return tipoConta;}
    public void setTipoConta(TipoConta tipoConta){this.tipoConta = tipoConta;}
    public BigDecimal getRendimento(){return rendimento;}
    public void setRendimento(BigDecimal rendimento){this.rendimento = rendimento;}
    public BigDecimal getManutencao(){return manutencao;}
    public void setManutencao(BigDecimal manutencao){this.manutencao = manutencao;}
}
