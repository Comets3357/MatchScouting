package com.example.matchscouting.common;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class ScoutingDataServerResponse {
    @SerializedName("matchSchedule")
    private List<MatchSchedule> matchSchedule;
    @SerializedName("scoringTable")
    private String scoringTable;

    @SerializedName("eventKey")
    private String eventKey;
}
