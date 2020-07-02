package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ArrayList<Categori> categoris = new ArrayList<>();
    ArrayList<String> categorisSpinner = new ArrayList<>();

    ArrayList<todo> todos = new ArrayList<>();


    Button add, delete, categoriAdd;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        categoriAdd = findViewById(R.id.categoriAdd);
        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        listView = findViewById(R.id.todoList);

        categoriAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this); //다이얼로그 생성
                LayoutInflater inflater = getLayoutInflater();//인플레이터 생성
                final View dialogView = inflater.inflate(R.layout.dialog_categori_add, null); //인플레이터로 다이얼로그에 띄울 VIEW객체 생성
                final EditText ed = dialogView.findViewById(R.id.dlgNameEd); // 위 객체 안에 있는 위젯 객체들 연결
                final EditText ed2 = dialogView.findViewById(R.id.dlgColorEd);

                dlg.setTitle("카테고리 추가");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Categori newCategori = new Categori();// 새로운 카테고리 생성
                        newCategori.setCategori(ed.getText().toString());
                        newCategori.setColor(ed2.getText().toString());

                        categoris.add(newCategori); // 카테고리 목록에 추가
                        categorisSpinner.add(newCategori.categori); // 스피너를 위한 카테고리 이름 목록에 추가
                        Toast.makeText(MainActivity.this, categoris.get(categoris.size() - 1).categori + "가 추가되었습니다 색상 코드: " +
                                categoris.get(categoris.size() - 1).red + ", " + categoris.get(categoris.size() - 1).green + ", " + categoris.get(categoris.size() - 1).blue, Toast.LENGTH_SHORT).show();
                   }
                }).setNegativeButton("취소", null).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this); // 다이얼로그 생성
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_todo_add, null); // 인플레이터로 VIEW객체 생성
                final EditText ed = dialogView.findViewById(R.id.todoName); // 위 객체 안의 위젯 객체들과 연결
                final EditText ed2 = dialogView.findViewById(R.id.todoContent);
                final Spinner spinner = dialogView.findViewById(R.id.itemPicker);
                final DatePicker datePicker = dialogView.findViewById(R.id.datePicker);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, categorisSpinner); // 아답터 생
                spinner.setAdapter(arrayAdapter);// 아답터 연결

                dlg.setTitle("할일 추가")
                        .setView(dialogView)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                todo newTodo = new todo();

                                newTodo.setTodoName(ed.getText().toString());
                                newTodo.setTodoContent(ed2.getText().toString());
                                newTodo.setYear(datePicker.getYear());
                                newTodo.setMonth(datePicker.getMonth());
                                newTodo.setDate(datePicker.getDayOfMonth());
                                newTodo.setCategori(categoris.get(spinner.getSelectedItemPosition()));

                                todos.add(newTodo);

                                Toast.makeText(MainActivity.this, todos.get(todos.size() - 1).categori.categori, Toast.LENGTH_SHORT).show();

                            }
                        }).setNegativeButton("취소", null).show();

            }
        });








    }
}
