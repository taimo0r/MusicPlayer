package com.taimoor.musicplayer.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteSongsDao {

    @Query(" Select * From FavoriteSongs")
    List<FavoriteSongs> getFavoriteSongs();

    @Query("Select url From FavoriteSongs Where FavoriteSongs.song_name == :name")
    String getSong(String name);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void SaveFavoriteSong(FavoriteSongs songs);

    @Delete
    void delete(FavoriteSongs songs);

    @Query(" DELETE FROM FavoriteSongs")
    void deleteAll();


}
