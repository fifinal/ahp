package com.example.ahp.model;

public class Pakar {
    String nama,email, alamat, no_hp, foto_profile;

    public Pakar(String nama, String email, String alamat, String no_hp, String foto_profile) {
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        this.no_hp = no_hp;
        this.foto_profile = foto_profile;
    }

    public Pakar() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getFoto_profile() {
        return foto_profile;
    }

    public void setFoto_profile(String foto_profile) {
        this.foto_profile = foto_profile;
    }
}
