package com.taimoor.musicplayer.Retrofit;

import com.taimoor.musicplayer.Models.SongApiResponse;

public interface SongResponseListener {

    void onFetch(SongApiResponse response, String message);
    void onError(String message);

}
