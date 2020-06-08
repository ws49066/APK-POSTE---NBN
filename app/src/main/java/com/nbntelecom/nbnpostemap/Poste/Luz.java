package com.nbntelecom.nbnpostemap.Poste;

public class Luz {
    private String tipoLuz,proprietatio,tipoPropri,tipoLampada,potencia;

    public Luz(String tipoLuz, String proprietatio, String tipoPropri, String tipoLampada, String potencia) {
        this.tipoLuz = tipoLuz;
        this.proprietatio = proprietatio;
        this.tipoPropri = tipoPropri;
        this.tipoLampada = tipoLampada;
        this.potencia = potencia;
    }

    public String getTipoLuz() {
        return tipoLuz;
    }

    public String getProprietatio() {
        return proprietatio;
    }

    public String getTipoPropri() {
        return tipoPropri;
    }

    public String getTipoLampada() {
        return tipoLampada;
    }

    public String getPotencia() {
        return potencia;
    }
}
