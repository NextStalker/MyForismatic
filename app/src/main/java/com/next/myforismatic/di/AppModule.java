package com.next.myforismatic.di;

import android.support.annotation.NonNull;

import com.next.myforismatic.api.ForismaticService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maslparu on 28.06.2016.
 */
@Module
public class AppModule {

    @Provides
    @NonNull
    @Singleton
    public ForismaticService provideForismaticService() {
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
        return retrofit.create(ForismaticService.class);
    }

}
