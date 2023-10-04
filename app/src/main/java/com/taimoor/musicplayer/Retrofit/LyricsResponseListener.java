package com.taimoor.musicplayer.Retrofit;

import com.taimoor.musicplayer.Models.LyricsApiResponse;

public interface LyricsResponseListener {

    void onFetch(LyricsApiResponse response, String message);
    void onError(String message);

}
