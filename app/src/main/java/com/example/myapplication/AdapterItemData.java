package com.example.myapplication;

public class AdapterItemData {
    String categoriName;
    String color;
    String todoName;
    String todoContent;
    String date;
    int red;
    int blue;
    int green;

    public String getCategoriName() {
        return categoriName;
    }

    public void setCategoriName(String categoriName) {
        this.categoriName = categoriName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.red = Integer.parseInt(color.substring(0,2), 16);
        this.blue = Integer.parseInt(color.substring(2,4), 16);
        this.green = Integer.parseInt(color.substring(4,6), 16);
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
