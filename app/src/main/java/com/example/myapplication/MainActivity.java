package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

    ArrayList<Categori> categoris = new ArrayList<>();
    ArrayList<String> categorisSpinner = new ArrayList<>(); //스피너에서만 쓰일 어레이 리스트 //코드의 일관성을 위해 다른용도로 사용금지
    ArrayList<Todo> Todos = new ArrayList<>();

    Button add, categoriAdd;
    ListView listView;

    String categoriSelected = "전체보기"; // 초기값은 항상 전체보기로 설
    int selectedItem = 0; // 전에 표시했던 카테고리가 어떤 카테고리 였는지 체크하기 위해 쓰는 변수 //코드의 일관성을 위해 다른용도로 사용금
    int selectedDate = 0;

    int[] day = new int[]{0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoriAdd = findViewById(R.id.categoriAdd); // 위젯 연결
        add = findViewById(R.id.add);
        listView = findViewById(R.id.todoList);

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

                        Categori newCategori = new Categori();// 새로운 카테고리 생성
                        newCategori.setCategori(ed.getText().toString());
                        newCategori.setColor(ed2.getText().toString());

                        categoris.add(newCategori); // 카테고리 목록에 추가
                        categorisSpinner.add(categoris.get(categoris.size() - 1).categoriName); // 스피너를 위한 카테고리 이름 목록에 추가

                        Toast.makeText(MainActivity.this, categoris.get(categoris.size() - 1).categoriName +
                                "가 추가되었습니다 색상 코드: " +
                                categoris.get(categoris.size() - 1).red +
                                ", " + categoris.get(categoris.size() - 1).green +
                                ", " + categoris.get(categoris.size() - 1).blue,
                                Toast.LENGTH_SHORT).show();

                   }
                })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNull(categoris)) { // 카테고리가 비어있으면 오류가 발생할수 있기 때문에

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
                                    Todos.remove(position);
                                    makeListItem();
                                }else{

                                    if(checkNull(categoris)) { // 카테고리가 비어있으면 오류가 발생할수 있기 때문에

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

                if(checkNull(categoris)) { //카테고리가 비어있으면 오류가 발생할수 있기 때문에

                    final String[] items = new String[categoris.size() + 1]; // 첫 아이템이 전체보기 이기 때문에 카테고리 수보다 1만큼 큰 배열 필요

                    items[0] = "전체보기"; // 첫 아이템은 전체보기로 설정

                    for (int i = 0; i < categoris.size(); i++) {
                        items[i + 1] = categoris.get(i).categoriName; // 카테고리들이 전체보기 다음에 들어가야 하므로 i + 1
                    }

                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("카테고리 선택") //카테고리 선택하기
                            .setSingleChoiceItems(items, selectedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    selectedItem = i;
                                    categoriSelected = items[i];

                                    Toast.makeText(MainActivity.this, items[i] + " 카테고리 항목만 보여드립니다.", Toast.LENGTH_SHORT).show();

                                    makeListItem(); // 리스트 항목에 띄워주기

                                    dialogInterface.dismiss(); // 라디오버튼 클릭시 바로 다이얼로그 종료
                                }
                            })
                            .show();
                }

                break;
            case R.id.menu2:

                int j = 0;

                String[] items = new String[Todos.size() + 1];

                for(int i = 0; i < items.length; i++){
                    items[i] = "null";
                }

                items[j] = "전체날짜";
                j++;

                for(int i = 0; i < Todos.size(); i++){
                    if(!(Arrays.asList(items).contains(Todos.get(i).getYear() + "/" + Todos.get(i).getMonth() + "/" + Todos.get(i).getDate()))){
                        items[j] = Todos.get(i).getYear() + "/" + Todos.get(i).getMonth() + "/" + Todos.get(i).getDate();
                        j++;
                    }else{
                        continue;
                    }
                }

                final String[] fItems = new String[j];

                for(int i = 0; i < j; i++){
                    fItems[i] = items[i];
                }

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("날짜 선택")
                        .setSingleChoiceItems(fItems, selectedDate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedDate = i;

                                if(fItems[i].equals("전체날짜")){
                                    day[0] = 0;
                                    day[1] = 0;
                                    day[2] = 0;
                                }else {
                                    String array[] = fItems[i].split("/");
                                    day[0] = Integer.parseInt(array[0]);
                                    day[1] = Integer.parseInt(array[1]);
                                    day[2] = Integer.parseInt(array[2]);
                                }

                                Toast.makeText(MainActivity.this,  fItems[i] + " 날짜에 해당하는 항목만 보여드립니다.", Toast.LENGTH_SHORT).show();

                                makeListItem();

                                dialogInterface.dismiss();
                            }
                        }).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkNull(ArrayList items){ //비어있는지 확인하는 변수
        if(items.size() == 0){
            Toast.makeText(this, "카테고리가 없습니다. 카테고리를 먼저 생성해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }


    }

    public void makeListItem(){ // 리스트 어댑터를 만들어 리스트에 연결해주는 변수 부를때마다 리스트 어댑터를 만들어 어댑터에 보내주기 때문에
                                // notify가 필요하지 않음

        ArrayList<Todo> insert = new ArrayList<>(); // 필터링된 항목을 담는 배열

        if(categoriSelected.equals("전체보기") && (day[0] == 0 && day[1] == 0 && day[2] == 0)){ // 카테고리와 날짜를 설정했을때 해당하지 않는 항목들 필터링
            for(int i = 0; i< Todos.size(); i++){
                insert.add(Todos.get(i));
            }
        }else if(!categoriSelected.equals("전체보기") && (day[0] == 0 && day[1] == 0 && day[2] == 0)){
            for (int i = 0; i < Todos.size(); i++) {
                if(categoriSelected.equals(Todos.get(i).categori.categoriName)){
                    insert.add(Todos.get(i));
                }
            }
        }else if(categoriSelected.equals("전체보기") && !(day[0] == 0 && day[1] == 0 && day[2] == 0)){
            for (int i = 0; i < Todos.size(); i++){
                if(day[0] == Todos.get(i).getYear() && day[1] == Todos.get(i).getMonth() && day[2] == Todos.get(i).getDate()){
                    insert.add(Todos.get(i));
                }
            }
        }else {
            for (int i = 0; i < Todos.size(); i++){
                if(day[0] == Todos.get(i).getYear() && day[1] == Todos.get(i).getMonth() && day[2] == Todos.get(i).getDate() && categoriSelected.equals(Todos.get(i).categori.categoriName)){
                    insert.add(Todos.get(i));
                }
            }
        }
        itemAdapter adapter = new itemAdapter();
        adapter.addItem(insert); // 필터링된 항목 보내주기
        listView.setAdapter(adapter);
    }




    public void dialogMakerAddOrEdit(final int id, final int position){

        final AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this); // 다이얼로그 생성
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_todo_add, null); // 인플레이터로 VIEW객체 생성
        final EditText ed = dialogView.findViewById(R.id.todoName); // 위 객체 안의 위젯 객체들과 연결
        final EditText ed2 = dialogView.findViewById(R.id.todoContent);
        final Spinner spinner = dialogView.findViewById(R.id.itemPicker);
        final DatePicker datePicker = dialogView.findViewById(R.id.datePicker);

        if(id == 2){
            ed.setText(Todos.get(position).todoName);
            ed2.setText(Todos.get(position).todoContent);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, categorisSpinner); // 아답터 생
        spinner.setAdapter(arrayAdapter);// 아답터 연결 (스피너)

        dlg.setTitle("할일 추가")
                .setView(dialogView)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Todo newTodo = new Todo(ed.getText().toString(), ed2.getText().toString(),categoris.get(spinner.getSelectedItemPosition()) , datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                        if(id == 2){
                            Todos.add(position, newTodo);
                            Todos.remove(position + 1);
                        }
                        else{
                            Todos.add(newTodo);
                        }

                        makeListItem(); // 리스트 항목에 띄워주기
                        Toast.makeText(MainActivity.this, Todos.get(Todos.size() - 1).categori.categoriName, Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

}
