package com.yandi.yarud.yadiupi.absensi.model;

public class ModelRisalahMK {
    private String idrs;
    private String idpn;
    private String pertemuan;
    private String topik;
    private String subtopik;
    private String sesuai;
    private String waktu;
    private String userid;
    private String approve;
    private String tgl;
    private String finish;
    private String tgl_finish;

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
    public void setTgl(String tgl){ this.tgl = tgl; }
    public void setFinish(String finish){ this.finish = finish; }
    public void setTgl_finish(String tgl_finish){ this.tgl_finish = tgl_finish; }

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
    public String getTgl() { return tgl; }
    public String getFinish() { return finish; }
    public String getTgl_finish() { return tgl_finish; }
}
