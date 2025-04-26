package br.com.cdb.BandoDigitalFinal2.enums;

public enum Situacao {

	ATIVADO("AT", "Ativado"),
	BLOQUEADO("BL", "Bloqueado"),
	CANCELADO("CN","Cancelado");

	private final String sigla;
	private final String descricao;
	
	
	Situacao(String sigla, String descricao) {
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