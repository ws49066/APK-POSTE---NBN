package com.nbntelecom.nbnpostemap.Poste;

public class CruzetaItems {
    private String TipodeCruzeta,AereaTipo,RedeMedia;

    public CruzetaItems(String tipodeCruzeta, String aereaTipo, String redeMedia) {
        TipodeCruzeta = tipodeCruzeta;
        AereaTipo = aereaTipo;
        RedeMedia = redeMedia;
    }

    public String getAereaTipo() {
        return AereaTipo;
    }

    public String getRedeMedia() {
        return RedeMedia;
    }

    public String getTipodeCruzeta() {
        return TipodeCruzeta;
    }
}
