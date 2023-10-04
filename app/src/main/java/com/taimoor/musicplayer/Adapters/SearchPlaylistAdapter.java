package com.taimoor.musicplayer.Adapters;

import android.content.Context;
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

import java.util.List;

public class SearchPlaylistAdapter extends RecyclerView.Adapter<SearchPlaylistAdapter.ViewHolder> {

    private Context context;
    private List<Result> list;
    onSongItemClick clickListener;

    public SearchPlaylistAdapter(Context context, List<Result> list, onSongItemClick clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.fileName.setText(list.get(position).getTitle());
        holder.artistName.setText(list.get(position).getMore_info().getUid());
        holder.duration.setText("Songs: "+list.get(position).getMore_info().getSong_count());
        Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.music_img).into(holder.album_art);

        String listId = list.get(position).getId();

        holder.songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(listId);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout songItem;
        TextView fileName,artistName, duration;
        ImageView album_art;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            songItem = itemView.findViewById(R.id.audio_item);
            artistName = itemView.findViewById(R.id.singer_name);
            duration = itemView.findViewById(R.id.song_duration);

        }
    }
}
