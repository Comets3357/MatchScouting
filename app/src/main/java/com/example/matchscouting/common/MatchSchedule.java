package com.example.matchscouting.common;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Entity(primaryKeys = {"eventKey", "matchNumber"})
@Getter
@Setter
public class MatchSchedule {

    @ColumnInfo(name = "eventKey")
    @NonNull
    @SerializedName("eventKey")
    public String eventKey;

    @ColumnInfo(name = "matchNumber")
    @NonNull
    @SerializedName("matchNumber")
    public String matchNumber;

    @ColumnInfo(name = "red1")
    @SerializedName("red1")
    public String red1;

    @ColumnInfo(name = "red2")
    @SerializedName("red2")
    public String red2;

    @ColumnInfo(name = "red3")
    @SerializedName("red3")
    public String red3;

    @ColumnInfo(name = "blue1")
    @SerializedName("blue1")
    public String blue1;

    @ColumnInfo(name = "blue2")
    @SerializedName("blue2")
    public String blue2;

    @ColumnInfo(name = "blue3")
    @SerializedName("blue3")
    public String blue3;

}
