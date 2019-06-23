package com.example.mieib.aleftask.TodoList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mieib.aleftask.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {


    Context context;
    public List todolist;

    public TodoListAdapter(Context context,List list){
        this.context =context;
        this.todolist = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.todo_list_item, viewGroup, false);
        return new TodoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.todoitemdate.setText(((Todo) todolist.get(i)).getDate());
            viewHolder.todoitemtext.setText(((Todo) todolist.get(i)).getText());
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.todoitemdate)
        TextView  todoitemdate;
        @BindView(R.id.todoitemtext)
        TextView  todoitemtext;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
