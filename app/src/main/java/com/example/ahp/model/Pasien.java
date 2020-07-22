package com.example.ahp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pasien implements Parcelable {
    private String id,nama, alamat, gender, no_telp;

    public Pasien(String nama, String alamat, String gender, String no_telp) {
        this.nama = nama;
        this.alamat = alamat;
        this.gender = gender;
        this.no_telp = no_telp;
    }

    public Pasien() {
    }

    protected Pasien(Parcel in) {
        nama = in.readString();
        alamat = in.readString();
        gender = in.readString();
        no_telp = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final Creator<Pasien> CREATOR = new Creator<Pasien>() {
        @Override
        public Pasien createFromParcel(Parcel in) {
            return new Pasien(in);
        }

        @Override
        public Pasien[] newArray(int size) {
            return new Pasien[size];
        }
    };

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(alamat);
        dest.writeString(gender);
        dest.writeString(no_telp);
    }
}
