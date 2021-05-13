package com.example.triviatime;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetOpenTBResponse {
    @GET("api.php?amount=21")
    Call<TBResponse> getResponse();


    @GET("api.php?amount=21&difficulty=easy")
    Call<TBResponse> getEasyResponse();


    @GET("api.php?amount=21&difficulty=medium")
    Call<TBResponse> getMediumResponse();


    @GET("api.php?amount=21&difficulty=hard")
    Call<TBResponse> getHardResponse();
}
