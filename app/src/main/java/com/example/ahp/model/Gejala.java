package com.example.ahp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Gejala implements Parcelable {
    String id, nama;

    protected Gejala(Parcel in) {
        id = in.readString();
        nama = in.readString();
    }

    public static final Creator<Gejala> CREATOR = new Creator<Gejala>() {
        @Override
        public Gejala createFromParcel(Parcel in) {
            return new Gejala(in);
        }

        @Override
        public Gejala[] newArray(int size) {
            return new Gejala[size];
        }
    };

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

    public Gejala() {
    }

    public Gejala(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama);
    }
}
