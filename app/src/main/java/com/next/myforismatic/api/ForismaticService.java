package com.next.myforismatic.api;

import com.next.myforismatic.models.Quote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Next on 07.04.2016.
 */
public interface ForismaticService {

    @GET("api/1.0/")
    Call<Quote> getQuote(
            @Query("method") String method,
            @Query("format") String format,
            @Query("lang") String lang,
            @Query("key") int key
    );
}
