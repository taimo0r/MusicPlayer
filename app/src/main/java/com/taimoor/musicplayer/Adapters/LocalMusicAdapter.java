package com.taimoor.musicplayer.Adapters;

import static com.taimoor.musicplayer.Utils.Common.formattedTime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.taimoor.musicplayer.Activities.PlayerActivity;
import com.taimoor.musicplayer.Activities.PlayerActivityLocal;
import com.taimoor.musicplayer.Models.MusicFiles;
import com.taimoor.musicplayer.R;

import java.util.ArrayList;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.Viewholder> {

    Context context;
    ArrayList<MusicFiles> files;


    public LocalMusicAdapter(Context context, ArrayList<MusicFiles> files) {
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public LocalMusicAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicAdapter.Viewholder holder, int position) {

        int duration_total = Integer.parseInt(files.get(position).getDuration()) / 1000;

        holder.fileName.setText(files.get(position).getTitle());
        holder.duration.setText(formattedTime(duration_total));


        holder.artistName.setText(files.get(position).getArtist());
        Picasso.get().load(files.get(position).getPath()).placeholder(R.drawable.music_img).into(holder.album_art);
        holder.songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayerActivityLocal.class);
                intent.putExtra("position", position);
                context.startActivity(intent);

            }
        });


//        byte[] image = getAlbumArt(files.get(position).getPath());
//
//        if (image!=null){
//            Glide.with(context).asBitmap()
//                    .load(image)
//                    .into(holder.album_art);
//        }
//        else {
//            Glide.with(context)
//                    .load(R.drawable.music)
//                    .into(holder.album_art);
//        }

    }


    @Override
    public int getItemCount() {
        return files.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ConstraintLayout songItem;
        TextView fileName, artistName, duration, totalSongs;
        ImageView album_art;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            songItem = itemView.findViewById(R.id.audio_item);
            artistName = itemView.findViewById(R.id.singer_name);
            duration = itemView.findViewById(R.id.song_duration);
            totalSongs = itemView.findViewById(R.id.total_songs);

        }
    }


}
