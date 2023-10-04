package com.taimoor.musicplayer.Activities;

import static com.taimoor.musicplayer.Activities.PlayerActivityLocal.musicService;
import static com.taimoor.musicplayer.Utils.ApplicationClass.shouldExecuteOnResume;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.taimoor.musicplayer.Database.AppDatabase;
import com.taimoor.musicplayer.Database.SearchedSongs;
import com.taimoor.musicplayer.Fragments.AlbumFragment;
import com.taimoor.musicplayer.Fragments.BottomPlayerFragment;
import com.taimoor.musicplayer.Fragments.PlaylistFragment;
import com.taimoor.musicplayer.Fragments.SongsFragment;
import com.taimoor.musicplayer.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    TabLayout tabLayout;
    private AutoCompleteTextView autoCompleteTextView;
    String searchQuery = null;
    private ArrayList<String> allSongs;
    AppDatabase database;
    ProgressDialog dialog;
    ImageView historyBtn;
    TextView noSongsTv;
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
        setContentView(R.layout.activity_search);
        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Search");
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        autoCompleteTextView = findViewById(R.id.autoCompleteTxt);
        historyBtn = findViewById(R.id.history_btn);
        noSongsTv = findViewById(R.id.no_songs_tv);
        layoutContainer = findViewById(R.id.frame_layout_container);
        fragBottomPlayer = findViewById(R.id.frag_bottom_player);

        swipeDismissBehavior = new SwipeDismissBehavior();

        database = AppDatabase.getInstance(SearchActivity.this);
        allSongs = (ArrayList<String>) database.searchedSongsDao().geSongs();


        ArrayAdapter<String> autocomplete = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, allSongs);
        autoCompleteTextView.setAdapter(autocomplete);


        if (getIntent().getStringExtra("query") != null) {
            searchQuery = getIntent().getStringExtra("query");
            getIntent().removeExtra("query");
            autoCompleteTextView.setText(searchQuery);
        }

        dialog = new ProgressDialog(SearchActivity.this);
        dialog.setTitle("Loading...");


        bottomNavigationView.setSelectedItemId(R.id.search_activity);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.online_activity:
                        Intent onlineIntent = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(onlineIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.search_activity:

                        return true;
                    case R.id.local_activity:
                        Intent localIntent = new Intent(SearchActivity.this, LocalSongs.class);
                        startActivity(localIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.favorite_activity:
                        Intent favIntent = new Intent(SearchActivity.this, FavoritedSongs.class);
                        startActivity(favIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }

                return false;
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchQuery = autoCompleteTextView.getText().toString();
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                initViewPager();
            }
        });


        autoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    searchQuery = autoCompleteTextView.getText().toString();

                    if (searchQuery.length() > 0) {

                        tabLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                        initViewPager();
                        saveSong(searchQuery);
                        return true;
                    }
                }

                return false;
            }
        });

        if (searchQuery != null) {
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            initViewPager();
        }

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

    private void saveSong(String searchQuery) {

        SearchedSongs songs = new SearchedSongs();
        songs.song = searchQuery;
        database.searchedSongsDao().SaveSearchedSong(songs);

    }

    public String getQuery() {

        return searchQuery;

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


    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SongsFragment(), "Songs");
        viewPagerAdapter.addFragments(new AlbumFragment(), "Albums");
        viewPagerAdapter.addFragments(new PlaylistFragment(), "Playlists");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        noSongsTv.setVisibility(View.GONE);

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


}