package com.ark.dmptest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("positions.json")
    Call<List<Job>> getJob(@Query("description") String desc,
                               @Query("location") String location,
                               @Query("full_time") String fullTime,
                               @Query("page") Integer page);

    @GET("positions/{id}")
    Call<Job> getJobDetail(@Path("id") String id);
}
