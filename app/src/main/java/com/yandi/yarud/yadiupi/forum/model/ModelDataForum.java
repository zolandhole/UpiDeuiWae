package com.yandi.yarud.yadiupi.forum.model;

public class ModelDataForum {
    private String id_frm,id_pn,user_id,judul,isi,induk,waktu,nama;

    public ModelDataForum() {}

    public ModelDataForum(String id_frm, String id_pn, String user_id, String judul, String isi, String induk, String waktu, String nama) {
        this.id_frm = id_frm;
        this.id_pn = id_pn;
        this.user_id = user_id;
        this.judul = judul;
        this.isi = isi;
        this.induk = induk;
        this.waktu = waktu;
        this.nama = nama;
    }

    public String getId_frm() {return id_frm;}
    public String getId_pn() {return id_pn;}
    public String getUser_id() {return user_id;}
    public String getJudul() {return judul;}
    public String getIsi() {return isi;}
    public String getInduk() {return induk;}
    public String getWaktu() {return waktu;}
    public String getNama() {return nama;}

    public void setId_frm(String id_frm) {this.id_frm = id_frm;}
    public void setId_pn(String id_pn) {this.id_pn = id_pn;}
    public void setUser_id(String user_id) {this.user_id = user_id;}
    public void setJudul(String judul) {this.judul = judul;}
    public void setIsi(String isi) {this.isi = isi;}
    public void setInduk(String induk) {this.induk = induk;}
    public void setWaktu(String waktu) {this.waktu = waktu;}
    public void setNama(String nama) {this.nama = nama;}
}
