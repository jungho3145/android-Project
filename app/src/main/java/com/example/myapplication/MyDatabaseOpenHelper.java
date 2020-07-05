package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {

    public static int TODOS_ID = 1;
    public static int CATEGORIES_ID = 1;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydb.db";

    public static final String TODOS_TABLE_NAME = "todos";
    public static final String CATEGORIES_TABLE_NAEM = "categories";


    public MyDatabaseOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_make_todos_table = "CREATE TABLE " + TODOS_TABLE_NAME + "(id INTEGER PRIMARY KEY, todoname TEXT, todocontent TEXT, categoriesid INTEGER, date TEXT);";
        String sql_make_categories_table = "CREATE TABLE " + CATEGORIES_TABLE_NAEM + "(categoriesid INTEGER PRIMARY KEY, categoryname TEXT, colorcode TEXT);";

        sqLiteDatabase.execSQL(sql_make_todos_table);
        sqLiteDatabase.execSQL(sql_make_categories_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql="drop table if exists todos";
        String sql2 = "drop table if exists categories";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql2);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if(!db.isReadOnly()){
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    public boolean insertCategories(int key, String name, String color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("categoriesid", key);
        contentValues.put("categoryname", name);
        contentValues.put("colorcode", color);
        long result = db.insert(CATEGORIES_TABLE_NAEM, null, contentValues);
        CATEGORIES_ID++;

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public String getCategoriesResult() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM categories", null);
        while (cursor.moveToNext()){
            result += cursor.getString(0) + ":" + cursor.getString(1) + ":" + cursor.getString(2) + "\n";
        }

        return result;
    }

    public boolean insertTodos(int Key, String name, String content, String categoriesid, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Key);
        contentValues.put("todoname", name);
        contentValues.put("todocontent", content);
        contentValues.put("categoriesid", categoriesid);
        contentValues.put("date", date);
        long result = db.insert(TODOS_TABLE_NAME, null, contentValues);
        TODOS_ID++;
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public String getTodosResultAll() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM todos", null);
        while (cursor.moveToNext()){
            result += cursor.getString(0) + ":"
                    + cursor.getString(1) + ":"
                    + cursor.getString(2) + ":"
                    + cursor.getString(3) + ":"
                    + cursor.getString(4) + "\n";
        }

        return result;
    }

    public String getTOdosResultDate() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT date FROM todos", null);
        while (cursor.moveToNext()){
            result += cursor.getString(0) + "\n";
        }

        return result;
    }

    public String getTodosResultById(int id){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT * FROM todos WHERE id = " + id, null);
        while (cursor.moveToNext()){
            result += cursor.getString(0) + ":"
                    + cursor.getString(1) + ":"
                    + cursor.getString(2) + ":"
                    + cursor.getString(3) + ":"
                    + cursor.getString(4);
        }

        return result;
    }

    public String getCategoriesResultById(int id){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT * FROM categories WHERE categoriesid = " + id, null);
        while (cursor.moveToNext()){
            result += cursor.getString(0) + ":"
                    + cursor.getString(1) + ":"
                    + cursor.getString(2);
        }

        return result;
    }

    public void updateTodosData(String id, String name, String content, String categoriesid, String date){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("todoname", name);
        contentValues.put("todocontent", content);
        contentValues.put("categoriesid", categoriesid);
        contentValues.put("date", date);
        db.update(TODOS_TABLE_NAME, contentValues, "ID = ?", new String[] {id});
    }

    public void deleteTodos(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM todos WHERE id = " + id);
        this.reLabling();
    }

    public void reLabling(){
        SQLiteDatabase db = getWritableDatabase();
        String[] todos = this.getTodosResultAll().split("\n");
        String[] categories = this.getCategoriesResult().split("\n");


        db.execSQL("DELETE FROM " + TODOS_TABLE_NAME);
        db.execSQL("DELETE FROM " + CATEGORIES_TABLE_NAEM);

        int i = 1;
        int j = 1;
        for(String s: todos ){
            String[] strings = s.split(":");
            this.insertTodos(i, strings[1], strings[2], strings[3], strings[4]);
            i++;
        }
        for(String s: categories){
            String[] strings = s.split(":");
            this.insertCategories(j, strings[1], strings[2]);
            j++;
        }

        this.TODOS_ID = i;
        this.CATEGORIES_ID = j;

    }

    public boolean CheckingRightColor(String s){
        if(s.length() == 6){
            return false;
        }
        return true;
    }



}
