package com.taimoor.musicplayer.Activities;

import static com.taimoor.musicplayer.Activities.PlayerActivityLocal.musicService;
import static com.taimoor.musicplayer.Utils.ApplicationClass.shouldExecuteOnResume;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.taimoor.musicplayer.Fragments.BottomPlayerFragment;
import com.taimoor.musicplayer.Fragments.NewAlbumsFragment;
import com.taimoor.musicplayer.Fragments.PopularPlaylistsFragment;
import com.taimoor.musicplayer.Fragments.TopChartsFragment;
import com.taimoor.musicplayer.Fragments.TrendingFragment;
import com.taimoor.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";
    public static final String IMAGE_LINK = "IMAGE LINK";
    public static final String MUSIC_DURATION = "MUSIC DURATION";
    public static String SONG_TO_FRAG = "SONG";
    public static String ARTIST_TO_FRAG = "ARTIST";
    public static boolean SHOW_MINI_PLAYER = false;
    public static boolean IS_FAV = false;
    public static String PATH_TO_FRAG = null;
    public static String IMAGE_TO_FRAG = null;
    public static String DURATION_TO_FRAG = null;
    public static final String PLAYING_FROM = "Player";
    String player;
    BottomNavigationView bottomNavigationView;
    public static boolean shuffleBoolean = false, repeatBoolean = false;
    ProgressDialog dialog;
    private List<String> selectedLanguages, removedLangs;
    public static String preferredLanguages;
    FrameLayout fragBottomPlayer;
    SwipeDismissBehavior swipeDismissBehavior;
    AudioManager am;
    CoordinatorLayout layoutContainer;
    boolean languageDialog = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        fragBottomPlayer = findViewById(R.id.frag_bottom_player);
        layoutContainer = findViewById(R.id.frame_layout_container);

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Loading...");

        initViewPager();

        SharedPreferences prefs = getSharedPreferences("My_Prefs", MODE_PRIVATE);
        preferredLanguages = prefs.getString("languages", "english");
        languageDialog = prefs.getBoolean("dialog", true);

        if (languageDialog) {
            selectLanguageDialogue();
        }

        swipeDismissBehavior = new SwipeDismissBehavior();

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        bottomNavigationView.setSelectedItemId(R.id.online_activity);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.online_activity:

                        return true;
                    case R.id.search_activity:
                        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.local_activity:
                        Intent localIntent = new Intent(MainActivity.this, LocalSongs.class);
                        startActivity(localIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.favorite_activity:
                        Intent favIntent = new Intent(MainActivity.this, FavoritedSongs.class);
                        startActivity(favIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        if (!isConnected(this)) {
            showCustomDialog();
        }

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

    public void hideMiniPlayer() {
        fragBottomPlayer.setVisibility(View.GONE);
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

    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new TrendingFragment(), "Trending");
        viewPagerAdapter.addFragments(new NewAlbumsFragment(), "New Albums");
        viewPagerAdapter.addFragments(new TopChartsFragment(), "Top Charts");
        viewPagerAdapter.addFragments(new PopularPlaylistsFragment(), "Popular Playlists");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

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


    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Please check your internet connection")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Connect to internet to load Music", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void selectLanguageDialogue() {
        selectedLanguages = new ArrayList<>();
        removedLangs = new ArrayList<>();

        final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Select Your Preferred Languages");

        SharedPreferences preferences = getSharedPreferences("My_Prefs", MODE_PRIVATE);
        String l = preferences.getString("languages", "english").toLowerCase();


        boolean english = false;
        boolean french = false;
        boolean german = false;
        boolean russian = false;
        boolean hindi = false;
        boolean tamil = false;
        boolean punjabi = false;
        boolean urdu = false;
        boolean spanish = false;
        boolean italian = false;


        if (l.contains("english")) {
            english = true;
        }
        if (l.contains("french")) {
            french = true;
        }
        if (l.contains("urdu")) {
            urdu = true;
        }
        if (l.contains("russian")) {
            russian = true;
        }
        if (l.contains("spanish")) {
            spanish = true;
        }
        if (l.contains("italian")) {
            italian = true;
        }
        if (l.contains("punjabi")) {
            punjabi = true;
        }
        if (l.contains("tamil")) {
            tamil = true;
        }
        if (l.contains("hindi")) {
            hindi = true;
        }
        if (l.contains("german")) {
            german = true;
        }


        boolean[] checked = {english, french, german, hindi, italian, russian, spanish, tamil, punjabi, urdu};

        alert.setMultiChoiceItems(R.array.Languages, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {

                String[] items = getResources().getStringArray(R.array.Languages);

                if (isChecked) {
                    selectedLanguages.add(items[i].toLowerCase());
                } else if (selectedLanguages.contains(items[i])) {
                    selectedLanguages.remove(items[i].toLowerCase());
                }

                if (!isChecked) {
                    removedLangs.add(items[i].toLowerCase());
                }

            }
        });


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String finalLanguages = "";

                for (String Item : selectedLanguages) {
                    finalLanguages = finalLanguages + "," + Item;
                }

                finalLanguages = l + finalLanguages;


                for (int j = 0; j < removedLangs.size(); j++) {

                    String temp = removedLangs.get(j);


                    if (finalLanguages.contains(temp)) {
                        finalLanguages = finalLanguages.replace(temp, "");
                        if (finalLanguages.startsWith(",")) {
                            finalLanguages.substring(1);
                        }

                        if (finalLanguages != null && finalLanguages.length() > 0 && finalLanguages.charAt(finalLanguages.length() - 1) == 'x') {
                            finalLanguages = finalLanguages.substring(0, finalLanguages.length() - 1);
                        }


                    }
                }


                SharedPreferences.Editor editor = getSharedPreferences("My_Prefs", MODE_PRIVATE).edit();
                editor.putString("languages", finalLanguages.toLowerCase());
                editor.putBoolean("dialog", false);
                editor.apply();
                finish();

                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        });


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        alert.show();

    }

    private void implementSwipeDismiss() {

        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fragBottomPlayer.getLayoutParams();

        layoutParams.setBehavior(swipeDismissBehavior);//set swipe behaviour to Coordinator layout

    }

    //Check if the internet is connected or not.
    private boolean isConnected(MainActivity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        getMenuInflater().inflate(R.menu.language_selection_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_favorites);
        MenuItem menuItem1 = menu.findItem(R.id.action_language);

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, FavoritedSongs.class);
                startActivity(intent);

                return true;
            }
        });

        menuItem1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                selectLanguageDialogue();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}