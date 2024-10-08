package com.example.matchscouting.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.matchscouting.common.Team;
import com.example.matchscouting.common.TeamMatchScout;

import java.util.List;

@Dao
public interface TeamDao {
    @Query("SELECT team_name FROM Team WHERE team_number = :teamNumber")
    String getTeamName(String teamNumber);
    @Insert
    void insertAll(Team... teams);

    @Delete
    void delete(Team team);

    @Query("SELECT COUNT(team_name) FROM Team WHERE team_number = :team")
    int teamExists(String team);

    @Query("SELECT * FROM Team WHERE team_number IN (SELECT DISTINCT team_number FROM TeamMatchScout WHERE event_key = :eventKey) ORDER BY cast(team_number as int)")
    List<Team> getTeamsWithScoutedMatches(String eventKey);
}
