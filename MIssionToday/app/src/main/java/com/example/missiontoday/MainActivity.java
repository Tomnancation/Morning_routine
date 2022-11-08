package com.example.missiontoday;
//Main interface action
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
//引用地址改完
import com.example.missiontoday.Model.TaskModel;
import com.example.missiontoday.Adapter.TaskAdapter;
import com.example.missiontoday.Model.Database.DataBase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

//import net.penguincoders.doit.Adapters.TaskAdapter;
//import net.penguincoders.doit.Model.TaskModel;
//import net.penguincoders.doit.Utils.DataBase;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView tasksRecyclerView;
    private TaskAdapter tasksAdapter;
    private FloatingActionButton fab;
    private DataBase db;

    private List<TaskModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();



        db = new DataBase(this);
        db.openDatabase();
        //set the adapter to the recycle view
        tasksRecyclerView = findViewById(R.id.taskRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TaskAdapter(db,MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab);
        //Load task bar behavior
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}
