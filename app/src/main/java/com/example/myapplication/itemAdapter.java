package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class itemAdapter extends BaseAdapter {

    ArrayList<AdapterItemData> data = new ArrayList<>();

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.todo_item, parent, false);
        }

        LinearLayout back = convertView.findViewById(R.id.back);
        LinearLayout categoriColor = convertView.findViewById(R.id.categoriColor);
        TextView todoItemName = convertView.findViewById(R.id.todoItemName);
        TextView todoItemContents = convertView.findViewById(R.id.todoItemContents);
        TextView todoItemCategori = convertView.findViewById(R.id.todoItemCategori);
        TextView todoItemDate = convertView.findViewById(R.id.todoItemDate);

        final AdapterItemData data = this.data.get(position);

        categoriColor.setBackgroundColor(Color.rgb(data.red, data.green, data.blue));
        todoItemName.setText(data.getTodoName());
        todoItemContents.setText("내용: " + data.getTodoContent());
        todoItemCategori.setText("카테고리: " + data.getCategoriName());
        todoItemDate.setText("날짜: " + data.getDate());
        if (data.isStatus()) {
            back.setBackgroundColor(Color.rgb(0, 102, 0));
        }

        return convertView;
    }

    public void addItem(ArrayList items) {
        this.data = items;
    }

}
