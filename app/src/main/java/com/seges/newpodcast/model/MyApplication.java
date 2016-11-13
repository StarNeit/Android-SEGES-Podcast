package com.seges.newpodcast.model;

import android.app.Application;
import android.content.ContextWrapper;

import com.seges.newpodcast.utils.Prefs;

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
