package com.ark.dmptest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("positions.json")
    Call<List<Job>> getJob();

    @GET("positions.json")
    Call<List<Job>> getMoreJob(@Query("description") String desc,
                               @Query("location") String location,
                               @Query("full_time") String fullTime,
                               @Query("page") Integer page);
}
