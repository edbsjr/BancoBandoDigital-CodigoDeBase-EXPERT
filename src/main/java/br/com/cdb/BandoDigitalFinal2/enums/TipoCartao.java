package br.com.cdb.BandoDigitalFinal2.enums;

public enum TipoCartao {

    DEBITO("Debito"),
    CREDITO("Credito");

    private final String descricao;

    TipoCartao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
