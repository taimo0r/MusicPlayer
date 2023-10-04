package com.taimoor.musicplayer.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SearchedSongs.class, FavoriteSongs.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {


  public abstract SearchedSongsDao searchedSongsDao();

  public abstract FavoriteSongsDao favoriteSongsDao();

    private static AppDatabase Instance;

    public static AppDatabase getInstance(Context context){
        if (Instance == null){
            Instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"SearchedSongs")
                    .allowMainThreadQueries()
                    .build();
        }
        return Instance;
    }

}
