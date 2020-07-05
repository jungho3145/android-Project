package com.example.myapplication;

public class Todo {
    String todoName;
    String todoContent;
    int categoryid;
    String date;

    public Todo(){
    }

    public Todo(String todoName, String todoContent, int categoryid, String date){
        this.todoName = todoName;
        this.todoContent = todoContent;
        this.categoryid = categoryid;
        this.date = date;

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

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
