package com.example.missiontoday.Model.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.missiontoday.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

//android sqlite db
public class DataBase extends SQLiteOpenHelper {
//Data table settings
    private static final int VERSION = 1;
    private static final String NAME = "myDatabase";
    private static final String TASK_TABLE = "taskTable";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + TASK_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";
//define variable
    private SQLiteDatabase db;
    public DataBase(Context context) {
        super(context, NAME, null, VERSION);
    }
//Create data table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK_TABLE);
    }
//update table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        // Create table
        onCreate(db);
    }
//Open database for calling
    public void openDatabase() {
        db = this.getWritableDatabase();
    }
//insert task
    public void insertTask(TaskModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TASK_TABLE, null, cv);
    }
//get table
    public List<TaskModel> getAllTasks(){
        List<TaskModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TASK_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        TaskModel task = new TaskModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }
//update status
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TASK_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
//update task
    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TASK_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
//delete task
    public void deleteTask(int id){
        db.delete(TASK_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
