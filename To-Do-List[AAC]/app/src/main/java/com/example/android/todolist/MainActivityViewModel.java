package com.example.android.todolist;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.todolist.database.AppDatabase;
import com.example.android.todolist.database.TaskEntry;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private LiveData<List<TaskEntry>> tasks;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mdb = AppDatabase.getInstance(this.getApplication());
//        Log.d(TAG, "MainActivityView Model is created");
        tasks = mdb.taskDao().loadAllTasks();

    }
    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }
}
