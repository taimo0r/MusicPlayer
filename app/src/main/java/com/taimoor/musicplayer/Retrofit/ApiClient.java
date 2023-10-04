package com.taimoor.musicplayer.Retrofit;

import static android.content.Context.MODE_PRIVATE;

import static com.taimoor.musicplayer.Activities.MainActivity.preferredLanguages;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.taimoor.musicplayer.Models.AlbumApiResponse;
import com.taimoor.musicplayer.Models.LaunchDataAPI;
import com.taimoor.musicplayer.Models.LyricsApiResponse;
import com.taimoor.musicplayer.Models.PlaylistApiResponse;
import com.taimoor.musicplayer.Models.SongApiResponse;
import com.taimoor.musicplayer.Utils.Common;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class ApiClient {

    private static String BASE_URL = "https://www.jiosaavn.com/";
    Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ApiClient(Context context) {
        this.context = context;
    }


    public interface SongApiInterface {
        @GET("api.php")
        Call<SongApiResponse> getSongs(@Query("_format") String format,
                                       @Query("_marker") int marker,
                                       @Query("api_version") int version,
                                       @Query("n") int limit,
                                       @Query("__call") String call,
                                       @Query("q") String songName);
    }

    public interface AlbumApiInterface {
        @GET("api.php")
        Call<AlbumApiResponse> getAlbum(@Query("_format") String format,
                                        @Query("_marker") int marker,
                                        @Query("__call") String call,
                                        @Query("albumid") String albumId);
    }

    public interface AlbumSearchApi {
        @GET("api.php")
        Call<SongApiResponse> getAlbums(@Query("_format") String format,
                                        @Query("_marker") int marker,
                                        @Query("api_version") int version,
                                        @Query("n") int limit,
                                        @Query("__call") String call,
                                        @Query("q") String songName);
    }

    public interface PlaylistSearchApi {
        @GET("api.php")
        Call<SongApiResponse> getPlaylist(@Query("_format") String format,
                                          @Query("_marker") int marker,
                                          @Query("api_version") int version,
                                          @Query("n") int limit,
                                          @Query("__call") String call,
                                          @Query("q") String songName);
    }

    public interface PlaylistApiInterface {
        @GET("api.php")
        Call<PlaylistApiResponse> getIdPlaylist(@Query("_format") String format,
                                                @Query("_marker") int marker,
                                                @Query("__call") String call,
                                                @Query("listid") String albumId);
    }

    public interface LyricsApiInterface {
        @GET("api.php")
        Call<LyricsApiResponse> getLyrics(@Query("__call") String call,
                                          @Query("ctx") String ctx,
                                          @Query("api_version") int version,
                                          @Query("_format") String format,
                                          @Query("_marker") int marker,
                                          @Query("lyrics_id") String lyricsId);
    }

    public interface LaunchDataInterface {

        @GET("api.php")
        Call<LaunchDataAPI> getLaunchData(@Header("Cookie") String lang,
                                          @Query("__call") String call,
                                          @Query("-format") String format,
                                          @Query("_marker") int marker,
                                          @Query("api_version") int version);

    }

    public void getLaunchData(LaunchDataListener listener) {

        SharedPreferences prefs = context.getSharedPreferences("My_Prefs", MODE_PRIVATE);
        String language = "L=" + prefs.getString("languages","english")+";" ;

        LaunchDataInterface apiInterface = retrofit.create(LaunchDataInterface.class);
        Call<LaunchDataAPI> call = apiInterface.getLaunchData(language,"webapi.getLaunchData", "json", 0, 4);

        call.enqueue(new Callback<LaunchDataAPI>() {
            @Override
            public void onResponse(Call<LaunchDataAPI> call, Response<LaunchDataAPI> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<LaunchDataAPI> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }

    public void getLyrics(LyricsResponseListener listener, String lyricsId) {

        LyricsApiInterface apiInterface = retrofit.create(LyricsApiInterface.class);
        Call<LyricsApiResponse> call = apiInterface.getLyrics("lyrics.getLyrics", "web6dot0", 4, "json", 0, lyricsId);

        call.enqueue(new Callback<LyricsApiResponse>() {
            @Override
            public void onResponse(Call<LyricsApiResponse> call, Response<LyricsApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<LyricsApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }

    public void getSelectedPlaylists(PlaylistResponseListener listener, String listId) {

        PlaylistApiInterface apiInterface = retrofit.create(PlaylistApiInterface.class);
        Call<PlaylistApiResponse> call = apiInterface.getIdPlaylist("json", 0, "playlist.getDetails", listId);

        call.enqueue(new Callback<PlaylistApiResponse>() {
            @Override
            public void onResponse(Call<PlaylistApiResponse> call, Response<PlaylistApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<PlaylistApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }


    public void getPlaylists(SongResponseListener listener, String query) {

        PlaylistSearchApi api = retrofit.create(PlaylistSearchApi.class);
        Call<SongApiResponse> call = api.getPlaylist("json", 0, 4, 30, "search.getPlaylistResults", query);

        call.enqueue(new Callback<SongApiResponse>() {
            @Override
            public void onResponse(Call<SongApiResponse> call, Response<SongApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<SongApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void getAlbums(SongResponseListener listener, String query) {
        AlbumSearchApi apiInterface = retrofit.create(AlbumSearchApi.class);
        Call<SongApiResponse> call = apiInterface.getAlbums("json", 0, 4, 30, "search.getAlbumResults", query);

        call.enqueue(new Callback<SongApiResponse>() {
            @Override
            public void onResponse(Call<SongApiResponse> call, Response<SongApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<SongApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void getSelectedAlbum(AlbumResponseListener listener, String albumId) {
        AlbumApiInterface albumApiInterface = retrofit.create(AlbumApiInterface.class);
        Call<AlbumApiResponse> call = albumApiInterface.getAlbum("json", 0, "content.getAlbumDetails", albumId);

        call.enqueue(new Callback<AlbumApiResponse>() {
            @Override
            public void onResponse(Call<AlbumApiResponse> call, Response<AlbumApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<AlbumApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }


    public void getSearchedSong(SongResponseListener listener, String query) {
        SongApiInterface apiInterface = retrofit.create(SongApiInterface.class);
        Call<SongApiResponse> call = apiInterface.getSongs("json", 0, 4, 30, "search.getResults", query);

        call.enqueue(new Callback<SongApiResponse>() {
            @Override
            public void onResponse(Call<SongApiResponse> call, Response<SongApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<SongApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });

    }


}
