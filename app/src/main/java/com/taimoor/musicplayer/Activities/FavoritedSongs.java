package com.taimoor.musicplayer.Activities;

import static com.taimoor.musicplayer.Activities.PlayerActivityLocal.musicService;
import static com.taimoor.musicplayer.Utils.ApplicationClass.shouldExecuteOnResume;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.taimoor.musicplayer.Adapters.FavoriteRecyclerAdapter;
import com.taimoor.musicplayer.Database.AppDatabase;
import com.taimoor.musicplayer.Database.FavoriteSongs;
import com.taimoor.musicplayer.Fragments.BottomPlayerFragment;
import com.taimoor.musicplayer.R;

import java.util.List;

public class FavoritedSongs extends AppCompatActivity {

    RecyclerView recyclerView;
    FavoriteRecyclerAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    AlertDialog alertDialog;
    Button cancelBtn, deleteBtn;
    BottomNavigationView bottomNavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorited_songs);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Favorites");
        }
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        recyclerView = findViewById(R.id.favorites_recyclerView);
        refreshLayout = findViewById(R.id.refreshLayout);
        layoutContainer = findViewById(R.id.frame_layout_container);
        fragBottomPlayer = findViewById(R.id.frag_bottom_player);

        swipeDismissBehavior = new SwipeDismissBehavior();

        bottomNavigationView.setSelectedItemId(R.id.favorite_activity);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.online_activity:
                        Intent onlineIntent = new Intent(FavoritedSongs.this, MainActivity.class);
                        startActivity(onlineIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.search_activity:
                        Intent searchIntent = new Intent(FavoritedSongs.this, SearchActivity.class);
                        startActivity(searchIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.local_activity:
                        Intent localIntent = new Intent(FavoritedSongs.this, LocalSongs.class);
                        startActivity(localIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.favorite_activity:

                        return true;
                }

                return false;
            }
        });

        initializeRecycler();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeRecycler();
                refreshLayout.setRefreshing(false);
            }
        });

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

    private void initializeRecycler() {
        AppDatabase database = AppDatabase.getInstance(FavoritedSongs.this);
        List<FavoriteSongs> songs = database.favoriteSongsDao().getFavoriteSongs();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FavoriteRecyclerAdapter(this, songs);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.delete_history);

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                clearHistoryDialog();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void clearHistoryDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(FavoritedSongs.this);
        View dialogView = getLayoutInflater().inflate(R.layout.clear_history_dialog, null);

        cancelBtn = (Button) dialogView.findViewById(R.id.cancel_btn);
        deleteBtn = (Button) dialogView.findViewById(R.id.delete_btn);

        alert.setView(dialogView);
        alertDialog = alert.create();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteHistory();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void deleteHistory() {
        AppDatabase database = AppDatabase.getInstance(FavoritedSongs.this);
        database.favoriteSongsDao().deleteAll();

        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}