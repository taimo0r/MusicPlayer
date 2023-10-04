package com.taimoor.musicplayer.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SearchedSongsDao {


    @Query(" Select * From SearchedSongs")
    List<SearchedSongs> getSearchedSongs();

    @Query("Select song From SearchedSongs ")
    List<String> geSongs();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void SaveSearchedSong(SearchedSongs songs);

    @Delete
    void delete(SearchedSongs songs);

    @Query(" DELETE FROM SearchedSongs")
    void deleteAll();

}
