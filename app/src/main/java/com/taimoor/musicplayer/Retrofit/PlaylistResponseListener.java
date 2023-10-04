package com.taimoor.musicplayer.Retrofit;

import com.taimoor.musicplayer.Models.PlaylistApiResponse;
import com.taimoor.musicplayer.Models.SongApiResponse;

public interface PlaylistResponseListener {

    void onFetch(PlaylistApiResponse response, String message);
    void onError(String message);

}
