package br.com.cdb.BandoDigitalFinal2.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;

@Entity
public class CartaoDebito extends Cartao {

	private BigDecimal limiteDiario;
	private BigDecimal limiteEmUso;
	
	public CartaoDebito() {
	}

	public CartaoDebito(BigDecimal limiteDiario) {
		super();
		this.limiteDiario = limiteDiario;
	}

	public BigDecimal getLimiteDiario() {
		return limiteDiario;
	}

	public void setLimiteDiario(BigDecimal limiteDiario) {
		this.limiteDiario = limiteDiario;
	}

	public BigDecimal getLimiteEmUso() {
		return limiteEmUso;
	}

	public void setLimiteEmUso(BigDecimal limiteEmUso) {
		this.limiteEmUso = limiteEmUso;
	}

	@Override
	public void pagar(BigDecimal valor) {
		if(getLimiteDiario().subtract(getLimiteEmUso()).compareTo(valor)>=0)
			setLimiteEmUso(getLimiteEmUso().add(valor));
	}
}
