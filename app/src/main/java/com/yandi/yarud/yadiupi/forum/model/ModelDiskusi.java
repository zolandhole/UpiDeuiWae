package com.yandi.yarud.yadiupi.forum.model;

public class ModelDiskusi {
    private String id_frm,id_pn,user_id,judul,isi,indu,waktu,nama;

    public ModelDiskusi() {}

    public ModelDiskusi(String id_frm, String id_pn, String user_id, String judul, String isi, String indu, String waktu, String nama) {
        this.id_frm = id_frm;
        this.id_pn = id_pn;
        this.user_id = user_id;
        this.judul = judul;
        this.isi = isi;
        this.indu = indu;
        this.waktu = waktu;
        this.nama = nama;
    }

    public String getId_frm() {
        return id_frm;
    }

    public void setId_frm(String id_frm) {
        this.id_frm = id_frm;
    }

    public String getId_pn() {
        return id_pn;
    }

    public void setId_pn(String id_pn) {
        this.id_pn = id_pn;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getIndu() {
        return indu;
    }

    public void setIndu(String indu) {
        this.indu = indu;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
