package com.example.matchscouting.common;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(primaryKeys = {"team_number", "event_key", "match_number"})
@Getter
@Setter
public class TeamMatchScout {
    @ColumnInfo(name="team_number")
    @NonNull
    public String teamNumber;
    @ColumnInfo(name="event_key")
    @NonNull
    public String event;
    @ColumnInfo(name="match_number")
    @NonNull
    public String matchNumber;

    @ColumnInfo(name="auto_speaker")
    public int autoSpeaker;

    @ColumnInfo(name="auto_amp")
    public int autoAmp;

    @ColumnInfo(name="auto_leave")
    public int autoLeave;

    @ColumnInfo(name="tele_speaker")
    public int teleSpeaker;

    @ColumnInfo(name="tele_amp")
    public int teleAmp;

    @ColumnInfo(name="trap")
    public int trap;

    @ColumnInfo(name="climb_status")
    public int climbStatus;

    @ColumnInfo(name="defense")
    public int defense;

    @ColumnInfo(name="tablet")
    public String tablet;

    @ColumnInfo(name="scouter")
    public String scouter;
}
