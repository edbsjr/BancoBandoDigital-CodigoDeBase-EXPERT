package br.com.cdb.BandoDigitalFinal2.enums;

public enum Estado {

	AC("Acre"), AL("Alagoas"), AP("Amapá"), AM("Amazonas"), BA("Bahia"), CE("Ceará"), DF("Distrito Federal"),
	ES("Espírito Santo"), GO("Goiás"), MA("Maranhão"), MT("Mato Grosso"), MS("Mato Grosso do Sul"), MG("Minas Gerais"),
	PA("Pará"), PB("Paraíba"), PR("Paraná"), PE("Pernambuco"), PI("Piauí"), RJ("Rio de Janeiro"),
	RN("Rio Grande do Norte"), RS("Rio Grande do Sul"), RO("Rondônia"), RR("Roraima"), SC("Santa Catarina"),
	SP("São Paulo"), SE("Sergipe"), TO("Tocantins");

	private final String estado;

	Estado(String estado) {

		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	public static Estado getDescricao(String descricao) {
        for (Estado estado : Estado.values()) {
            if (estado.estado.equalsIgnoreCase(descricao)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Descrição de Estado inválida: " + descricao);
        
    }
	
	

}