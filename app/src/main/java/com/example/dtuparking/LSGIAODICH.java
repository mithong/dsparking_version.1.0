package com.example.dtuparking;

import java.util.Date;

public class LSGIAODICH {
    private int resourceID;
    private String thongbao;
    private Date ngay;
    private String tien;
    private String magd;
    private String id;
    private String mau;


    public LSGIAODICH(int resourceID, String thongbao, Date ngay, String tien, String magd, String id, String mau) {
        this.resourceID = resourceID;
        this.thongbao = thongbao;
        this.ngay = ngay;
        this.tien = tien;
        this.magd = magd;
        this.id = id;
        this.mau = mau;
    }

    public String getMau() {
        return mau;
    }

    public void setMau(String mau) {
        this.mau = mau;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getThongbao() {
        return thongbao;
    }

    public void setThongbao(String thongbao) {
        this.thongbao = thongbao;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public String getTien() {
        return tien;
    }

    public void setTien(String tien) {
        this.tien = tien;
    }

    public String getMagd() {
        return magd;
    }

    public void setMagd(String magd) {
        this.magd = magd;
    }
}
