package com.nbntelecom.nbnpostemap.model;

public class Provinsi {
    private String Id;
    private  String Nama;

    public Provinsi(){

    }

    public Provinsi(String id, String  nama){
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
