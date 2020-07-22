package com.example.ahp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ahp.metode.Fuzzy;

public class KombinasiPenyakitGejala implements Parcelable {

    private String penyakit;
    private Gejala gejala;
    private Fuzzy bobot;

    public KombinasiPenyakitGejala() {
    }

    public KombinasiPenyakitGejala(String penyakit, Gejala gejala, Fuzzy bobot) {
        this.penyakit = penyakit;
        this.gejala = gejala;
        this.bobot = bobot;
    }

    public String getPenyakit() {
        return penyakit;
    }

    public void setPenyakit(String penyakit) {
        this.penyakit = penyakit;
    }

    public Gejala getGejala() {
        return gejala;
    }

    public void setGejala(Gejala gejala) {
        this.gejala = gejala;
    }

    public Fuzzy getBobot() {
        return bobot;
    }

    public void setBobot(Fuzzy bobot) {
        this.bobot = bobot;
    }

    protected KombinasiPenyakitGejala(Parcel in) {
        penyakit = in.readString();
        gejala = in.readParcelable(Gejala.class.getClassLoader());
    }

    public static final Creator<KombinasiPenyakitGejala> CREATOR = new Creator<KombinasiPenyakitGejala>() {
        @Override
        public KombinasiPenyakitGejala createFromParcel(Parcel in) {
            return new KombinasiPenyakitGejala(in);
        }

        @Override
        public KombinasiPenyakitGejala[] newArray(int size) {
            return new KombinasiPenyakitGejala[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(penyakit);
        dest.writeParcelable(gejala, flags);
    }
}
