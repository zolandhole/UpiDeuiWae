package com.yandi.yarud.yadiupi.absensi.model;

public class ModelPresensi {
    private String nim;
    private String nama;
    private String status;
    private String keterangan;
    private String idrs;

    public ModelPresensi(){}

    public void setNim(String nim){
        this.nim = nim;
    }
    public void setNama(String nama){this.nama = nama;}
    public void setStatus(String status){
        this.status = status;
    }
    public void setKeterangan(String keterangan){
        this.keterangan = keterangan;
    }
    public void setIdrs(String idrs){this.idrs = idrs;}

    public String getNim(){
        return nim;
    }
    public String getNama() {return nama; }
    public String getStatus(){
        return status;
    }
    public String getKeterangan(){
        return keterangan;
    }
    public String getIdrs(){return idrs;}
}
