package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class itemAdapter extends BaseAdapter{

    ArrayList<todo> todos = new ArrayList<>();

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Object getItem(int position) {
        return todos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.todo_item, parent, false);
        }

        LinearLayout categoriColor = convertView.findViewById(R.id.categoriColor);
        TextView todoItemName = convertView.findViewById(R.id.todoItemName);
        TextView todoItemContents = convertView.findViewById(R.id.todoItemContents);
        TextView todoItemCategori = convertView.findViewById(R.id.todoItemCategori);
        TextView todoItemDate = convertView.findViewById(R.id.todoItemDate);

        todo itemTodo = todos.get(position);

        categoriColor.setBackgroundColor(Color.rgb(itemTodo.categori.red, itemTodo.categori.green, itemTodo.categori.blue));
        todoItemName.setText(itemTodo.todoName);
        todoItemContents.setText(itemTodo.todoContent);
        todoItemCategori.setText(itemTodo.categori.categori);
        todoItemDate.setText(itemTodo.year + " . " + itemTodo.month + " . " + itemTodo.date);


        return convertView;
    }

    public void addItem(ArrayList items){
        this.todos = items;
    }
}
