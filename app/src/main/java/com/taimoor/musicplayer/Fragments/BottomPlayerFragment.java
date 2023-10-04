package com.taimoor.musicplayer.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.taimoor.musicplayer.Activities.MainActivity.ARTIST_TO_FRAG;
import static com.taimoor.musicplayer.Activities.MainActivity.DURATION_TO_FRAG;
import static com.taimoor.musicplayer.Activities.MainActivity.IMAGE_TO_FRAG;
import static com.taimoor.musicplayer.Activities.MainActivity.IS_FAV;
import static com.taimoor.musicplayer.Activities.MainActivity.PATH_TO_FRAG;
import static com.taimoor.musicplayer.Activities.MainActivity.SHOW_MINI_PLAYER;
import static com.taimoor.musicplayer.Activities.MainActivity.SONG_TO_FRAG;

import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Service.MusicService;
import com.taimoor.musicplayer.Service.MusicServiceLocal;
import com.taimoor.musicplayer.Utils.DataHandlers;

public class BottomPlayerFragment extends Fragment implements ServiceConnection {

    ImageView nextBtn, prevBtn, closeBtn, downloadBtn;
    ShapeableImageView albumArt;
    TextView artistName, songName;
    public static FloatingActionButton playPauseBtn;
    public static boolean IS_PLAYING = false;
    MusicServiceLocal musicService;
    MusicService musicServiceOnline;
    String player = "null";
    Uri uri;
    ProgressBar progressLayout;
    Context context;
    private Handler handler = new Handler();

    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";
    public static final String IMAGE_LINK = "IMAGE LINK";
    public static final String MUSIC_DURATION = "MUSIC DURATION";
    public static String PLAYING_FROM = "Player";


    public BottomPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_player, container, false);

        context = getContext();

        artistName = view.findViewById(R.id.artist_name_miniPlayer);
        artistName.setSelected(true);
        songName = view.findViewById(R.id.song_name_miniPlayer);
        songName.setSelected(true);
//        closeBtn = view.findViewById(R.id.close_bottom);
        downloadBtn = view.findViewById(R.id.download_bottom);
        nextBtn = view.findViewById(R.id.skip_next_bottom);
        prevBtn = view.findViewById(R.id.skip_prev_bottom);
        playPauseBtn = view.findViewById(R.id.play_pause_button_miniPlayer);
        albumArt = view.findViewById(R.id.bottom_album_art);
        progressLayout = (ProgressBar) view.findViewById(R.id.progress_bar_horizontal);


        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadManager.Request request = new DownloadManager.Request(uri);

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(SONG_TO_FRAG)
                        .setVisibleInDownloadsUi(true)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "MusicPlayer/" + SONG_TO_FRAG + ".mp3");

                downloadManager.enqueue(request);

                Toast.makeText(getContext(), "Download Started", Toast.LENGTH_LONG).show();
            }
        });

