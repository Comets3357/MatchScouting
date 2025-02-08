package com.example.matchscouting.common;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class JsonResponse {
    @SerializedName("response")
    ScoutingDataServerResponse response;
}
