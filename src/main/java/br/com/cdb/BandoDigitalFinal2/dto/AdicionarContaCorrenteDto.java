package br.com.cdb.BandoDigitalFinal2.dto;

import java.math.BigDecimal;

public class AdicionarContaCorrenteDto {

	private Long clienteId;
	private BigDecimal taxa;
	
	
	
	public AdicionarContaCorrenteDto(Long clienteId, BigDecimal taxa) {
		this.clienteId = clienteId;
		this.taxa = taxa;
	}
	public AdicionarContaCorrenteDto() {
		
	}
	public Long getClienteId() {
		return clienteId;
	}
	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}
	public BigDecimal getTaxa() {
		return taxa;
	}
	public void setTaxa(BigDecimal taxa) {
		this.taxa = taxa;
	}
	
}
