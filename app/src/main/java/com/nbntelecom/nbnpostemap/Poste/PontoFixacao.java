package com.nbntelecom.nbnpostemap.Poste;

public class PontoFixacao {
    private String ponto,tipoPonto,subtipo;

    public PontoFixacao(String ponto, String tipo, String subtipo) {
        this.ponto = ponto;
        this.tipoPonto = tipo;
        this.subtipo = subtipo;
    }

    public String getPonto() {
        return ponto;
    }

    public String getSubtipo() {
        return subtipo;
    }

    public String getTipoPonto() {
        return tipoPonto;
    }
}