//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((MainActivity) getActivity()).stopMusic();
//                ((MainActivity) getActivity()).hideMiniPlayer();
//            }
//        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (player.equals("ONLINE")) {
                    if (musicServiceOnline != null) {

                        int currentPos = musicServiceOnline.getCurrentPosition() / 1000;
                        progressLayout.setProgress(currentPos);
                    }
                } else {
                    if (musicService != null) {
                        int currentPos = musicService.getCurrentPosition() / 1000;
                        progressLayout.setProgress(currentPos);
                    }
                }
                handler.postDelayed(this, 1000);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (IS_FAV) {
                    nextBtn.setClickable(false);
                    Toast.makeText(getContext(), "Action Not Possible", Toast.LENGTH_SHORT).show();
                } else {
                    nextBtnClicked();
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (IS_FAV) {
                    prevBtn.setClickable(false);
                    Toast.makeText(getContext(), "Action Not Possible", Toast.LENGTH_SHORT).show();
                } else {
                    prevBtnClicked();
                }

            }
        });


        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (player.equals("ONLINE")) {
                    if (musicServiceOnline != null) {
                        musicServiceOnline.playPauseBtnClicked();

                        int currentPos = musicServiceOnline.getCurrentPosition() / 1000;
                        progressLayout.setProgress(currentPos);

                        if (musicServiceOnline.isPlaying()) {
                            IS_PLAYING = true;
                            playPauseBtn.setImageResource(R.drawable.pause_icon);
                        } else {
                            IS_PLAYING = false;
                            playPauseBtn.setImageResource(R.drawable.play_icon);
                        }
                    }
                } else {
                    if (musicService != null) {
                        musicService.playPauseBtnClicked();

                        int currentPos = musicService.getCurrentPosition() / 1000;
                        progressLayout.setProgress(currentPos);

                        if (musicService.isPlaying()) {
                            IS_PLAYING = true;
                            playPauseBtn.setImageResource(R.drawable.pause_icon);
                        } else {
                            IS_PLAYING = false;
                            playPauseBtn.setImageResource(R.drawable.play_icon);
                        }
                    }
                }
            }
        });

        SharedPreferences prefs = getContext().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
        player = prefs.getString(PLAYING_FROM, "");

        setResources();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getContext().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
        player = prefs.getString(PLAYING_FROM, "");

        if (player.equals("ONLINE")) {

            downloadBtn.setVisibility(View.VISIBLE);

            if (SHOW_MINI_PLAYER) {
                if (PATH_TO_FRAG != null) {

                    uri = Uri.parse(PATH_TO_FRAG);
                    Glide.with(getContext()).load(IMAGE_TO_FRAG).placeholder(R.drawable.music_img).into(albumArt);
                    songName.setText(SONG_TO_FRAG);
                    artistName.setText(ARTIST_TO_FRAG);
                    progressLayout.setMax(Integer.parseInt(DURATION_TO_FRAG));

                    Intent intent = new Intent(getContext(), MusicService.class);
                    if (getContext() != null) {
                        getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
                    }
                }
            }
        } else {
            downloadBtn.setVisibility(View.GONE);

            if (SHOW_MINI_PLAYER) {
                if (PATH_TO_FRAG != null) {
                    byte[] art = getAlbumArt(PATH_TO_FRAG);

                    Glide.with(getContext()).load(art).placeholder(R.drawable.music_img).into(albumArt);
                    songName.setText(SONG_TO_FRAG);
                    artistName.setText(ARTIST_TO_FRAG);
                    progressLayout.setMax(Integer.parseInt(DURATION_TO_FRAG) / 1000);

                    Intent intent = new Intent(getContext(), MusicServiceLocal.class);
                    if (getContext() != null) {
                        getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
                    }
                }
            }
        }
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (getContext() != null) {
//            getContext().unbindService(this);
//        }
//    }

    private void nextBtnClicked() {

        if (player.equals("ONLINE")) {
            if (musicServiceOnline != null) {
                musicServiceOnline.nextBtnClicked();

                if (getActivity() != null) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                    try {
                        editor.putString(MUSIC_FILE, DataHandlers.downloadLinkDecrypt(musicServiceOnline.musicFiles.get(musicServiceOnline.position).getEncrypted_media_url()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editor.putString(ARTIST_NAME, musicServiceOnline.musicFiles.get(musicServiceOnline.position).getPrimary_artists());
                    editor.putString(SONG_NAME, musicServiceOnline.musicFiles.get(musicServiceOnline.position).getSong());
                    editor.putString(IMAGE_LINK, musicServiceOnline.musicFiles.get(musicServiceOnline.position).getImage());
                    editor.putString(MUSIC_DURATION, musicServiceOnline.musicFiles.get(musicServiceOnline.position).getDuration());


                    editor.apply();

                    int currentPos = musicServiceOnline.getCurrentPosition() / 1000;
                    progressLayout.setMax(musicServiceOnline.getDuration() / 1000);
                    progressLayout.setProgress(currentPos);

                    SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
                    String path = preferences.getString(MUSIC_FILE, null);
                    String artist = preferences.getString(ARTIST_NAME, null);
                    String song = preferences.getString(SONG_NAME, null);
                    String img = preferences.getString(IMAGE_LINK, null);
                    String dur = preferences.getString(MUSIC_DURATION, null);
                    boolean isFav = preferences.getBoolean("isFavorite", false);

                    if (path != null) {
                        SHOW_MINI_PLAYER = true;
                        PATH_TO_FRAG = path;
                        uri = Uri.parse(path);
                        ARTIST_TO_FRAG = artist;
                        SONG_TO_FRAG = song;
                        IMAGE_TO_FRAG = img;
                        DURATION_TO_FRAG = dur;
                        IS_FAV = isFav;
                    } else {
                        SHOW_MINI_PLAYER = false;
                        PATH_TO_FRAG = null;
                        ARTIST_TO_FRAG = null;
                        uri = null;
                        SONG_TO_FRAG = null;
                        IMAGE_TO_FRAG = null;
                        DURATION_TO_FRAG = null;
                        IS_FAV = false;
                    }

                    if (musicServiceOnline.isPlaying()) {
                        playPauseBtn.setImageResource(R.drawable.pause_icon);
                        IS_PLAYING = true;
                    } else {
                        playPauseBtn.setImageResource(R.drawable.play_icon);
                        IS_PLAYING = false;
                    }

                }

                if (SHOW_MINI_PLAYER) {
                    if (PATH_TO_FRAG != null) {
                        Glide.with(getContext()).load(IMAGE_TO_FRAG).placeholder(R.drawable.music_img).into(albumArt);
                        songName.setText(SONG_TO_FRAG);
                        artistName.setText(ARTIST_TO_FRAG);
                    }
                }


            }
        } else {
            if (musicService != null) {
                musicService.nextBtnClicked();

                if (getActivity() != null) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                    editor.putString(MUSIC_FILE, musicService.musicFiles.get(musicService.position).getPath());
                    editor.putString(ARTIST_NAME, musicService.musicFiles.get(musicService.position).getArtist());
                    editor.putString(SONG_NAME, musicService.musicFiles.get(musicService.position).getTitle());
                    editor.putString(MUSIC_DURATION, musicService.musicFiles.get(musicService.position).getDuration());

                    editor.apply();

                    int currentPos = musicService.getCurrentPosition() / 1000;
                    progressLayout.setMax(musicService.getDuration() / 1000);
                    progressLayout.setProgress(currentPos);

                    SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
                    String path = preferences.getString(MUSIC_FILE, null);
                    String artist = preferences.getString(ARTIST_NAME, null);
                    String song = preferences.getString(SONG_NAME, null);

                    if (path != null) {
                        SHOW_MINI_PLAYER = true;
                        PATH_TO_FRAG = path;
                        ARTIST_TO_FRAG = artist;
                        SONG_TO_FRAG = song;
                    } else {
                        SHOW_MINI_PLAYER = false;
                        PATH_TO_FRAG = null;
                        ARTIST_TO_FRAG = null;
                        SONG_TO_FRAG = null;
                    }

                    if (musicService.isPlaying()) {
                        playPauseBtn.setImageResource(R.drawable.pause_icon);
                        IS_PLAYING = true;
                    } else {
                        playPauseBtn.setImageResource(R.drawable.play_icon);
                        IS_PLAYING = false;
                    }

                }

                if (SHOW_MINI_PLAYER) {
                    if (PATH_TO_FRAG != null) {
                        byte[] art = getAlbumArt(PATH_TO_FRAG);
                        Glide.with(getContext()).load(art).placeholder(R.drawable.music_img).into(albumArt);
                        songName.setText(SONG_TO_FRAG);
                        artistName.setText(ARTIST_TO_FRAG);
                    }
                }
            }
        }
    }

    private void prevBtnClicked() {
        if (player.equals("ONLINE")) {
            if (musicServiceOnline != null) {
                musicServiceOnline.prevBtnClicked();

                if (getActivity() != null) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                    try {
                        editor.putString(MUSIC_FILE, DataHandlers.downloadLinkDecrypt(musicServiceOnline.musicFiles.get(musicServiceOnline.position).getEncrypted_media_url()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editor.putString(ARTIST_NAME, musicServiceOnline.musicFiles.get(musicServiceOnline.position).getPrimary_artists());
                    editor.putString(SONG_NAME, musicServiceOnline.musicFiles.get(musicServiceOnline.position).getSong());
                    editor.putString(IMAGE_LINK, musicServiceOnline.musicFiles.get(musicServiceOnline.position).getImage());
                    editor.putString(MUSIC_DURATION, musicServiceOnline.musicFiles.get(musicServiceOnline.position).getDuration());
                    editor.apply();

                    SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
                    String path = preferences.getString(MUSIC_FILE, null);
                    String artist = preferences.getString(ARTIST_NAME, null);
                    String song = preferences.getString(SONG_NAME, null);
                    String img = preferences.getString(IMAGE_LINK, null);
                    boolean isFav = preferences.getBoolean("isFavorite", false);

                    int currentPos = musicServiceOnline.getCurrentPosition() / 1000;
                    progressLayout.setMax(musicServiceOnline.getDuration() / 1000);
                    progressLayout.setProgress(currentPos);

                    if (path != null) {
                        SHOW_MINI_PLAYER = true;
                        PATH_TO_FRAG = path;
                        uri = Uri.parse(path);
                        ARTIST_TO_FRAG = artist;
                        SONG_TO_FRAG = song;
                        IMAGE_TO_FRAG = img;
                        IS_FAV = isFav;
                    } else {
                        SHOW_MINI_PLAYER = false;
                        PATH_TO_FRAG = null;
                        ARTIST_TO_FRAG = null;
                        uri = null;
                        SONG_TO_FRAG = null;
                        IMAGE_TO_FRAG = null;
                        IS_FAV = false;
                    }

                    if (musicServiceOnline.isPlaying()) {
                        playPauseBtn.setImageResource(R.drawable.pause_icon);
                        IS_PLAYING = true;
                    } else {
                        playPauseBtn.setImageResource(R.drawable.play_icon);
                        IS_PLAYING = false;
                    }

                }

                if (SHOW_MINI_PLAYER) {
                    if (PATH_TO_FRAG != null) {
                        Glide.with(getContext()).load(IMAGE_TO_FRAG).placeholder(R.drawable.music_img).into(albumArt);
                        songName.setText(SONG_TO_FRAG);
                        artistName.setText(ARTIST_TO_FRAG);
                    }
                }


            }
        } else {
            if (musicService != null) {
                musicService.prevBtnClicked();

                if (getActivity() != null) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                    editor.putString(MUSIC_FILE, musicService.musicFiles.get(musicService.position).getPath());
                    editor.putString(ARTIST_NAME, musicService.musicFiles.get(musicService.position).getArtist());
                    editor.putString(SONG_NAME, musicService.musicFiles.get(musicService.position).getTitle());
                    editor.putString(SONG_NAME, musicService.musicFiles.get(musicService.position).getDuration());
                    editor.apply();

                    int currentPos = musicService.getCurrentPosition() / 1000;
                    progressLayout.setMax(musicService.getDuration() / 1000);
                    progressLayout.setProgress(currentPos);

                    SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
                    String path = preferences.getString(MUSIC_FILE, null);
                    String artist = preferences.getString(ARTIST_NAME, null);
                    String song = preferences.getString(SONG_NAME, null);

                    if (path != null) {
                        SHOW_MINI_PLAYER = true;
                        PATH_TO_FRAG = path;
                        ARTIST_TO_FRAG = artist;
                        SONG_TO_FRAG = song;
                    } else {
                        SHOW_MINI_PLAYER = false;
                        PATH_TO_FRAG = null;
                        ARTIST_TO_FRAG = null;
                        SONG_TO_FRAG = null;
                    }

                    if (musicService.isPlaying()) {
                        playPauseBtn.setImageResource(R.drawable.pause_icon);
                        IS_PLAYING = true;
                    } else {
                        playPauseBtn.setImageResource(R.drawable.play_icon);
                        IS_PLAYING = false;
                    }

                }

                if (SHOW_MINI_PLAYER) {
                    if (PATH_TO_FRAG != null) {
                        byte[] art = getAlbumArt(PATH_TO_FRAG);
                        Glide.with(getContext()).load(art).placeholder(R.drawable.music_img).into(albumArt);
                        songName.setText(SONG_TO_FRAG);
                        artistName.setText(ARTIST_TO_FRAG);
                    }
                }


            }
        }
    }

    private void setResources() {
        if (player.equals("ONLINE")) {

            downloadBtn.setVisibility(View.VISIBLE);

            if (SHOW_MINI_PLAYER) {
                if (PATH_TO_FRAG != null) {
                    uri = Uri.parse(PATH_TO_FRAG);
                    Glide.with(getContext()).load(IMAGE_TO_FRAG).placeholder(R.drawable.music_img).into(albumArt);
                    songName.setText(SONG_TO_FRAG);
                    artistName.setText(ARTIST_TO_FRAG);


                    Intent intent = new Intent(getContext(), MusicService.class);
                    if (getContext() != null) {
                        getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
                    }
                }
            }
        } else {
            downloadBtn.setVisibility(View.GONE);

            if (SHOW_MINI_PLAYER) {
                if (PATH_TO_FRAG != null) {
                    byte[] art = getAlbumArt(PATH_TO_FRAG);
                    Glide.with(getContext()).load(art).placeholder(R.drawable.music_img).into(albumArt);
                    songName.setText(SONG_TO_FRAG);
                    artistName.setText(ARTIST_TO_FRAG);

                    Intent intent = new Intent(getContext(), MusicServiceLocal.class);
                    if (getContext() != null) {
                        getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
                    }
                }
            }

        }
    }

    private byte[] getAlbumArt(String uri) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        return art;

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {

        if (player.equals("ONLINE")) {
            MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
            musicServiceOnline = myBinder.getService();
        } else {
            MusicServiceLocal.MyBinder binder = (MusicServiceLocal.MyBinder) service;
            musicService = binder.getService();
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        //musicService = null;
    }

}