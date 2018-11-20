package com.yandi.yarud.yadiupi.forum.model;

public class ModelForum {
    private String idMK,namaMK,namaPST;

    public ModelForum(String idMK, String namaMK, String namaPST){}

    public ModelForum(){}

    public void setIdMK(String idMK){
        this.idMK = idMK;
    }
    public void setNamaMK(String namaMK){
        this.namaMK = namaMK;
    }
    public void setNamaPST(String namaPST){
        this.namaPST = namaPST;
    }

    public String getIdMK(){
        return idMK;
    }
    public String getNamaMK(){
        return namaMK;
    }
    public String getNamaPST() {
        return namaPST;
    }
}
