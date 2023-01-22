package com.heitorcandido.tasklist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.heitorcandido.tasklist.Model.Task;
import com.heitorcandido.tasklist.R;

import org.w3c.dom.Text;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Task> taskList;

    public TaskAdapter(List<Task> list) {
        // Get Task list
        this.taskList = list;
    }

    public Context getContext(){

        return getContext();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Define the layout
        View itemList = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.tasklist_adapter,
                parent,
                false );

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.task.setText(task.getTaskName());
        holder.task.setChecked(task.getStatusTask());
    }

    @Override
    public int getItemCount() {
        // How many items appears
        return this.taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox task;

        // Get items of layout
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.checkBoxTask);
        }
    }

}
