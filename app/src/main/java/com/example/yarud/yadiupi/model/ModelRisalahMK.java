package com.example.yarud.yadiupi.model;

public class ModelRisalahMK {
    String idrs;
    String idpn;
    String pertemuan;
    String topik;
    String subtopik;
    String sesuai;
    String waktu;
    String userid;
    String approve;

    public ModelRisalahMK(){

    }

    public void setIdrs(String idrs){
        this.idrs = idrs;
    }
    public void setIdpn(String idpn){
        this.idpn = idpn;
    }
    public void setPertemuan(String pertemuan){
        this.pertemuan = pertemuan;
    }
    public void setTopik(String topik){
        this.topik = topik;
    }
    public void setSubtopik(String subtopik){
        this.subtopik = subtopik;
    }
    public void setSesuai(String sesuai){
        this.sesuai = sesuai;
    }
    public void setWaktu(String waktu){
        this.waktu = waktu;
    }
    public void setUserid(String userid){
        this.userid = userid;
    }
    public void setApprove(String approve){
        this.approve = approve;
    }

    public String getIdrs(){
        return idrs;
    }
    public String getIdpn(){
        return idpn;
    }
    public String getPertemuan(){
        return pertemuan;
    }
    public String getTopik(){
        return topik;
    }
    public String getSubtopik(){
        return subtopik;
    }
    public String getSesuai(){ return sesuai; }
    public String getWaktu(){
        return waktu;
    }
    public String getUserid(){
        return userid;
    }
    public String getApprove(){ return approve; }
}
