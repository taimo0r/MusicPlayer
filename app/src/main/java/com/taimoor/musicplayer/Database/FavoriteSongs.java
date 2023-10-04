package com.taimoor.musicplayer.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavoriteSongs", indices = {@Index(value = {"url"}, unique = true)})
public class FavoriteSongs {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "song_name")
    public String songName;

    @ColumnInfo(name = "duration")
    public String duration;

    @ColumnInfo(name = "artist")
    public String artist;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "position")
    public int position;

    @ColumnInfo(name = "url")
    public String url;

}
