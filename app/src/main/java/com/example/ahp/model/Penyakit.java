package com.example.ahp.model;

public class Penyakit {

    String id;
    String nama, info, solusi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSolusi() {
        return solusi;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    public Penyakit(String id, String nama, String info, String solusi) {
        this.id = id;
        this.nama = nama;
        this.info = info;
        this.solusi = solusi;
    }

    public Penyakit() {
    }
}
