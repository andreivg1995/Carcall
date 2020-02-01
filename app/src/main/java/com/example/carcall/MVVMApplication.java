package com.example.carcall;

import android.app.Application;

public class MVVMApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        MVVMRepository.get(this);
    }
}
