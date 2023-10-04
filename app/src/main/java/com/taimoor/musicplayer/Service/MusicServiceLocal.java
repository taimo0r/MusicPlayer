package com.taimoor.musicplayer.Service;

import static com.taimoor.musicplayer.Activities.PlayerActivityLocal.listSongs;
import static com.taimoor.musicplayer.Utils.ApplicationClass.ACTION_NEXT;
import static com.taimoor.musicplayer.Utils.ApplicationClass.ACTION_PLAY;
import static com.taimoor.musicplayer.Utils.ApplicationClass.ACTION_PREVIOUS;
import static com.taimoor.musicplayer.Utils.ApplicationClass.CHANNEL_ID_1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.taimoor.musicplayer.Activities.MainActivity;
import com.taimoor.musicplayer.Listeners.ActionPlaying;
import com.taimoor.musicplayer.Models.MusicFiles;
import com.taimoor.musicplayer.R;

import java.util.ArrayList;

public class MusicServiceLocal extends Service implements MediaPlayer.OnCompletionListener {

    IBinder myBinder = new MyBinder();
    public static MediaPlayer mediaPlayer;
    public ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    Uri uri;
    public int position = -1;
    ActionPlaying actionPlaying;
    MediaSessionCompat mediaSessionCompat;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";
    public static final String PLAYING_FROM = "Player";
    public static final String MUSIC_DURATION = "MUSIC DURATION";

    @Override
    public void onCreate() {
        super.onCreate();
        musicFiles = listSongs;
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    public class MyBinder extends Binder {

        public MusicServiceLocal getService() {
            return MusicServiceLocal.this;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int myPosition = intent.getIntExtra("servicePosition", -1);
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

        return START_REDELIVER_INTENT;
    }

    private void playMedia(int startPosition) {
        musicFiles = listSongs;
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
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void createMediaPlayer(int positionInner) {
        position = positionInner;
        uri = Uri.parse(musicFiles.get(position).getPath());
        SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
        editor.putString(MUSIC_FILE, uri.toString());
        editor.putString(ARTIST_NAME,musicFiles.get(position).getArtist());
        editor.putString(SONG_NAME, musicFiles.get(position).getTitle());
        editor.putString(MUSIC_DURATION, musicFiles.get(position).getDuration());
        editor.putString(PLAYING_FROM, "LOCAL");
        editor.apply();
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    public void onCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
//        if (actionPlaying!=null){
//            actionPlaying.nextBtnClicked();
//            if (mediaPlayer!=null){
//                createMediaPlayer(position);
//                mediaPlayer.start();
//                onCompleted();
//            }
//        }


        if (actionPlaying != null) {


            actionPlaying.nextBtnClicked();
            //actionPlaying.playPauseBtnClicked();
        }

        onCompleted();
    }

    public void setCallback(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;

    }

    public void showNotification(int playPauseBtn) {

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_IMMUTABLE);

        Intent prevIntent = new Intent(this, NotificationReceiverLocal.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, NotificationReceiverLocal.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent pauseIntent = new Intent(this, NotificationReceiverLocal.class)
                .setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        byte[] picture = null;
        picture = getAlbumArt(musicFiles.get(position).getPath());
        Bitmap thumb = null;
        if (picture != null) {
            thumb = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        } else {
            thumb = BitmapFactory.decodeResource(getResources(), R.drawable.music_img);
        }

        mediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder()
                .putLong(MediaMetadata.METADATA_KEY_DURATION, -1)
                .build());

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setSmallIcon(playPauseBtn)
                .setLargeIcon(thumb)
                .setContentTitle(musicFiles.get(position).getTitle())
                .setContentText(musicFiles.get(position).getArtist())
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

        startForeground(1,notification);

    }



    private byte[] getAlbumArt(String uri) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        return art;

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
