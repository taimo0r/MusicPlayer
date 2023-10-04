package com.taimoor.musicplayer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taimoor.musicplayer.Listeners.onSongItemClick;
import com.taimoor.musicplayer.Models.Result;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Utils.Common;

import java.util.List;



public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context context;
    private List<Result> list;
    onSongItemClick clickListener;

    public MusicAdapter(Context context, List<Result> list, onSongItemClick clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String time = Common.convertSecondsToTime(Integer.parseInt(list.get(position).getMore_info().getDuration()));


        holder.fileName.setText(list.get(position).getTitle());
        holder.fileName.setSelected(true);

        holder.artistName.setText(list.get(position).getMore_info().getMusic());
        holder.artistName.setSelected(true);


        holder.duration.setText(time);
        Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.music_img).into(holder.album_art);

        String albumId = list.get(position).more_info.getAlbum_id();

        holder.songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(albumId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout songItem;
        TextView fileName, artistName, duration;
        ImageView album_art;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            songItem = itemView.findViewById(R.id.audio_item);
            artistName = itemView.findViewById(R.id.singer_name);
            duration = itemView.findViewById(R.id.song_duration);
        }
    }


}
