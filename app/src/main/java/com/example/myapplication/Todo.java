package com.example.myapplication;

import java.util.Date;

public class Todo {
    String todoName;
    String todoContent;
    Categori categori;
    int date;
    int year;
    int month;

    public Todo(){
    }

    public Todo(String todoName, String todoContent, Categori categori, int year, int month, int date){
        this.todoName = todoName;
        this.todoContent = todoContent;
        this.categori = categori;
        this.date = date;
        this.year = year;
        this.month = month;
    }

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }

    public String getTodoContent() {
        return todoContent;
    }

    public void setTodoContent(String todoContent) {
        this.todoContent = todoContent;
    }

    public Categori getCategori() {
        return categori;
    }

    public void setCategori(Categori categori) {
        this.categori = categori;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
