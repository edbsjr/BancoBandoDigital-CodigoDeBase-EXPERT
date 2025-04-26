package br.com.cdb.BandoDigitalFinal2.enums;

public enum FormaDePagamento {

	DINHEIRO("DIN", "Dinheiro"), 
	SAQUE("SAQ", "Saque"), 
	CARTAO_DE_CREDITO("CDC", "Cartao de Credito"),
	CARTAO_DE_DEBITO("CDD", "Cartao de Debito"), 
	PIX("PIX", "PIX"), 
	TRANSFERENCIA("TRS", "Transferencia");

	private final String sigla;
	private final String descricao;

	FormaDePagamento(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;

	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

}