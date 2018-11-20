package com.yandi.yarud.yadiupi.absensi.model;

public class ModelPenugasan {
    String idMK ;
    String namaMK;
    String sks;
    String namaKelas;
    String semester;
    String tahunajar;
    String namaPST;
    String jenjang;
    String namadsn;
    String namadsn2;
    String namadsn3;

    public ModelPenugasan(String idMK, String namaMK, String sks, String namaKelas, String semester, String tahunajar, String namaPST, String namadsn){

    }

    public ModelPenugasan(){

    }

    public void setIdMK(String idMK){
        this.idMK = idMK;
    }
    public void setNamaMK(String namaMK){
        this.namaMK = namaMK;
    }
    public void setSks(String sks){
        this.sks = sks;
    }
    public void setNamaKelas(String namaKelas){
        this.namaKelas = namaKelas;
    }
    public void setSemester(String semester){this.semester = semester;}
    public void setTahunajar(String tahunajar){this.tahunajar = tahunajar;}
    public void setNamaPST(String namaPST){
        this.namaPST = namaPST;
    }
    public void setJenjang(String jenjang){this.jenjang = jenjang;}
    public void setNamadsn2(String namadsn2){this.namadsn2=namadsn2;}
    public void setNamadsn3(String namadsn3) {this.namadsn3=namadsn3;}
    public void setNamadsn (String namadsn) {this.namadsn=namadsn;}

    public String getIdMK(){
        return idMK;
    }
    public String getNamaMK(){
        return namaMK;
    }
    public String getSks(){
        return sks;
    }
    public String getNamaKelas(){
        return namaKelas;
    }
    public String getSemester() {return semester;}
    public String getTahunajar() {return tahunajar;}
    public String getNamaPST() {
        return namaPST;
    }
    public String getJenjang(){return jenjang;}
    public String getNamadsn2(){return namadsn2;}
    public String getNamadsn3(){return namadsn3;}
    public String getNamadsn(){return namadsn;}
}
