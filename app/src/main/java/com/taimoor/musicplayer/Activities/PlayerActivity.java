package com.taimoor.musicplayer.Activities;

import static com.taimoor.musicplayer.Activities.AlbumActivity.allSongs;
import static com.taimoor.musicplayer.Activities.MainActivity.repeatBoolean;
import static com.taimoor.musicplayer.Activities.MainActivity.shuffleBoolean;
import static com.taimoor.musicplayer.Activities.PlaylistActivity.songsList;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.taimoor.musicplayer.Adapters.AlbumAdapter;
import com.taimoor.musicplayer.Adapters.PlaylistAdapter;
import com.taimoor.musicplayer.Fragments.BottomPlayerFragment;
import com.taimoor.musicplayer.Listeners.ActionPlaying;
import com.taimoor.musicplayer.Listeners.onSongClick;
import com.taimoor.musicplayer.Models.AlbumApiResponse;
import com.taimoor.musicplayer.Models.LyricsApiResponse;
import com.taimoor.musicplayer.Models.PlaylistApiResponse;
import com.taimoor.musicplayer.Models.Song;
import com.taimoor.musicplayer.Service.MusicService;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Retrofit.AlbumResponseListener;
import com.taimoor.musicplayer.Retrofit.ApiClient;
import com.taimoor.musicplayer.Retrofit.LyricsResponseListener;
import com.taimoor.musicplayer.Retrofit.PlaylistResponseListener;
import com.taimoor.musicplayer.Utils.Common;
import com.taimoor.musicplayer.Utils.DataHandlers;
import com.taimoor.musicplayer.Utils.SharedViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class PlayerActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection, onSongClick {

    TextView songName, artistName, durationPlayed, durationTotal, lyrics;
    ImageView nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn, lyricsBtn, imgBtn, downloadBtn, songListBtn;
    LinearLayout songListContainer;
    CircleImageView coverArt;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    String url, song, artist, img, duration, durationSeconds;
    public static List<Song> list;
    Song savedSong;
    public static int position;
    static MediaPlayer mediaPlayer;
    Uri uri;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    ProgressDialog dialog;
    RelativeLayout topBar;
    Bitmap bitmap;
    public static MusicService musicService;
    SharedViewModel sharedViewModel;
    ScrollView lyricsView;
    ApiClient client;
    String hasLyrics, lyr;
    public static boolean isSaved = false;
    public static Activity onlinePlayer;
    AudioManager audioManager;
    DownloadManager downloadManager;
    ImageButton copyBtn;
    RecyclerView recyclerView;
    AlbumAdapter albumListAdapter;
    PlaylistAdapter playlistadapter;
    String albumId, activity;

    private static final int FILTERED_GREY = Color.argb(155, 185, 185, 185);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_player);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();

        isSaved = getIntent().getBooleanExtra("isSaved", false);
        savedSong = new Song();

        list = new ArrayList<>();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);


        initViews();
        getIntentMethod();

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (hasLyrics.equals("true")) {
            dialog.show();
            client.getLyrics(listener, list.get(position).id);
            copyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipboard();
                }
            });

        } else {
            lyrics.setText("No Lyrics Found :(");
            lyrics.setTextSize(28);
            copyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PlayerActivity.this, "No lyrics to copy...", Toast.LENGTH_SHORT).show();
                }
            });

        }

        if(isSaved){
            songListBtn.setVisibility(View.GONE);
        }

        songListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songListContainer.getVisibility() == View.VISIBLE) {
                    songListContainer.setVisibility(View.GONE);
                    coverArt.setVisibility(View.VISIBLE);
                    lyricsView.setVisibility(View.GONE);
                    spinningAnimation(coverArt);
                } else {
                    songListContainer.setVisibility(View.VISIBLE);
                    coverArt.setVisibility(View.GONE);
                    coverArt.clearAnimation();

                    if (activity.equals("album")) {
                        client.getSelectedAlbum(selectedListener, albumId);
                    }else if (activity.equals("playlist")){
                        client.getSelectedPlaylists(playlistListener, albumId);
                    }
                }
            }
        });
        //musicService.showNotification(R.drawable.pause_icon);

        lyricsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lyricsBtn.setVisibility(View.GONE);
                coverArt.setVisibility(View.GONE);
                coverArt.clearAnimation();
                imgBtn.setVisibility(View.VISIBLE);
                copyBtn.setVisibility(View.VISIBLE);
                lyricsView.setVisibility(View.VISIBLE);
                songListContainer.setVisibility(View.GONE);

            }
        });


        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBtn.setVisibility(View.GONE);
                lyricsView.setVisibility(View.GONE);
                copyBtn.setVisibility(View.GONE);
                coverArt.setVisibility(View.VISIBLE);
                spinningAnimation(coverArt);
                lyricsBtn.setVisibility(View.VISIBLE);
                songListContainer.setVisibility(View.GONE);


            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (musicService != null && b) {
                    musicService.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int currentPos = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPos);
                    durationPlayed.setText(formattedTime(currentPos));
                }
                handler.postDelayed(this, 1000);
            }
        });

        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shuffleBoolean) {
                    shuffleBoolean = false;
                    shuffleBtn.setImageResource(R.drawable.shuffle_off);
                } else {
                    shuffleBoolean = true;
                    shuffleBtn.setImageResource(R.drawable.shuffle_on);
                }
            }
        });

        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatBoolean) {
                    repeatBoolean = false;
                    repeatBtn.setImageResource(R.drawable.repeat_off);
                } else {
                    repeatBoolean = true;
                    repeatBtn.setImageResource(R.drawable.repeat_on);
                }
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadManager.Request request = new DownloadManager.Request(uri);

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(song)
                        .setVisibleInDownloadsUi(true)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "MusicPlayer/" + song + ".mp3");

                downloadManager.enqueue(request);

                Toast.makeText(PlayerActivity.this, "Download Started", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void copyToClipboard() {

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Copied Data", lyr);

        clipboardManager.setPrimaryClip(data);
        Toast.makeText(this, "Lyrics Copied!", Toast.LENGTH_SHORT).show();

    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);

        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);

    }

    private final LyricsResponseListener listener = new LyricsResponseListener() {
        @Override
        public void onFetch(LyricsApiResponse response, String message) {
            if (response.getLyrics().isEmpty()) {
                Toast.makeText(PlayerActivity.this, "No Songs Found", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            lyr = response.getLyrics();
            lyrics.setText(lyr.replace("<br>", "\n"));

        }

        @Override
        public void onError(String message) {
            Toast.makeText(PlayerActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };

    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isSaved) {
                            prevBtn.setClickable(false);
                            Toast.makeText(PlayerActivity.this, "Action not Possible", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.show();
                            prevBtnClicked();
                        }
                    }
                });
            }
        };
        prevThread.start();
    }

    public void prevBtnClicked() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(list.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (list.size() - 1) : (position - 1));
            }

            url = list.get(position).getEncrypted_media_url();

            try {
                uri = Uri.parse(DataHandlers.downloadLinkDecrypt(url));
            } catch (Exception e) {
                e.printStackTrace();
            }

            musicService.createMediaPlayer(position);
            metaData();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPos = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            musicService.showNotification(R.drawable.pause_icon);
            playPauseBtn.setBackgroundResource(R.drawable.pause_icon);
            musicService.start();
            dialog.dismiss();
        } else {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(list.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (list.size() - 1) : (position - 1));
            }

            url = list.get(position).getEncrypted_media_url();

            try {
                uri = Uri.parse(DataHandlers.downloadLinkDecrypt(url));
            } catch (Exception e) {
                e.printStackTrace();
            }

            musicService.createMediaPlayer(position);
            metaData();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPos = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            musicService.showNotification(R.drawable.pause_icon);
            playPauseBtn.setBackgroundResource(R.drawable.play_icon);
        }
        dialog.dismiss();
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isSaved) {
                            nextBtn.setClickable(false);
                            Toast.makeText(PlayerActivity.this, "Action not Possible", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.show();
                            nextBtnClicked();
                        }
                    }
                });
            }
        };
        nextThread.start();
    }

    public void nextBtnClicked() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(list.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % list.size());
            }

            url = list.get(position).getEncrypted_media_url();

            try {
                uri = Uri.parse(DataHandlers.downloadLinkDecrypt(url));
            } catch (Exception e) {
                e.printStackTrace();
            }

            musicService.createMediaPlayer(position);
            metaData();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPos = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            musicService.showNotification(R.drawable.pause_icon);
            playPauseBtn.setBackgroundResource(R.drawable.pause_icon);
            musicService.start();
            dialog.dismiss();
        } else {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(list.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % list.size());
            }

            url = list.get(position).getEncrypted_media_url();

            try {
                uri = Uri.parse(DataHandlers.downloadLinkDecrypt(url));
            } catch (Exception e) {
                e.printStackTrace();
            }

            musicService.createMediaPlayer(position);
            metaData();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPos = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            musicService.showNotification(R.drawable.play_icon);
            playPauseBtn.setBackgroundResource(R.drawable.play_icon);
            dialog.dismiss();
        }
    }

    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    public void playPauseBtnClicked() {

        if (musicService.isPlaying()) {
            playPauseBtn.setImageResource(R.drawable.play_icon);
            musicService.showNotification(R.drawable.play_icon);
            BottomPlayerFragment.playPauseBtn.setImageResource(R.drawable.play_icon);
            musicService.pause();
            coverArt.clearAnimation();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPos = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        } else {
            musicService.showNotification(R.drawable.pause_icon);
            playPauseBtn.setImageResource(R.drawable.pause_icon);
            BottomPlayerFragment.playPauseBtn.setImageResource(R.drawable.pause_icon);
            musicService.start();
            spinningAnimation(coverArt);
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPos = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    private int getRandom(int i) {
        Random random = new Random();

        return random.nextInt(i + 1);
    }

    private String formattedTime(int currentPos) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(currentPos % 60);
        String minutes = String.valueOf(currentPos / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void getIntentMethod() {

        if (isSaved) {

            url = getIntent().getStringExtra("url");
            position = getIntent().getIntExtra("pos", -1);
            hasLyrics = "false";

        } else {


            list = (List<Song>) getIntent().getSerializableExtra("list");
            position = getIntent().getIntExtra("pos", -1);
            url = list.get(position).getEncrypted_media_url();
            hasLyrics = list.get(position).has_lyrics;
            albumId = getIntent().getStringExtra("albumId");
            activity = getIntent().getStringExtra("activity");
        }


        try {
            uri = Uri.parse(DataHandlers.downloadLinkDecrypt(url));
        } catch (Exception e) {
            e.printStackTrace();
        }


        metaData();
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("servicePosition", position);
        intent.putExtra("url", url);
        intent.putExtra("img", img);
        intent.putExtra("song", song);
        intent.putExtra("artist", artist);
        intent.putExtra("durationSeconds", durationSeconds);
        startService(intent);


    }

    private void initViews() {
        songName = findViewById(R.id.song_name);
        songName.setSelected(true);
        recyclerView = findViewById(R.id.song_list_recycler);
        artistName = findViewById(R.id.artist_name);
        artistName.setSelected(true);
        durationPlayed = findViewById(R.id.duration_played);
        durationTotal = findViewById(R.id.duration_total);
        coverArt = findViewById(R.id.cover_art);
        nextBtn = findViewById(R.id.next_btn);
        prevBtn = findViewById(R.id.prev_btn);
        backBtn = findViewById(R.id.back_btn);
        shuffleBtn = findViewById(R.id.shuffle_btn);
        repeatBtn = findViewById(R.id.repeat_btn);
        playPauseBtn = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seek_bar);
        lyrics = findViewById(R.id.lyrics);
        lyricsBtn = findViewById(R.id.lyrics_btn);
        imgBtn = findViewById(R.id.img_btn);
        downloadBtn = findViewById(R.id.download_btn);
        songListBtn = findViewById(R.id.song_list_btn);
        lyricsView = findViewById(R.id.lyrics_scrollView);
        copyBtn = findViewById(R.id.copyBtn);
        topBar = findViewById(R.id.top_btn);
        songListContainer = findViewById(R.id.song_list_container);

        client = new ApiClient(this);


    }

    private void metaData() {

        if (isSaved) {
            song = getIntent().getStringExtra("song");
            artist = getIntent().getStringExtra("artist");
            img = getIntent().getStringExtra("image");
            duration = getIntent().getStringExtra("duration");
            durationSeconds = getIntent().getStringExtra("durationSeconds");

            savedSong.setSong(song);

        } else {

            song = list.get(position).getSong();
            artist = list.get(position).getPrimary_artists();
            img = list.get(position).getImage();
            duration = Common.convertSecondsToTime(Integer.parseInt(list.get(position).getDuration()));
        }

        song = song.replace("#", "");
        song = song.replace("&", "");
        song = song.replace(";", "");

        Picasso.get().load(img).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitImg, Picasso.LoadedFrom from) {
                bitmap = bitImg;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        if (img != null) {

            ImageAnimation(this, coverArt, bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {

                    Palette.Swatch swatch = palette.getDominantSwatch();
                    ImageView gradient = findViewById(R.id.image_view_gradient);
                    RelativeLayout container = findViewById(R.id.player_container);
                    gradient.setBackgroundResource(R.drawable.gradient_bg);
                    container.setBackgroundResource(R.drawable.main_bg);
                    GradientDrawable gradientDrawable;

                    if (swatch != null) {

                        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), 0x00000000});
                        gradient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), swatch.getRgb()});
                        container.setBackground(gradientDrawableBg);

                        songName.setTextColor(swatch.getTitleTextColor());
                        artistName.setTextColor(swatch.getBodyTextColor());
                        lyrics.setTextColor(swatch.getBodyTextColor());
                        durationPlayed.setTextColor(swatch.getBodyTextColor());
                        durationTotal.setTextColor(swatch.getBodyTextColor());
                        copyBtn.setColorFilter(swatch.getTitleTextColor());
                    } else {

                        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0x00000000});
                        gradient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0xff000000});
                        container.setBackground(gradientDrawableBg);

                        songName.setTextColor(Color.WHITE);
                        artistName.setTextColor(Color.DKGRAY);
                        lyrics.setTextColor(Color.WHITE);
                    }
                }
            });
        } else {

            ImageView gradient = findViewById(R.id.image_view_gradient);
            RelativeLayout container = findViewById(R.id.player_container);
            gradient.setBackgroundResource(R.drawable.gradient_bg);
            container.setBackgroundResource(R.drawable.main_bg);

            songName.setTextColor(Color.WHITE);
            artistName.setTextColor(Color.DKGRAY);


        }

        songName.setText(song);
        artistName.setText(artist);
        durationTotal.setText(duration);


    }

    public void ImageAnimation(Context context, ImageView imageView, Bitmap bitmap) {

        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);


        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animIn);
                spinningAnimation(coverArt);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);
        //  dialog.dismiss();
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallback(this);
        seekBar.setMax(musicService.getDuration() / 1000);
        musicService.onCompleted();
        musicService.showNotification(R.drawable.pause_icon);

        dialog.dismiss();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }

    private AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    switch (focusChange) {

                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                            // Lower the volume while ducking.
                            mediaPlayer.setVolume(0.2f, 0.2f);
                            break;
                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                            musicService.pause();
                            break;

                        case (AudioManager.AUDIOFOCUS_LOSS):
                            musicService.stop();
                            ComponentName component = new ComponentName(PlayerActivity.this, MusicService.class);
                            am.unregisterMediaButtonEventReceiver(component);
                            break;

                        case (AudioManager.AUDIOFOCUS_GAIN):
                            // Return the volume to normal and resume if paused.
                            mediaPlayer.setVolume(1f, 1f);
                            musicService.start();
                            break;
                        default:
                            break;
                    }
                }
            };

    private void spinningAnimation(ImageView imageView) {

        //Spinning Image Animation
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(30000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        imageView.startAnimation(rotateAnimation);

    }

    private final AlbumResponseListener selectedListener = new AlbumResponseListener() {
        @Override
        public void onFetch(AlbumApiResponse response, String message) {
            if (response.getSongs().isEmpty()) {
                Toast.makeText(PlayerActivity.this, "No Songs Found", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            showData(response.getSongs());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(PlayerActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };

    private void showData(List<Song> songs) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        albumListAdapter = new AlbumAdapter(this, songs, this);
        recyclerView.setAdapter(albumListAdapter);

    }

    private final PlaylistResponseListener playlistListener = new PlaylistResponseListener() {
        @Override
        public void onFetch(PlaylistApiResponse response, String message) {
            if (response.getSongs().isEmpty()) {
                Toast.makeText(PlayerActivity.this, "No Songs Found", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            showPlaylistData(response.getSongs(), response);
        }

        @Override
        public void onError(String message) {
            Toast.makeText(PlayerActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };


    private void showPlaylistData(List<Song> songs, PlaylistApiResponse response) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        playlistadapter = new PlaylistAdapter(this, songs, this);
        recyclerView.setAdapter(playlistadapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSongItemClick(int position) {
        finish();
        Intent intent = new Intent(PlayerActivity.this, PlayerActivity.class);

        if (activity.equals("album")){
            intent.putExtra("pos", position);
            intent.putExtra("list", (Serializable) allSongs);
            intent.putExtra("albumId", albumId);
            intent.putExtra("activity","album");
        }else if (activity.equals("playlist")){
            intent.putExtra("pos", position);
            intent.putExtra("list", (Serializable) songsList);
            intent.putExtra("albumId", albumId);
            intent.putExtra("activity","playlist");
        }

        startActivity(intent);

    }
}