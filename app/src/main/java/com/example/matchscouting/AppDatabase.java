package com.example.matchscouting;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.matchscouting.common.ActiveEventKey;
import com.example.matchscouting.common.MatchSchedule;
import com.example.matchscouting.common.Team;
import com.example.matchscouting.common.TeamMatchScout;
import com.example.matchscouting.dao.ActiveEventKeyDao;
import com.example.matchscouting.dao.MatchScheduleDao;
import com.example.matchscouting.dao.TeamDao;
import com.example.matchscouting.dao.TeamMatchScoutDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Team.class, TeamMatchScout.class, ActiveEventKey.class, MatchSchedule.class}, version = 6, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TeamDao teamDao();

    public abstract TeamMatchScoutDao teamMatchScoutDao();

    public abstract ActiveEventKeyDao activeEventKeyDao();

    public abstract MatchScheduleDao matchScheduleDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
