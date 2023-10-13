package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    Context context;
    ArrayList<AudioModel> modelArrayList;

    public MusicListAdapter(Context context, ArrayList<AudioModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AudioModel songData=modelArrayList.get(position);
        holder.txtMusicTitle.setText(songData.getTitle());

        int index=position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex=index;

                Intent intent=new Intent(context,MusicPlayerActivity.class);
                intent.putExtra("LIST",modelArrayList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                if(MyMediaPlayer.currentIndex==position)
                {
                    holder.txtMusicTitle.setTextColor(Color.parseColor("#FF0000"));
                }
                else
                {
                    holder.txtMusicTitle.setTextColor(Color.parseColor("#000000"));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMusicTitle;
        ImageView imgMusicIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMusicTitle=itemView.findViewById(R.id.txtMusicTitle);
            imgMusicIcon=itemView.findViewById(R.id.imgMusicIcon);
        }
    }
}
