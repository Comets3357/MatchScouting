package com.example.matchscouting.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.matchscouting.common.TeamMatchScout;

import java.util.List;

@Dao
public interface TeamMatchScoutDao {
    @Query("SELECT * FROM TeamMatchScout WHERE event_key = :eventKey AND team_number = :teamNumber")
    TeamMatchScout getTeamAtEvent(String teamNumber, String eventKey);

    @Query("SELECT * FROM TeamMatchScout WHERE event_key = :eventKey")
    List<TeamMatchScout> getAllMatchesAtEvent(String eventKey);

    @Query("SELECT COUNT(team_number) FROM TeamMatchScout WHERE event_key = :eventKey AND team_number = :teamNumber AND match_number = :matchNumber")
    int getAlreadySubmitted(String teamNumber, String eventKey, String matchNumber);

    @Query("SELECT * FROM TeamMatchScout WHERE event_key = :eventKey AND team_number = :teamNumber")
    List<TeamMatchScout> getMatchesScouted(String teamNumber, String eventKey);

    @Query("SELECT * FROM TeamMatchScout WHERE event_key = :eventKey AND team_number = :teamNumber AND match_number = :matchNumber")
    TeamMatchScout getMatchScouted(String teamNumber, String matchNumber, String eventKey);

    @Insert
    void insertAll(TeamMatchScout... teamMatchScouts);
    @Delete
    void delete(TeamMatchScout teamMatchScout);

    @Query("DELETE FROM TeamMatchScout WHERE event_key = :eventKey")
    void deleteEventRecords(String eventKey);

    @Update
    void update(TeamMatchScout teamPitScout);

}
