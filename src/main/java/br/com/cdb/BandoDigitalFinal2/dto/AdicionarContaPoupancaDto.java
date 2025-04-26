package br.com.cdb.BandoDigitalFinal2.dto;

import java.math.BigDecimal;

public class AdicionarContaPoupancaDto {

	private Long clienteId;
	private BigDecimal rendimento;
	
	
	
	public AdicionarContaPoupancaDto(Long clienteId, BigDecimal rendimento) {
		this.clienteId = clienteId;
		this.rendimento = rendimento;
	}
	public AdicionarContaPoupancaDto() {
		
	}
	public Long getClienteId() {
		return clienteId;
	}
	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}
	public BigDecimal getRendimento() {
		return rendimento;
	}
	public void setTaxa(BigDecimal rendimento) {
		this.rendimento = rendimento;
	}
}
