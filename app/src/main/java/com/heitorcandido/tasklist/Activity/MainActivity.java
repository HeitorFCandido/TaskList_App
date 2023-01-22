package com.heitorcandido.tasklist.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.heitorcandido.tasklist.Adapter.TaskAdapter;
import com.heitorcandido.tasklist.Helper.RecyclerItemClickListener;
import com.heitorcandido.tasklist.Helper.TaskDAO;
import com.heitorcandido.tasklist.Model.Task;
import com.heitorcandido.tasklist.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove elevation actionBar
        getSupportActionBar().setElevation(0);

        // Configure a recycler
        recyclerView = findViewById(R.id.recyclerView);

        // Adding click event in RecyclerView
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                // Check the task
                                TaskDAO taskDao = new TaskDAO(getApplicationContext());
                                Task taskSelect = taskList.get(position);

                                Task task = new Task();
                                task.setTaskName(taskSelect.getTaskName());
                                task.setId(taskSelect.getId());

                                if (taskSelect.getStatusTask()) {
                                    task.setStatusTask(0);
                                } else {
                                    task.setStatusTask(1);
                                }

                                taskDao.update(task);
                                loadTask();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                // Update the task
                                Task taskSelect = taskList.get(position);
                                openDialog(
                                        "Edit Task",
                                        "Edit a task...",
                                        "Save",
                                        taskSelect);

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            openDialog(
                    "New Task",
                    "Type a new task...",
                    "Save",
                    null);
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        loadTask();
        super.onWindowFocusChanged(hasFocus);
    }

    private void loadTask() {

        // List Tasks
        TaskDAO taskDAO = new TaskDAO(getApplicationContext());
        taskList = taskDAO.list();

        // Configure an adapter
        taskAdapter = new TaskAdapter(taskList);

        // Configure RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(taskAdapter);

        moveRecycler();

    }

    private void moveRecycler() {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                TaskDAO taskDao = new TaskDAO(getApplicationContext());
                Task taskSelect = taskList.get(viewHolder.getAdapterPosition());

                if (taskDao.delete(taskSelect)) {

                    Snackbar undoSnack = Snackbar.make(findViewById(R.id.coordinator),
                            "Task \"" + taskSelect.getTaskName()+ "\"" + " excluded.", Snackbar.LENGTH_LONG);

                    undoSnack.setAction("Undo", view -> {
                        Task task = new Task();
                        task.setTaskName(taskSelect.getTaskName());
                        task.setStatusTask(taskSelect.getStatusTaskINT());
                        task.setId(taskSelect.getId());
                        taskDao.save(task);
                        loadTask();
                    });
                    undoSnack.show();

                } else {
                    Toast.makeText(getApplicationContext(), "Fail in delete the task", Toast.LENGTH_SHORT).show();
                }

                loadTask();
            }
        }).attachToRecyclerView(recyclerView);


    }


    private void openDialog(String txtTitle, String hintInput, String txtButton, Task taskSelect) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        TextView textTitle = dialog.findViewById(R.id.textTitle);
        EditText inputTask = dialog.findViewById(R.id.inputNewTask);

        textTitle.setText(txtTitle);
        inputTask.setHint(hintInput);
        btnSave.setText(txtButton);

        TaskDAO taskDao = new TaskDAO(getApplicationContext());

        if (taskSelect == null) {
            btnSave.setOnClickListener(view -> {
                // Save mode
                String taskName = inputTask.getText().toString();
                if (!taskName.isEmpty()) {

                    Task task = new Task();
                    task.setTaskName(taskName);
                    task.setStatusTask(0);

                    if (taskDao.save(task)) {
                        dialog.dismiss();
                        Toast.makeText(this, "Success in saving the task!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Fail in saving the task", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Type a task", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Edit Mode
            inputTask.setText(taskSelect.getTaskName());

            btnSave.setOnClickListener(view -> {
                // Save pressed
                String taskName = inputTask.getText().toString();

                if (!taskName.isEmpty()) {

                    Task task = new Task();
                    task.setTaskName(taskName);
                    task.setId(taskSelect.getId());
                    task.setStatusTask(taskSelect.getStatusTaskINT());


                    // Update DataBase
                    if (taskDao.update(task)) {
                        dialog.dismiss();
                        Toast.makeText(this, "Success in saving the task!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Fail in saving the task", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}