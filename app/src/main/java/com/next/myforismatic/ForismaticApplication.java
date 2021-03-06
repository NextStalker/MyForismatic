package com.next.myforismatic;

import android.app.Application;

import com.next.myforismatic.di.AppComponent;
import com.next.myforismatic.di.DaggerAppComponent;
import com.next.myforismatic.di.AppModule;

/**
 * Created by Next on 07.04.2016.
 */
public class ForismaticApplication extends Application {

    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();
    }

}
