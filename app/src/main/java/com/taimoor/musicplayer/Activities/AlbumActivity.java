package com.taimoor.musicplayer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.taimoor.musicplayer.Adapters.AlbumAdapter;
import com.taimoor.musicplayer.Adapters.MusicAdapter;
import com.taimoor.musicplayer.Listeners.onSongClick;
import com.taimoor.musicplayer.Listeners.onSongItemClick;
import com.taimoor.musicplayer.Models.AlbumApiResponse;
import com.taimoor.musicplayer.Models.Song;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Retrofit.AlbumResponseListener;
import com.taimoor.musicplayer.Retrofit.ApiClient;
import com.taimoor.musicplayer.Utils.DataHandlers;

import java.io.Serializable;
import java.util.List;

public class AlbumActivity extends AppCompatActivity implements onSongClick {

    RecyclerView recyclerView;
    AlbumAdapter adapter;
    ApiClient client;
    String albumId;
    ShapeableImageView albumImg;
    TextView albumName, artistName, release;
    public static List<Song> allSongs;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_album);

        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.recyclerView);
        albumImg = findViewById(R.id.album_img);
        albumName = findViewById(R.id.album_name);
        artistName = findViewById(R.id.artist_name);
        release = findViewById(R.id.release_date);

        albumName.setSelected(true);
        release.setSelected(true);
        artistName.setSelected(true);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        client = new ApiClient(this);

        albumId = getIntent().getStringExtra("albumId");

        client.getSelectedAlbum(selectedListener, albumId);

    }

    @Override
    protected void onResume() {
        super.onResume();

        client.getSelectedAlbum(selectedListener, albumId);

    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private final AlbumResponseListener selectedListener = new AlbumResponseListener() {
        @Override
        public void onFetch(AlbumApiResponse response, String message) {
            if (response.getSongs().isEmpty()) {
                Toast.makeText(AlbumActivity.this, "No Songs Found", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            showData(response.getSongs(), response);
        }

        @Override
        public void onError(String message) {
            Toast.makeText(AlbumActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };

    private void showData(List<Song> songs, AlbumApiResponse response) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new AlbumAdapter(this, songs, this);
        recyclerView.setAdapter(adapter);

        String name = response.getName();

        name = name.replace("#", "" );
        name = name.replace("&", "" );
        name = name.replace(";", "" );
        name = name.replace("039", "'" );

        Picasso.get().load(response.getImage()).into(albumImg);
        albumName.setText(name);
        artistName.setText(response.getPrimary_artists());
        release.setText(response.getRelease_date());

        allSongs = songs;
        dialog.dismiss();

    }

    @Override
    public void onSongItemClick(int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("pos", position);
        intent.putExtra("list", (Serializable) allSongs);
        intent.putExtra("albumId", albumId);
        intent.putExtra("activity","album");
        startActivity(intent);
    }
}