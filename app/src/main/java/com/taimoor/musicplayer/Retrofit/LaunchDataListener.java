package com.taimoor.musicplayer.Retrofit;

import com.taimoor.musicplayer.Models.LaunchDataAPI;

public interface LaunchDataListener {

    void onFetch(LaunchDataAPI response, String message);
    void onError(String message);

}
