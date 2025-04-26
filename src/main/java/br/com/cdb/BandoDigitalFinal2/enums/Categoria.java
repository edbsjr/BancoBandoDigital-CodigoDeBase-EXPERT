package br.com.cdb.BandoDigitalFinal2.enums;

public enum Categoria {

	COMUM("Comum"),
	SUPER("Super"),
	PREMIUM("Premium");
	
	private final String descricao;
	
	Categoria(String descricao) 
	{
		this.descricao = descricao;
	}
	
	public String getDescricao() 
	{
		return descricao;
	}
}
