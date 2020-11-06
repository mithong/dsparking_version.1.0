package com.example.dtuparking;

public class NapTienBaoVe {
    String DateSend;
    String IdStudent;
    String PayMoney;
    String IdPay;
    String Method;
    String IdSender;


    public NapTienBaoVe() {
    }

    public NapTienBaoVe(String dateSend, String idStudent, String payMoney, String idPay, String method, String idSender) {
        DateSend = dateSend;
        IdStudent = idStudent;
        PayMoney = payMoney;
        IdPay = idPay;
        Method = method;
        IdSender = idSender;
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
