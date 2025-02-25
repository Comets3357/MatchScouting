package com.example.matchscouting.dao;

import com.example.matchscouting.common.JsonResponse;
import com.example.matchscouting.common.PostDataResponse;
import com.example.matchscouting.common.ScoutingDataServerResponse;
import com.example.matchscouting.common.TeamMatchScout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ScoutingDataServerAPIService {

    @GET("/app/getData")
    Call<JsonResponse> getScoutingDataServerData();

    @POST("/app/uploadMatches")
    Call<PostDataResponse> sendDataToServer(@Body List<TeamMatchScout> dataToSend);

}
