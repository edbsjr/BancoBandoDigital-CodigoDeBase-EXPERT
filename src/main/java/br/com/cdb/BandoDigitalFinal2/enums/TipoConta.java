package br.com.cdb.BandoDigitalFinal2.enums;

public enum TipoConta {
    CORRENTE("Corrente"),
    POUPANCA("Poupanca");

    private final String descricao;

    TipoConta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
