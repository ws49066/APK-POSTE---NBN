package com.nbntelecom.nbnpostemap.model;



public class detalhes_consulta {
    private String Id;
    private String id_poste;

    private  String Nama;

    public detalhes_consulta(){

    }

    public detalhes_consulta(String id, String  nama){
        this.Id = id;
        this.Nama = nama;
    }

    public String getId(){
        return Id;
    }

    public String getNama(){
        return  Nama;
    }
}
