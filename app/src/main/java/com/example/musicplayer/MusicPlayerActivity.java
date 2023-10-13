package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    private TextView txtSongTitle,txtCurrentTime,txtTotalTime;
    private ImageView imgPlay,imgPrev,imgNext,imgMusicIcon;
    private SeekBar seekBar;

    private ArrayList<AudioModel> songsList;
    private AudioModel currentSong;
    private MediaPlayer mediaPlayer=MyMediaPlayer.getInstance();

    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        txtSongTitle=findViewById(R.id.txtSongTitle);
        txtCurrentTime=findViewById(R.id.txtCurrentTime);
        txtTotalTime=findViewById(R.id.txtTotalTime);
        imgNext=findViewById(R.id.imgNext);
        imgPlay=findViewById(R.id.imgPlay);
        imgPrev=findViewById(R.id.imgPrev);
        seekBar=findViewById(R.id.seekBar);
        imgMusicIcon=findViewById(R.id.imgMusicIcon);

        songsList=(ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mediaPlayer!=null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    txtCurrentTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition() + ""));
                }

                if(mediaPlayer.isPlaying())
                {
                    imgPlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                    imgMusicIcon.setRotation(x+=2);
                }
                else
                {
                    imgPlay.setImageResource(R.drawable.baseline_play_circle_outline_24);
                    imgMusicIcon.setRotation(0);
                }

                new Handler().postDelayed(this,100);
            }


        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b)
                {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playPrevMusic();

            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playNextMusic();

            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPauseMusic();

            }
        });


    }

    void setResourcesWithMusic()
    {
        currentSong=songsList.get(MyMediaPlayer.currentIndex);
        txtSongTitle.setText(currentSong.getTitle());
        txtTotalTime.setText(convertToMMSS(currentSong.getDuration()));

        playMusic();
    }


    public static String convertToMMSS(String duration)
    {
        Long millis=Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    private void playMusic()
    {

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void playNextMusic()
    {

        if(MyMediaPlayer.currentIndex==songsList.size()-1)
            return;

        MyMediaPlayer.currentIndex+=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }
    private void playPrevMusic()
    {

        if(MyMediaPlayer.currentIndex==0)
            return;

        MyMediaPlayer.currentIndex-=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }
    private void playPauseMusic()
    {

        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
        else
            mediaPlayer.start();

    }
}