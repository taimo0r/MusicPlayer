package com.taimoor.musicplayer.Activities;

import static com.taimoor.musicplayer.Activities.PlayerActivityLocal.musicService;
import static com.taimoor.musicplayer.Utils.ApplicationClass.shouldExecuteOnResume;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.taimoor.musicplayer.Fragments.BottomPlayerFragment;
import com.taimoor.musicplayer.Models.MusicFiles;
import com.taimoor.musicplayer.R;

import java.util.ArrayList;

public class LocalSongs extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView noSongsTv;
    public static final int REQUEST_CODE = 1;
    public static ArrayList<MusicFiles> musicFiles;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";
    public static final String IMAGE_LINK = "IMAGE LINK";
    public static final String MUSIC_DURATION = "MUSIC DURATION";
    public static final String PLAYING_FROM = "Player";
    public static boolean SHOW_MINI_PLAYER = false;
    public static boolean IS_FAV = false;
    public static String SONG_TO_FRAG = "SONG";
    public static String ARTIST_TO_FRAG = "ARTIST";
    public static String PATH_TO_FRAG = null;
    public static String IMAGE_TO_FRAG = null;
    public static String DURATION_TO_FRAG = null;
    SwipeDismissBehavior swipeDismissBehavior;
    CoordinatorLayout layoutContainer;
    FrameLayout fragBottomPlayer;
    String player;
    AudioManager am;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_songs);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Downloaded Songs");
        }

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        noSongsTv = findViewById(R.id.no_songs_tv);
        layoutContainer = findViewById(R.id.frame_layout_container);
        fragBottomPlayer = findViewById(R.id.frag_bottom_player);


        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        swipeDismissBehavior = new SwipeDismissBehavior();


        bottomNavigationView.setSelectedItemId(R.id.local_activity);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.online_activity:
                        Intent onlineIntent = new Intent(LocalSongs.this, MainActivity.class);
                        startActivity(onlineIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.search_activity:
                        Intent searchIntent = new Intent(LocalSongs.this, SearchActivity.class);
                        startActivity(searchIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.local_activity:
                        return true;
                    case R.id.favorite_activity:
                        Intent favIntent = new Intent(LocalSongs.this, FavoritedSongs.class);
                        startActivity(favIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }

                return false;
            }
        });

        runtimePermission();

        if (SHOW_MINI_PLAYER) {
            layoutContainer.setVisibility(View.VISIBLE);
        } else {
            layoutContainer.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        showMiniPlayer();

    }

    private void showMiniPlayer() {

        if (shouldExecuteOnResume) {

            SharedPreferences preferences = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
            String path = preferences.getString(MUSIC_FILE, null);
            String artist = preferences.getString(ARTIST_NAME, null);
            String song = preferences.getString(SONG_NAME, null);
            String img = preferences.getString(IMAGE_LINK, null);
            player = preferences.getString(PLAYING_FROM, "");
            boolean isFav = preferences.getBoolean("isFavorite", false);
            String duration = preferences.getString(MUSIC_DURATION, null);

            if (path != null) {
                SHOW_MINI_PLAYER = true;
                PATH_TO_FRAG = path;
                ARTIST_TO_FRAG = artist;
                SONG_TO_FRAG = song;
                IMAGE_TO_FRAG = img;
                DURATION_TO_FRAG = duration;
                IS_FAV = isFav;
            } else {
                SHOW_MINI_PLAYER = false;
                PATH_TO_FRAG = null;
                ARTIST_TO_FRAG = null;
                SONG_TO_FRAG = null;
                IMAGE_TO_FRAG = null;
                DURATION_TO_FRAG = null;
            }

            SHOW_MINI_PLAYER = !player.equals("Null") && !player.equals("");

            implementSwipeDismiss();

            swipeDismissBehavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
                @Override
                public void onDismiss(View view) {

                    stopMusic();

                }

                @Override
                public void onDragStateChanged(int state) {

                }
            });


            if (SHOW_MINI_PLAYER) {
                layoutContainer.setVisibility(View.VISIBLE);
            } else {
                layoutContainer.setVisibility(View.GONE);
            }

        } else {
            shouldExecuteOnResume = true;
            SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
            editor.putString(PLAYING_FROM, "Null");
            editor.apply();
        }
    }

    public void stopMusic() {

        if (player.equals("ONLINE")) {
            if (PlayerActivity.musicService.isPlaying()) {
                PlayerActivity.musicService.stop();
                PlayerActivity.musicService.showNotification(R.drawable.play_icon);
                BottomPlayerFragment.playPauseBtn.setImageResource(R.drawable.play_icon);
//                ComponentName component = new ComponentName(MainActivity.this, MusicService.class);
//                am.unregisterMediaButtonEventReceiver(component);

                SHOW_MINI_PLAYER = false;
                SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                editor.putString(MUSIC_FILE, null);
                editor.putString(ARTIST_NAME, null);
                editor.putString(SONG_NAME, null);
                editor.putString(PLAYING_FROM, "Null");
                editor.apply();

            }
        } else {
            if (musicService.isPlaying()) {
                musicService.stop();
                musicService.showNotification(R.drawable.play_icon);
                BottomPlayerFragment.playPauseBtn.setImageResource(R.drawable.play_icon);
//                ComponentName component = new ComponentName(MainActivity.this, MusicServiceLocal.class);
//                am.unregisterMediaButtonEventReceiver(component);

                SHOW_MINI_PLAYER = false;
                SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                editor.putString(MUSIC_FILE, null);
                editor.putString(ARTIST_NAME, null);
                editor.putString(SONG_NAME, null);
                editor.putString(PLAYING_FROM, "Null");
                editor.apply();

            }
        }

    }

    private void implementSwipeDismiss() {

        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fragBottomPlayer.getLayoutParams();

        layoutParams.setBehavior(swipeDismissBehavior);//set swipe behaviour to Coordinator layout

    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }

        void addFragments(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void initViewPager() {
        LocalSongs.ViewPagerAdapter viewPagerAdapter = new LocalSongs.ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new com.taimoor.musicplayer.Fragments.LocalSongs(), "Songs");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

//    private void implementSwipeDismiss() {
//
//        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);//Swipe direction i.e any direction, here you can put any direction LEFT or RIGHT
//
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fragBottomPlayer.getLayoutParams();
//
//        layoutParams.setBehavior(swipeDismissBehavior);//set swipe behaviour to Coordinator layout
//
//    }

    public void runtimePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocalSongs.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

        } else {
            musicFiles = getSongs(this);
            if (musicFiles != null) {
                noSongsTv.setVisibility(View.GONE);
            }
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFiles = getSongs(this);
                if (musicFiles != null) {
                    noSongsTv.setVisibility(View.GONE);
                }
                initViewPager();
            } else {
                ActivityCompat.requestPermissions(LocalSongs.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    public ArrayList<MusicFiles> getSongs(Context context) {

        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };

        String path1 = "/storage/emulated/0";

        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=" + 0
                + " AND " + MediaStore.Audio.Media.DATA + " LIKE '" + path1
                + "/Music/MusicPlayer/%'";

        String[] argsArray = tempAudioList.toArray(new String[tempAudioList.size()]);

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, argsArray, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration);
                //take log e to check path
                Log.e("Path: " + path, "Duration: " + duration);
                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }


}