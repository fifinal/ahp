package com.example.ahp.model;

public class HasilDiagnosa {
    String kodePenyakit,namaPenyakit;
    double nilai;

    public HasilDiagnosa(String kodePenyakit, String namaPenyakit, double nilai) {
        this.kodePenyakit = kodePenyakit;
        this.namaPenyakit = namaPenyakit;
        this.nilai = nilai;
    }

    public String getKodePenyakit() {
        return kodePenyakit;
    }

    public void setKodePenyakit(String kodePenyakit) {
        this.kodePenyakit = kodePenyakit;
    }

    public String getNamaPenyakit() {
        return namaPenyakit;
    }

    public void setNamaPenyakit(String namaPenyakit) {
        this.namaPenyakit = namaPenyakit;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }
}
