package com.dedykuncoro.kuncorocrud.data;

/**
 * Created by Kuncoro on 26/03/2016.
 */
public class Data {
    private String id, nama, alamat, nohp;

    public Data() {
    }

    public Data(String id, String nama, String alamat, String nohp) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.nohp = nohp;
    }

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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }
}
