package com.example.matchscouting.common;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActiveEventKey {
    @PrimaryKey
    @ColumnInfo(name = "pk")
    @NonNull
    String pk;

    @ColumnInfo(name = "active_event_key")
    String activeEventKey;
}
