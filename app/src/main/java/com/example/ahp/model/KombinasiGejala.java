package com.example.ahp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KombinasiGejala implements Parcelable {
    private String gejala1;
    private String gejala2;
    private double bobot;

    public KombinasiGejala() {
    }

    public KombinasiGejala(String gejala1, String gejala2, double bobot) {
        this.gejala1 = gejala1;
        this.gejala2 = gejala2;
        this.bobot = bobot;
    }

    protected KombinasiGejala(Parcel in) {
        gejala1 = in.readString();
        gejala2 = in.readString();
        bobot = in.readDouble();
    }

    public String getGejala1() {
        return gejala1;
    }

    public void setGejala1(String gejala1) {
        this.gejala1 = gejala1;
    }

    public String getGejala2() {
        return gejala2;
    }

    public void setGejala2(String gejala2) {
        this.gejala2 = gejala2;
    }

    public double getBobot() {
        return bobot;
    }

    public void setBobot(double bobot) {
        this.bobot = bobot;
    }

    public static final Creator<KombinasiGejala> CREATOR = new Creator<KombinasiGejala>() {
        @Override
        public KombinasiGejala createFromParcel(Parcel in) {
            return new KombinasiGejala(in);
        }

        @Override
        public KombinasiGejala[] newArray(int size) {
            return new KombinasiGejala[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gejala1);
        dest.writeString(gejala2);
        dest.writeDouble(bobot);
    }
}
