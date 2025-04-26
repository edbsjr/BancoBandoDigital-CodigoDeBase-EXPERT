package br.com.cdb.BandoDigitalFinal2.dto;

import java.math.BigDecimal;

public class TransferenciaBancariaDto {

	
    private Long idOrigem;
    private Long idDestino;
    private BigDecimal valor;

 
 
    public TransferenciaBancariaDto() {
    }

    public TransferenciaBancariaDto(Long idOrigem, Long idDestino, BigDecimal valor) {
    
    	this.idOrigem = idOrigem;
    	this.idDestino = idDestino;
    	this.valor = valor;
    }

	public Long getIdOrigem() {
		return idOrigem;
	}

	public void setIdOrigem(Long idOrigem) {
		this.idOrigem = idOrigem;
	}

	public Long getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Long idDestino) {
		this.idDestino = idDestino;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

   
}
