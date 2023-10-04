package com.taimoor.musicplayer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taimoor.musicplayer.Activities.AlbumActivity;
import com.taimoor.musicplayer.Activities.PlaylistActivity;
import com.taimoor.musicplayer.Listeners.onSongItemClick;
import com.taimoor.musicplayer.Models.NewAlbum;
import com.taimoor.musicplayer.Models.NewTrending;
import com.taimoor.musicplayer.R;

import java.util.List;

public class NewAlbumsAdapter extends RecyclerView.Adapter<NewAlbumsAdapter.ViewHolder> {
    Context context;
    List<NewAlbum> list;

    public NewAlbumsAdapter(Context context, List<NewAlbum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.launch_data_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.music_img).into(holder.image);
        holder.title.setText(list.get(position).getTitle());

        String type = list.get(position).getType();
        type = type.substring(0,1).toUpperCase() + type.substring(1).toLowerCase();
        holder.type.setText(type);
        holder.info.setText(list.get(position).getLanguage());

        String finalId = list.get(position).getId();

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra("albumId", finalId);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, type, info;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title_txt);
            type = itemView.findViewById(R.id.type);
            info = itemView.findViewById(R.id.info);
            container = itemView.findViewById(R.id.launch_data_container);
        }
    }
}
