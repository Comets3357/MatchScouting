package com.example.matchscouting.common;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;

@Entity
@Getter
public class Team {
    @PrimaryKey
    @ColumnInfo(name = "team_number")
    @NonNull
    public String teamNumber;

    @ColumnInfo(name = "team_name")
    public String teamName;
}