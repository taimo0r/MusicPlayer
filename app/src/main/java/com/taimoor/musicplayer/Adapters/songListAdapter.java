package com.taimoor.musicplayer.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.taimoor.musicplayer.Listeners.onSongClick;
import com.taimoor.musicplayer.Models.Song;
import com.taimoor.musicplayer.R;

import java.util.List;

public class songListAdapter extends RecyclerView.Adapter<songListAdapter.ViewHolder> {
    private Context context;
    private List<Song> list;
    onSongClick clickListener;

    public songListAdapter(Context context, List<Song> list, onSongClick clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout songItem;
        TextView fileName, artist, duration;
        ImageView album_art, downloadBtn, favoriteBtn;
        SpinKitView spinKitView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            songItem = itemView.findViewById(R.id.audio_item);
            artist = itemView.findViewById(R.id.singer_name);
            duration = itemView.findViewById(R.id.song_duration);
            downloadBtn = itemView.findViewById(R.id.download_btn);
            favoriteBtn = itemView.findViewById(R.id.favorite_btn);
            spinKitView = itemView.findViewById(R.id.spinKitView);
        }
    }
}
