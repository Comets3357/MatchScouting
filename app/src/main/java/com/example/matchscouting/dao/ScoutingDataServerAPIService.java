package com.example.matchscouting.dao;

import com.example.matchscouting.common.JsonResponse;
import com.example.matchscouting.common.ScoutingDataServerResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ScoutingDataServerAPIService {

    @GET("/app/getData")
    Call<JsonResponse> getScoutingDataServerData();

}
