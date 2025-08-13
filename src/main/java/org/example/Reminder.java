package org.example;

public class Reminder {
    private String cust; // הלקוח שם
    private String tel; // הלקוח טלפון
    private String inst; // המכון שם
    private String date; // התור תאריך
    private int hour; // התור שעת
    private int status;//-2 ביטול, -1 אישור, -0 אין מענה

    public Reminder(String cust, String tel, String inst, String date, int hour) {
        this.cust = cust;
        this.tel = tel;
        this.inst = inst;
        this.date = date;
        this.hour = hour;
        this.status = 0;
    }

    public String getCust() {
        return cust;
    }

    public void setCust(String cust) {
        this.cust = cust;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "cust='" + cust + '\'' +
                ", tel='" + tel + '\'' +
                ", inst='" + inst + '\'' +
                ", date='" + date + '\'' +
                ", hour=" + hour +
                ", status=" + status +
                '}';
    }
}
