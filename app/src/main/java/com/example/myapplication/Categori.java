package com.example.myapplication;

public class Categori {
    String categoriName;
    String color;
    int red;
    int green;
    int blue;

    public String getCategori() {
        return categoriName;
    }

    public void setCategori(String categori) {
        this.categoriName = categori;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        red = Integer.parseInt(this.color.substring(0,2), 16);
        green = Integer.parseInt(this.color.substring(2,4), 16);
        blue = Integer.parseInt(this.color.substring(4,6), 16);
    }
}
