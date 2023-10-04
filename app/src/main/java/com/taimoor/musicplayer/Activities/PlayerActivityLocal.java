package com.taimoor.musicplayer.Activities;

import static com.taimoor.musicplayer.Activities.LocalSongs.musicFiles;
import static com.taimoor.musicplayer.Activities.MainActivity.repeatBoolean;
import static com.taimoor.musicplayer.Activities.MainActivity.shuffleBoolean;
import static com.taimoor.musicplayer.Utils.Common.formattedTime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taimoor.musicplayer.Fragments.BottomPlayerFragment;
import com.taimoor.musicplayer.Listeners.ActionPlaying;
import com.taimoor.musicplayer.Models.MusicFiles;
import com.taimoor.musicplayer.Service.MusicServiceLocal;
import com.taimoor.musicplayer.R;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivityLocal extends AppCompatActivity implements  ActionPlaying, ServiceConnection {

    TextView songName, artistName, durationPlayed, durationTotal, lyrics;
    ImageView coverArt, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn, lyricsBtn, imgBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    public static int position;
    ScrollView lyricsView;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    public static MusicServiceLocal musicService;

    public static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_player_local);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        audioManager.requestAudioFocus(focusChangeListener,AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

//
//        if (MusicService.mediaPlayer.isPlaying()){
//
//            Intent intent = new Intent();
//            intent.setClass(PlayerActivityLocal.this, MusicService.class);
//            stopService(intent);
//
//            MusicService.mediaPlayer.stop();
//            MusicService.mediaPlayer.release();
//        }

        initViews();
        getIntentMethod();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        lyrics.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(PlayerActivityLocal.this, "No Lyrics to copy", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        lyricsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyricsBtn.setVisibility(View.GONE);
                coverArt.setVisibility(View.GONE);
                imgBtn.setVisibility(View.VISIBLE);
                lyricsView.setVisibility(View.VISIBLE);

            }
        });


        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBtn.setVisibility(View.GONE);
                lyricsView.setVisibility(View.GONE);
                coverArt.setVisibility(View.VISIBLE);
                lyricsBtn.setVisibility(View.VISIBLE);


            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (PlayerActivityLocal.this.musicService != null && b) {
                    PlayerActivityLocal.this.musicService.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivityLocal.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (PlayerActivityLocal.this.musicService != null) {
                    int currentPos = PlayerActivityLocal.this.musicService.getCurrentPosition() / 1000;

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


    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {

        Intent intent = new Intent(this, MusicServiceLocal.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        intent.putExtra("servicePosition", position);
        getApplicationContext().startService(intent);

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



    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //     dialog.show();
                        prevBtnClicked();
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
                position = getRandom(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }

            uri = Uri.parse(listSongs.get(position).getPath());

            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());

            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivityLocal.this.runOnUiThread(new Runnable() {
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
        } else {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }

            uri = Uri.parse(listSongs.get(position).getPath());


            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());

            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivityLocal.this.runOnUiThread(new Runnable() {
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
            musicService.start();
        }
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //           dialog.show();
                        nextBtnClicked();
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
                position = getRandom(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }

            uri = Uri.parse(listSongs.get(position).getPath());


            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());

            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivityLocal.this.runOnUiThread(new Runnable() {
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
        } else {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }

            uri = Uri.parse(listSongs.get(position).getPath());


            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());

            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivityLocal.this.runOnUiThread(new Runnable() {
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
            musicService.start();
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

            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivityLocal.this.runOnUiThread(new Runnable() {
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
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivityLocal.this.runOnUiThread(new Runnable() {
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




    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        listSongs = musicFiles;

        if (listSongs != null) {

            playPauseBtn.setImageResource(R.drawable.pause_icon);
            uri = Uri.parse(listSongs.get(position).getPath());
        }

        Intent intent = new Intent(this, MusicServiceLocal.class);
        intent.putExtra("servicePosition", position);
        getApplicationContext().startService(intent);

    }

    private void initViews() {
        songName = findViewById(R.id.song_name);
        artistName = findViewById(R.id.artist_name);
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
        lyricsView = findViewById(R.id.lyrics_scrollView);

    }

    private void metaData(Uri uri) {
        int duration_total = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        durationTotal.setText(formattedTime(duration_total));

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());

        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap = null;
        if (art != null) {
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(animOut);

    }

//    @Override
//    public void onCompletion(MediaPlayer mediaPlayer) {
//
//
//
//
////        if (mediaPlayer != null) {
////            nextBtnClicked();
////         //   playPauseBtnClicked();
////            mediaPlayer.setOnCompletionListener(this);
////        }
//    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {

        MusicServiceLocal.MyBinder myBinder = (MusicServiceLocal.MyBinder) service;
        musicService = myBinder.getService();

        musicService.setCallback(this);

        seekBar.setMax(musicService.getDuration() / 1000);
        metaData(uri);

        songName.setText(listSongs.get(position).getTitle());
        artistName.setText(listSongs.get(position).getArtist());
        musicService.onCompleted();
        musicService.showNotification(R.drawable.pause_icon);

    }

    private AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    AudioManager am =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    switch (focusChange) {

//                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) :
//                           // Lower the volume while ducking.
//                            mediaPlayer.setVolume(0.2f, 0.2f);
//                            break;
                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) :
                            musicService.pause();
                            break;

                        case (AudioManager.AUDIOFOCUS_LOSS) :
                            musicService.stop();
//                            ComponentName component =new ComponentName(AudioPlayerActivity.this,MediaControlReceiver.class);
//                            am.unregisterMediaButtonEventReceiver(component);
                            break;

                        case (AudioManager.AUDIOFOCUS_GAIN) :
                            // Return the volume to normal and resume if paused.
                            //mediaPlayer.setVolume(1f, 1f);
                            musicService.start();
                            break;
                        default: break;
                    }
                }
            };

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (!musicService.isPlaying()){
//            Intent intent = new Intent(PlayerActivityLocal.this, MusicServiceLocal.class);
//            musicService.stopService(intent);
//            Toast.makeText(PlayerActivityLocal.this, "bye" ,Toast.LENGTH_SHORT).show();
//        }
    }
}