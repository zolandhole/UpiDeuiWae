package com.yandi.yarud.yadiupi.forum.model;

public class ModelForumMhs {
    private String IDMK, KODEMK, NAMAMK, SKS, KM, NEEDAPPROVE;

    public ModelForumMhs() {}

    public ModelForumMhs(String IDMK, String KODEMK, String NAMAMK, String SKS, String NIM, String KM, String NEEDAPPROVE) {
        this.IDMK = IDMK;
        this.KODEMK = KODEMK;
        this.NAMAMK = NAMAMK;
        this.SKS = SKS;
        this.KM = KM;
        this.NEEDAPPROVE = NEEDAPPROVE;
    }

    public String getIDMK() {
        return IDMK;
    }

    public void setIDMK(String IDMK) {
        this.IDMK = IDMK;
    }

    public String getKODEMK() {
        return KODEMK;
    }

    public void setKODEMK(String KODEMK) {
        this.KODEMK = KODEMK;
    }

    public String getNAMAMK() {
        return NAMAMK;
    }

    public void setNAMAMK(String NAMAMK) {
        this.NAMAMK = NAMAMK;
    }

    public String getSKS() {
        return SKS;
    }

    public void setSKS(String SKS) {
        this.SKS = SKS;
    }

    public String getKM() {
        return KM;
    }

    public void setKM(String KM) {
        this.KM = KM;
    }

    public String getNEEDAPPROVE() {
        return NEEDAPPROVE;
    }

    public void setNEEDAPPROVE(String NEEDAPPROVE) {
        this.NEEDAPPROVE = NEEDAPPROVE;
    }
}
