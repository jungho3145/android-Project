package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    MyDatabaseOpenHelper db;

    ArrayList<String> categorisSpinner = new ArrayList<>(); //스피너에서만 쓰일 어레이 리스트 //코드의 일관성을 위해 다른용도로 사용금지

    Button add, categoriAdd;
    ListView listView;

    String categoriSelected = "전체보기"; // 초기값은 항상 전체보기로 설
    int selectedItem = 0; // 전에 표시했던 카테고리가 어떤 카테고리 였는지 체크하기 위해 쓰는 변수 //코드의 일관성을 위해 다른용도로 사용금
    int selectedDate = 0;

    String date = "전체날짜";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new MyDatabaseOpenHelper(MainActivity.this);

        categoriAdd = findViewById(R.id.categoriAdd); // 위젯 연결
        add = findViewById(R.id.add);
        listView = findViewById(R.id.todoList);

        categorisSpinner = CategoriesSpinnerUpdate();
        makeListItem();



        categoriAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this); //다이얼로그 생성
                LayoutInflater inflater = getLayoutInflater();//인플레이터 생성

                final View dialogView = inflater.inflate(R.layout.dialog_categori_add, null); //인플레이터로 다이얼로그에 띄울 VIEW객체 생성
                final EditText ed = dialogView.findViewById(R.id.dlgNameEd); // 위 객체 안에 있는 위젯 객체들 연결
                final EditText ed2 = dialogView.findViewById(R.id.dlgColorEd);

                dlg.setTitle("카테고리 추가") // 다이얼로그 설정
                    .setView(dialogView)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Category newCategory = new Category();// 새로운 카테고리 생성
                        newCategory.setCategori(ed.getText().toString());
                        newCategory.setColor(ed2.getText().toString());

                        if(db.insertCategories(db.CATEGORIES_ID, newCategory.categoriName, newCategory.color)){
                            Toast.makeText(MainActivity.this, db.getCategoriesResult(), Toast.LENGTH_SHORT).show();
                        }

                        categorisSpinner = CategoriesSpinnerUpdate();
                   }
                })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNull()) { // 카테고리가 비어있으면 오류가 발생할수 있기 때문에

                    dialogMakerAddOrEdit(1, 0);
                }

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final String[] array = new String[] {"삭제", "수정"};

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("선택하세요.")
                        .setItems(array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(array[i].equals("삭제")) {
                                    db.deleteTodos(position + 1);
                                    makeListItem();
                                }else{

                                    if(checkNull()) { // 카테고리가 비어있으면 오류가 발생할수 있기 때문에

                                        dialogMakerAddOrEdit(2, position);
                                    }

                                    dialogInterface.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 옵션메뉴 생성을 위한 메소드 수정 X
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1:

                if(checkNull()) {

                    final String[] items = new String[categorisSpinner.size() + 1];
                    items[0] = "전체보기";

                    for(int i = 1; i <= categorisSpinner.size(); i++){
                        items[i] = categorisSpinner.get(i - 1);
                    }


                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("카테고리 선택") //카테고리 선택하기
                            .setSingleChoiceItems(items, selectedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    selectedItem = i;
                                    categoriSelected = items[i];

                                    Toast.makeText(MainActivity.this, items[i] + " 카테고리 항목만 보여드립니다.", Toast.LENGTH_SHORT).show();

                                    //makeListItem(); // 리스트 항목에 띄워주기

                                    dialogInterface.dismiss(); // 라디오버튼 클릭시 바로 다이얼로그 종료
                                }
                            })
                            .show();
                }

                break;
            case R.id.menu2:

                ArrayList<String> items = new ArrayList<>();

                String[] dateAll = db.getTOdosResultDate().split("\n");

                items.add("전체날짜");

                for(String date : dateAll){
                    if(!items.contains(date)){
                        items.add(date);
                    }
                }

                final String[] fItems = new String[items.size()];

                for(int i = 0; i < items.size(); i++){
                    fItems[i] = items.get(i);
                }




                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("날짜 선택")
                        .setSingleChoiceItems(fItems, selectedDate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedDate = i;

                                date = fItems[i];

                                Toast.makeText(MainActivity.this,  fItems[i] + " 날짜에 해당하는 항목만 보여드립니다.", Toast.LENGTH_SHORT).show();

                                //makeListItem();

                                dialogInterface.dismiss();
                            }
                        }).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkNull(){ //비어있는지 확인하는 변수
        if(db.getCategoriesResult().equals("")){
            Toast.makeText(this, "카테고리가 없습니다. 카테고리를 먼저 생성해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }


    }

    public void makeListItem(){ // 리스트 어댑터를 만들어 리스트에 연결해주는 변수 부를때마다 리스트 어댑터를 만들어 어댑터에 보내주기 때문에
                                // notify가 필요하지 않음

        String[] insertBefore = db.getTodosResultAll().split("\n");

        if(!insertBefore[0].equals("")) {
            ArrayList<AdapterItemData> insert = new ArrayList<>();

            for (String s : insertBefore) {
                String[] s1 = s.split(":");
                AdapterItemData newData = new AdapterItemData();

                newData.setTodoName(s1[1]);
                newData.setTodoContent(s1[2]);
                newData.setCategoriName(this.getCategories(Integer.parseInt(s1[3])).getCategori());
                newData.setColor(this.getCategories(Integer.parseInt(s1[3])).getColor());
                newData.setDate(s1[4]);

                insert.add(newData);
            }

            itemAdapter adapter = new itemAdapter();
            adapter.addItem(insert);
            listView.setAdapter(adapter);
        }

    }




    public void dialogMakerAddOrEdit(final int id, final int position){

        String[] datas = db.getTodosResultById(position + 1).split(":");

        final AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this); // 다이얼로그 생성
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_todo_add, null); // 인플레이터로 VIEW객체 생성
        final EditText ed = dialogView.findViewById(R.id.todoName); // 위 객체 안의 위젯 객체들과 연결
        final EditText ed2 = dialogView.findViewById(R.id.todoContent);
        final Spinner spinner = dialogView.findViewById(R.id.itemPicker);
        final DatePicker datePicker = dialogView.findViewById(R.id.datePicker);

        if(id == 2){
            ed.setText(datas[1]);
            ed2.setText(datas[2]);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, categorisSpinner); // 아답터 생
        spinner.setAdapter(arrayAdapter);// 아답터 연결 (스피너)

        dlg.setTitle("할일 추가")
                .setView(dialogView)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String date = datePicker.getYear() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth();

                        Todo newTodo = new Todo(ed.getText().toString(), ed2.getText().toString(), (spinner.getSelectedItemPosition() + 1), date);

                        if(id == 2){
                            db.updateTodosData(String.valueOf(position + 1), newTodo.getTodoName(), newTodo.getTodoContent(), String.valueOf(newTodo.getCategoryid()), newTodo.getDate());
                        }
                        else{
                            db.insertTodos(db.TODOS_ID, newTodo.getTodoName(), newTodo.getTodoContent(), String.valueOf(newTodo.getCategoryid()), newTodo.getDate());
                        }

                        makeListItem(); // 리스트 항목에 띄워주기

                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    public ArrayList CategoriesSpinnerUpdate(){
        String result = db.getCategoriesResult();
        ArrayList<String> newCategoriesSpinner = new ArrayList<>();
        if(!result.equals("")) {
            String[] sli = result.split("\n");

            for (String s : sli) {
                String[] strings = s.split(":");
                newCategoriesSpinner.add(strings[1]);
            }
        }

        return newCategoriesSpinner;
    }

    public Category getCategories(int id) {
        String[] categoriString = db.getCategoriesResultById(id).split(":");
        Category newcategory = new Category();
        if(!categoriString[0].equals("")) {
            newcategory.setCategori(categoriString[1]);
            newcategory.setColor(categoriString[2]);
        }else{
            newcategory.setCategori("ERRER");
            newcategory.setColor("000000");
        }
        return newcategory;
    }

}
