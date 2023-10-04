package com.taimoor.musicplayer.Service;

import static com.taimoor.musicplayer.Activities.PlayerActivity.isSaved;
import static com.taimoor.musicplayer.Activities.PlayerActivity.list;
import static com.taimoor.musicplayer.Utils.ApplicationClass.ACTION_NEXT;
import static com.taimoor.musicplayer.Utils.ApplicationClass.ACTION_PLAY;
import static com.taimoor.musicplayer.Utils.ApplicationClass.ACTION_PREVIOUS;
import static com.taimoor.musicplayer.Utils.ApplicationClass.CHANNEL_ID_2;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.taimoor.musicplayer.Activities.MainActivity;
import com.taimoor.musicplayer.Listeners.ActionPlaying;
import com.taimoor.musicplayer.Models.Song;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Utils.DataHandlers;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder myBinder = new MyBinder();
    public static MediaPlayer mediaPlayer;
    public List<Song> musicFiles = new ArrayList<>();
    Uri uri;
    public int position = -1;
    ActionPlaying actionPlaying;
    MediaSessionCompat mediaSessionCompat;
    String img, url, song, artist, duration;
    Bitmap bitmap;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";
    public static final String PLAYING_FROM = "Player";
    public static final String MUSIC_DURATION = "MUSIC DURATION";

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio");

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind", "Method");
        return myBinder;
    }


    public class MyBinder extends Binder {

        public MusicService getService() {
            return MusicService.this;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition", -1);
        url = intent.getStringExtra("url");
        if (isSaved) {
            img = intent.getStringExtra("img");
            song = intent.getStringExtra("song");
            artist = intent.getStringExtra("artist");
            duration = intent.getStringExtra("durationSeconds");
        }

        String actionName = intent.getStringExtra("ActionName");
        if (myPosition != -1) {
            playMedia(myPosition);
        }
        if (actionName != null) {
            switch (actionName) {
                case "playPause":
                    playPauseBtnClicked();
                    break;
                case "next":
                    nextBtnClicked();
                    break;
                case "previous":
                    prevBtnClicked();
                    break;
            }
        }
        return START_STICKY;
    }

    private void playMedia(int startPosition) {

        musicFiles = list;
        position = startPosition;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFiles != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }

    }

    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void createMediaPlayer(int positionInner) {
        position = positionInner;

        if (isSaved) {
            try {
                uri = Uri.parse(DataHandlers.downloadLinkDecrypt(url));
            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
            editor.putString(MUSIC_FILE, uri.toString());
            editor.putString(ARTIST_NAME,artist);
            editor.putString(SONG_NAME, song);
            editor.putString(MUSIC_DURATION, duration);
            editor.putString(PLAYING_FROM, "ONLINE");
            editor.putBoolean("isFavorite",true);
            editor.apply();

        } else {

            try {
                uri = Uri.parse(DataHandlers.downloadLinkDecrypt(musicFiles.get(position).getEncrypted_media_url()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
            editor.putString(MUSIC_FILE, uri.toString());
            editor.putString(ARTIST_NAME, musicFiles.get(position).getPrimary_artists());
            editor.putString(SONG_NAME, musicFiles.get(position).getSong());
            editor.putString(MUSIC_DURATION, musicFiles.get(position).getDuration());
            editor.putString(PLAYING_FROM, "ONLINE");
            editor.putBoolean("isFavorite",false);
            editor.apply();

        }



        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    public void onCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (actionPlaying != null) {

            actionPlaying.nextBtnClicked();
            actionPlaying.playPauseBtnClicked();
        }

        onCompleted();
    }

    public void setCallback(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }

    public void showNotification(int playPauseBtn) {

        if (!isSaved) {
            song = musicFiles.get(position).getSong();
            artist = musicFiles.get(position).getPrimary_artists();

            song = song.replace("#", "" );
            song = song.replace("&", "" );
            song = song.replace(";", "" );
            song = song.replace("039", "'" );
        }

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_IMMUTABLE);

        Intent prevIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        mediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder()
                .putLong(MediaMetadata.METADATA_KEY_DURATION, -1)
                .build());


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(playPauseBtn)
                .setLargeIcon(getAlbumArt())
                .setContentTitle(song)
                .setContentText(artist)
                .setCategory(Notification.EXTRA_MEDIA_SESSION)
                .addAction(R.drawable.skip_previous, "Previous", prevPending)
                .addAction(playPauseBtn, "Pause", pausePending)
                .addAction(R.drawable.skip_next, "Next", nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2)
                        .setShowCancelButton(false))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        startForeground(1, notification);
        //notificationManager.notify(0, notification);

    }

    Bitmap getAlbumArt() {
        if (!isSaved) {
            img = musicFiles.get(position).getImage();
        }


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
        return bitmap;
    }

    public void nextBtnClicked() {
        if (actionPlaying != null) {
            actionPlaying.nextBtnClicked();
        }
    }

    public void prevBtnClicked() {
        if (actionPlaying != null) {
            actionPlaying.prevBtnClicked();
        }
    }

    public void playPauseBtnClicked() {
        if (actionPlaying != null) {
            actionPlaying.playPauseBtnClicked();
        }
    }
}
