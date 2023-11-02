package com.example.matchscouting.common;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(primaryKeys = {"team_number", "event_key"})
@Getter
@Setter
public class TeamMatchScout {
    @ColumnInfo(name="team_number")
    @NonNull
    public String teamNumber;
    @ColumnInfo(name="event_key")
    @NonNull
    public String event;
}
