package com.taimoor.musicplayer.Retrofit;

import com.taimoor.musicplayer.Models.AlbumApiResponse;
import com.taimoor.musicplayer.Models.SongApiResponse;

public interface AlbumResponseListener {

    void onFetch(AlbumApiResponse response, String message);
    void onError(String message);

}
