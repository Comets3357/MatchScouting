package com.example.matchscouting.dao;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ActiveEventKeyDao {
    @Query("SELECT active_event_key FROM ActiveEventKey WHERE pk = '1'")
    String getActiveEventKey();

    @Query("UPDATE ActiveEventKey SET active_event_key = :activeEventKey WHERE pk = '1'")
    void setActiveEventKey(String activeEventKey);

    @Query("INSERT INTO ActiveEventKey VALUES ('1', '')")
    void initEventKey();

    @Query("SELECT COUNT(active_event_key) FROM ActiveEventKey WHERE pk = '1'")
    int countEventKey();

    @Query("INSERT INTO ActiveEventKey VALUES ('2', '0')")
    void initMatchScheduleKey();

    @Query("UPDATE ActiveEventKey SET active_event_key = :newVal WHERE pk = '2'")
    void setMatchScheduleKey(String newVal);

    @Query("SELECT COUNT(active_event_key) FROM ActiveEventKey WHERE pk = '2'")
    int countMatchScheduleKey();

    @Query("SELECT active_event_key FROM ActiveEventKey WHERE pk = '2'")
    String getMatchScheduleKey();

}
