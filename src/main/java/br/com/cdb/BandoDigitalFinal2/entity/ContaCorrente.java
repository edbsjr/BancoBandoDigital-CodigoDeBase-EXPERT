package br.com.cdb.BandoDigitalFinal2.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ContaCorrente extends Conta{
	
	private BigDecimal taxa;
	
	public ContaCorrente() {}

    public ContaCorrente(Cliente cliente, BigDecimal taxa) {
        super();//TODO CONFIRMAR SE VAI DAR CERTO ESSE METODO CONSTRUTOR
        this.taxa = taxa;
    }

	public BigDecimal getTaxa() {
		return taxa;
	}

	public void setTaxa(BigDecimal taxa) {
		this.taxa = taxa;
	}

	@Override
	public void debitar(BigDecimal valor) {
		
		if(valor.compareTo(getSaldo()) <= 0)
			setSaldo(getSaldo().subtract(valor));
		else
			throw new RuntimeException("Saldo insuficiente"); // TODO VERIFICAR MELHOR COMO TRATAR ESSE ERRO
		
	}

	@Override
	public void creditar(BigDecimal valor) {
		setSaldo(getSaldo().add(valor));
		
	}
	
	
	
	
	
}
