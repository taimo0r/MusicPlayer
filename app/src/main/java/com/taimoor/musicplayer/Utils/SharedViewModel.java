package com.taimoor.musicplayer.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.taimoor.musicplayer.Models.Song;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SharedViewModel extends ViewModel {

    private List<Song> lastPlayedList = new ArrayList<>();
    private int lastPlayedPosition;
    private boolean miniPlayer;


    public boolean isMiniPlayer() {
        return miniPlayer;
    }

    public void setMiniPlayer(boolean miniPlayer) {
        this.miniPlayer = miniPlayer;
    }

    public int getLastPlayedPosition() {
        return lastPlayedPosition;
    }

    public void setLastPlayedPosition(int lastPlayedPosition) {
        this.lastPlayedPosition = lastPlayedPosition;
    }

    public void setLastPlayedList(List<Song> lastPlayedList) {
        this.lastPlayedList = lastPlayedList;
    }

    public List<Song> getLastPlayedList() {
        return lastPlayedList;
    }


}
