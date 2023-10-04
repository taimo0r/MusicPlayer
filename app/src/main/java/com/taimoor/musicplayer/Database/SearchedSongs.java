package com.taimoor.musicplayer.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "SearchedSongs", indices = {@Index(value = {"song"}, unique = true)})
public class SearchedSongs {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "song")
    public String song;

}
