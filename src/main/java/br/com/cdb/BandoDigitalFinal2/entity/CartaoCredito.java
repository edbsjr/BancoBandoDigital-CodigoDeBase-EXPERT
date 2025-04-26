package br.com.cdb.BandoDigitalFinal2.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;

@Entity
public class CartaoCredito extends Cartao{

	private BigDecimal fatura= new BigDecimal("0.0");
	
	private BigDecimal limite;
	
	private BigDecimal limiteEmUso;
	
	public CartaoCredito() {}
	
	public CartaoCredito(BigDecimal fatura) {
		super();
		this.fatura = fatura;
	}
	public BigDecimal getFatura() {
		return fatura;
	}

	public void setFatura(BigDecimal fatura) {
		this.fatura = fatura;
	}
	
	
	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}
	
	public BigDecimal getLimiteEmUso() {
		return limiteEmUso;
	}

	public void setLimiteEmUso(BigDecimal limiteEmUso) {
		this.limiteEmUso = limiteEmUso;
	}

	@Override
	public void pagar(BigDecimal valor) {
		
		if(getLimite().subtract(getLimiteEmUso()).compareTo(valor)>=0)
		{
			setFatura(getFatura().add(valor));
			setLimiteEmUso(getLimiteEmUso().add(valor));
		}
	}

	
	
}
