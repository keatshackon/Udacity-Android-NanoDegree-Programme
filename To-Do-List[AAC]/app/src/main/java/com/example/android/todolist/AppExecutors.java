package com.example.android.todolist;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIo;
    private final Executor mainThread;
    private final Executor networkIo;


    private AppExecutors(Executor diskIO, Executor networkIo,Executor mainThread){
        this.diskIo = diskIO;
        this.networkIo= networkIo;
        this.mainThread = mainThread;
    }

    public static AppExecutors getInstance(){
        if(sInstance == null){
            synchronized (LOCK){            // diskio executors  ensure the database operation done sequentially
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        //it is pool of three thread which allows to run different network call  simultaneously while controlling number of thread we have
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }
    public Executor diskIo(){
        return diskIo;
    }
    public Executor getNetworkIo(){
        return networkIo;
    }
    public Executor mainThread(){
        return mainThread;
    }



    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }



}
