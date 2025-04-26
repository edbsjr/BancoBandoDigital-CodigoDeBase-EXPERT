package br.com.cdb.BandoDigitalFinal2.dto;

import java.math.BigDecimal;

public class NumeroValorDto {
	
	private Long numero;
	private BigDecimal valor;
	
	
	public NumeroValorDto() {
		super();
	}
	public NumeroValorDto(Long numero, BigDecimal valor) {
		super();
		this.numero = numero;
		this.valor = valor;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	
}
