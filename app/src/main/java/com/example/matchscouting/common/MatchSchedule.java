package com.example.matchscouting.common;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(primaryKeys = {"eventKey", "matchNumber"})
@Getter
public class MatchSchedule {

    @ColumnInfo(name = "eventKey")
    @NonNull
    public String eventKey;

    @ColumnInfo(name = "matchNumber")
    @NonNull
    public String matchNumber;

    @ColumnInfo(name = "red1")
    public String red1;

    @ColumnInfo(name = "red2")
    public String red2;

    @ColumnInfo(name = "red3")
    public String red3;

    @ColumnInfo(name = "blue1")
    public String blue1;

    @ColumnInfo(name = "blue2")
    public String blue2;

    @ColumnInfo(name = "blue3")
    public String blue3;

}
