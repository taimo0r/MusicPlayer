package com.taimoor.musicplayer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taimoor.musicplayer.Listeners.onSongItemClick;
import com.taimoor.musicplayer.Models.Result;
import com.taimoor.musicplayer.R;
import com.taimoor.musicplayer.Utils.Common;

import java.util.List;

import ir.siaray.downloadmanagerplus.classes.Downloader;

public class SearchAlbumAdapter extends RecyclerView.Adapter<SearchAlbumAdapter.ViewHolder> {

    private Context context;
    private List<Result> list;
    onSongItemClick clickListener;

    public SearchAlbumAdapter(Context context, List<Result> list, onSongItemClick clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.fileName.setText(list.get(position).getTitle());
        holder.language.setText(Common.firstWordToUppercase(list.get(position).getLanguage()));
        holder.songCount.setText("Songs: "+ list.get(position).getMore_info().getSong_count());
        Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.music_img).into(holder.album_art);


        String albumId = list.get(position).getId();


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

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView songItem;
        TextView fileName, language, songCount;
        ImageView album_art;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.album_name);
            language = itemView.findViewById(R.id.language);
            songCount = itemView.findViewById(R.id.total_songs);
            album_art = itemView.findViewById(R.id.album_img);
            songItem = itemView.findViewById(R.id.album_items_container);

        }
    }
}
