package com.example.dtuparking;

public class NapTienBaoVe {
    String DateSend;
    String IdStudent;
    String PayMoney;
    Boolean IsNoti;
    String IdPay;
    String Method;
    String IdSender;


    public NapTienBaoVe() {
    }

    public NapTienBaoVe(String dateSend, String idStudent, String payMoney,Boolean isNoti, String idPay, String method, String idSender) {
        DateSend = dateSend;
        IdStudent = idStudent;
        PayMoney = payMoney;
        IsNoti = isNoti;
        IdPay = idPay;
        Method = method;
        IdSender = idSender;
    }

    public Boolean getIsNoti() {
        return IsNoti;
    }

    public void setIsNoti(Boolean isNoti) {
        IsNoti = isNoti;
    }

    public String getDateSend() {
        return DateSend;
    }

    public void setDateSend(String dateSend) {
        DateSend = dateSend;
    }

    public String getIdStudent() {
        return IdStudent;
    }

    public void setIdStudent(String idStudent) {
        IdStudent = idStudent;
    }

    public String getPayMoney() {
        return PayMoney;
    }

    public void setPayMoney(String payMoney) {
        PayMoney = payMoney;
    }

    public String getIdPay() {
        return IdPay;
    }

    public void setIdPay(String idPay) {
        IdPay = idPay;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getIdSender() {
        return IdSender;
    }

    public void setIdSender(String idSender) {
        IdSender = idSender;
    }
}
