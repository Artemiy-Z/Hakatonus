package com.example.hakatonus.model;

public class Vodila {

    int id;
    String img,title,txt_data,txt_time;

    public Vodila(int id, String img, String title, String txt_data, String txt_time){
        this.id = id;
        this.img = img;
        this.title = title;
        this.txt_data = txt_data;
        this.txt_time = txt_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTxt_data(String txt_data) {
        this.txt_data = txt_data;
    }

    public void setTxt_time(String txt_time) {
        this.txt_time = txt_time;
    }

    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getTxt_data() {
        return txt_data;
    }

    public String getTxt_time() {
        return txt_time;
    }
}
