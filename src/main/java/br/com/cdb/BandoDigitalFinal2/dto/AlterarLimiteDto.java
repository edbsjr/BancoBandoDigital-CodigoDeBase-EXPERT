package br.com.cdb.BandoDigitalFinal2.dto;

import java.math.BigDecimal;

public class AlterarLimiteDto 
{
	private Long idCartao;
	private BigDecimal limite;
	public AlterarLimiteDto(Long idCartao, BigDecimal limite) {
		super();
		this.idCartao = idCartao;
		this.limite = limite;
	}
	public AlterarLimiteDto() {
		super();
	}
	public Long getIdCartao() {
		return idCartao;
	}
	public void setIdCartao(Long idCartao) {
		this.idCartao = idCartao;
	}
	public BigDecimal getLimite() {
		return limite;
	}
	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}
	
}
