package com.example.hakatonus.model;

public class Driver {

    int id;
    String img,txt_FIO,txt_kyda,txt_otkyda;

    public Driver(int id, String img, String txt_FIO, String txt_kyda, String txt_otkyda){
        this.id = id;
        this.img = img;
        this.txt_FIO = txt_FIO;
        this.txt_kyda = txt_kyda;
        this.txt_otkyda = txt_otkyda;
    }

    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getTxt_FIO() {
        return txt_FIO;
    }

    public String getTxt_kyda() {
        return txt_kyda;
    }

    public String getTxt_otkyda() {
        return txt_otkyda;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTxt_FIO(String txt_FIO) {
        this.txt_FIO = txt_FIO;
    }

    public void setTxt_kyda(String txt_kyda) {
        this.txt_kyda = txt_kyda;
    }

    public void setTxt_otkyda(String txt_otkyda) {
        this.txt_otkyda = txt_otkyda;
    }
}

