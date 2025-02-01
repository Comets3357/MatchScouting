package com.example.matchscouting.common;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * POJO for Match Scout Data
 */
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

    @ColumnInfo(name="auto_l4_coral")
    public String autoL4Coral;

    @ColumnInfo(name="auto_l3_coral")
    public String autoL3Coral;

    @ColumnInfo(name="auto_l2_coral")
    public String autoL2Coral;

    @ColumnInfo(name="auto_l1_coral")
    public int autoL1Coral;

    @ColumnInfo(name="tele_l4_coral")
    public String teleL4Coral;

    @ColumnInfo(name="tele_l3_coral")
    public String teleL3Coral;

    @ColumnInfo(name="tele_l2_coral")
    public String teleL2Coral;

    @ColumnInfo(name="tele_l1_coral")
    public int teleL1Coral;

    @ColumnInfo(name="auto_net")
    public int autoNet;

    @ColumnInfo(name="tele_net")
    public int teleNet;

    @ColumnInfo(name="auto_proc")
    public int autoProcessor;

    @ColumnInfo(name="tele_proc")
    public int teleProcessor;

    @ColumnInfo(name="auto_l3_algae")
    public String autoL3Algae;

    @ColumnInfo(name="auto_l2_algae")
    public String autoL2Algae;

    @ColumnInfo(name="tele_l3_algae")
    public String teleL3Algae;

    @ColumnInfo(name="tele_l2_algae")
    public String teleL2Algae;

    @ColumnInfo(name="endgame")
    public String endgame;

    @ColumnInfo(name="tablet")
    public String tablet;

    @ColumnInfo(name="scouter")
    public String scouter;
}
