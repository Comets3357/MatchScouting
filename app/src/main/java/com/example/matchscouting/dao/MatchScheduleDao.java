package com.example.matchscouting.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.matchscouting.common.MatchSchedule;

@Dao
public interface MatchScheduleDao {

    @Query("SELECT * FROM MatchSchedule WHERE eventKey = :eventKey AND matchNumber = :matchNumber")
    MatchSchedule getMatchSchedule(String eventKey, String matchNumber);

    @Insert
    void insertAll(MatchSchedule... matchSchedules);

    @Query("SELECT COUNT(matchNumber) FROM MatchSchedule WHERE eventKey = :event_key")
    int eventHasLoadedMatches(String event_key);

    @Query("SELECT COUNT(matchNumber) FROM MatchSchedule WHERE eventKey = :event_key " +
            "AND matchNumber = :matchNumber")
    int matchAlreadyLoaded(String event_key, String matchNumber);

    @Query("SELECT COUNT(matchNumber) FROM MatchSchedule WHERE matchNumber = :match_number AND eventKey = :event_key")
    int matchExists(String event_key, String match_number);

    @Query("SELECT red1 FROM MatchSchedule WHERE matchNumber = :matchNumber AND eventKey = :eventKey")
    String getRed1(String matchNumber, String eventKey);

    @Query("SELECT red2 FROM MatchSchedule WHERE matchNumber = :matchNumber AND eventKey = :eventKey")
    String getRed2(String matchNumber, String eventKey);

    @Query("SELECT red3 FROM MatchSchedule WHERE matchNumber = :matchNumber AND eventKey = :eventKey")
    String getRed3(String matchNumber, String eventKey);

    @Query("SELECT blue1 FROM MatchSchedule WHERE matchNumber = :matchNumber AND eventKey = :eventKey")
    String getBlue1(String matchNumber, String eventKey);

    @Query("SELECT blue2 FROM MatchSchedule WHERE matchNumber = :matchNumber AND eventKey = :eventKey")
    String getBlue2(String matchNumber, String eventKey);

    @Query("SELECT blue3 FROM MatchSchedule WHERE matchNumber = :matchNumber AND eventKey = :eventKey")
    String getBlue3(String matchNumber, String eventKey);
}
