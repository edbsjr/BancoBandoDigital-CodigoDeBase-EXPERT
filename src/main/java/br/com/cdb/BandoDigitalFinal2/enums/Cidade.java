package br.com.cdb.BandoDigitalFinal2.enums;


public enum Cidade {

	MURIAE("MG", "Muriae"),
	MIRAI("MG", "Mirai"),
	JUIZ_DE_FORA("MG", "Juiz de Fora"),
	LEOPOLDINA("MG", "Leopoldina"),
	VICOSA("MG","Vicosa"),
	ITAPERUNA("RJ", "Itaperuna"),
	RIO_DE_JANEIRO("RJ", "Rio de Janeiro"),
	CABO_FRIO("RJ", "Cabo Frio"),
	BUZIOS("RJ", "Buzios"),
	NITEROI("RJ", "Niteroi");

	private final String estado;
	private final String cidade;
	
	public String getCidade() {
		return cidade;
	}

	public String getEstado() {
		return estado;
	}

	Cidade(String estado, String cidade) {

		this.estado = estado;
		this.cidade = cidade;
	}
	public static Cidade buscarCidade(String cidadeDigitada) {
		
		for (Cidade cidadeRetorno: Cidade.values())
		{
			if(cidadeRetorno.getCidade().equalsIgnoreCase(cidadeDigitada))
				return cidadeRetorno;
		
		}
		return null;
		
	}
	
}