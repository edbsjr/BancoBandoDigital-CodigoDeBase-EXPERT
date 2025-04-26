package br.com.cdb.BandoDigitalFinal2.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;

@Entity
public class ContaPoupanca extends Conta {

	private BigDecimal rendimento;
	
	public ContaPoupanca (){
		
	}
	public ContaPoupanca(Cliente cliente, BigDecimal rendimento){
		
		super ();//TODO CONFIRMAR SE VAI DAR CERTO ESSE METODO CONSTRUTOR
		this.rendimento = rendimento;
		
	}
	
	public BigDecimal getRendimento() {
		return rendimento;
	}
	public void setRendimento(BigDecimal rendimento) {
		this.rendimento = rendimento;
	}
	@Override
	public void debitar(BigDecimal valor) {
		
		if(valor.compareTo(getSaldo())<=0)
			setSaldo(getSaldo().subtract(valor));
		else
			throw new RuntimeException("Saldo insuficiente");
		
	}

	@Override
	public void creditar(BigDecimal valor) {
		setSaldo(getSaldo().add(valor));
	}

	
	
}
