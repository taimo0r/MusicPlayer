package com.taimoor.musicplayer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import com.taimoor.musicplayer.Adapters.AlbumAdapter;
import com.taimoor.musicplayer.Adapters.PlaylistAdapter;
import com.taimoor.musicplayer.Adapters.SearchPlaylistAdapter;
import com.taimoor.musicplayer.Listeners.onSongClick;
import com.taimoor.musicplayer.Listeners.onSongItemClick;
import com.taimoor.musicplayer.Models.AlbumApiResponse;
import com.taimoor.musicplayer.Models.PlaylistApiResponse;
import com.taimoor.musicplayer.Models.Song;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Retrofit.AlbumResponseListener;
import com.taimoor.musicplayer.Retrofit.ApiClient;
import com.taimoor.musicplayer.Retrofit.PlaylistResponseListener;

import java.io.Serializable;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity implements onSongClick {

    RecyclerView recyclerView;
    PlaylistAdapter adapter;
    ApiClient client;
    String listId;
    ShapeableImageView albumImg;
    TextView albumName, artistName, release;
    public static List<Song> songsList;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.recyclerView);
        albumImg = findViewById(R.id.album_img);
        albumName = findViewById(R.id.album_name);
        artistName = findViewById(R.id.artist_name);
        release = findViewById(R.id.release_date);
        albumName.setSelected(true);
        artistName.setSelected(true);
        release.setSelected(true);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();


        client = new ApiClient(this);

        listId = getIntent().getStringExtra("listId");

        client.getSelectedPlaylists(listener, listId);


    }

    private final PlaylistResponseListener listener = new PlaylistResponseListener() {
        @Override
        public void onFetch(PlaylistApiResponse response, String message) {
            if (response.getSongs().isEmpty()) {
                Toast.makeText(PlaylistActivity.this, "No Songs Found", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            showData(response.getSongs(), response);
        }

        @Override
        public void onError(String message) {
            Toast.makeText(PlaylistActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };


    private void showData(List<Song> songs, PlaylistApiResponse response) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        adapter = new PlaylistAdapter(this, songs, this);
        recyclerView.setAdapter(adapter);

        Picasso.get().load(response.getImage()).placeholder(R.drawable.music_img).into(albumImg);
        albumName.setText(response.getListname());

        String name = response.getUid();
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        artistName.setText(name);
        release.setText(response.getFirstname());

        dialog.dismiss();
        songsList = songs;

    }


    @Override
    public void onSongItemClick(int position) {
        Intent intent = new Intent(PlaylistActivity.this, PlayerActivity.class);
        intent.putExtra("pos", position);
        intent.putExtra("list", (Serializable) songsList);
        intent.putExtra("albumId", listId);
        intent.putExtra("activity","playlist");
        startActivity(intent);
    }
}