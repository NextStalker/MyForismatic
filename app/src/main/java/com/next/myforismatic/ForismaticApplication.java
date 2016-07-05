package com.next.myforismatic;

import android.app.Application;

import com.next.myforismatic.components.AppComponent;
import com.next.myforismatic.components.DaggerAppComponent;
import com.next.myforismatic.modules.AppModule;

/**
 * Created by Next on 07.04.2016.
 */
public class ForismaticApplication extends Application {

    //private ForismaticService service;
    private static AppComponent component;
    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initRetrofit();
        buildComponent();
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();
    }

/*
    private void initRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.forismatic.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        service = retrofit.create(ForismaticService.class);
    }

    public ForismaticService getForismaticService() {
        return service;
    }
*/
}
