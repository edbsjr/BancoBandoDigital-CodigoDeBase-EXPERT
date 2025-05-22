package br.com.cdb.BandoDigitalFinal2.dto;

import br.com.cdb.BandoDigitalFinal2.enums.TipoConta;

public class ContaTipoDto {

    private Long idCliente;
    private TipoConta tipoConta;

    public ContaTipoDto(TipoConta tipoConta, Long idCliente) {
        this.tipoConta = tipoConta;
        this.idCliente = idCliente;
    }

    public ContaTipoDto() {}
    public Long getIdCliente() {return idCliente;}
    public void setIdCliente(Long idCliente) {this.idCliente = idCliente;}
    public TipoConta getTipoConta() {return tipoConta;}
    public void setTipoConta(TipoConta tipoConta) {this.tipoConta = tipoConta;}
}
