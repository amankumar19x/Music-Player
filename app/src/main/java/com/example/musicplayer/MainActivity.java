package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtNoSong;

    ArrayList<AudioModel> songList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);
        txtNoSong=findViewById(R.id.txtNoSong);

        if(checkPermission()==false)
        {
            requestPermission();
            return;
        }

        String projection[]={
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION


        };

        String selection=MediaStore.Audio.Media.IS_MUSIC+" !=0";

        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
        while(cursor.moveToNext())
        {
            AudioModel songData=new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if(new File(songData.getPath()).exists())
            {
                songList.add(songData);
            }

            if(songList.size()==0)
            {
                txtNoSong.setVisibility(View.VISIBLE);
            }
            else
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(new MusicListAdapter(MainActivity.this,songList));

            }
        }


    }


    boolean checkPermission()
    {
        int result= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO);
        if(result== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
            return false;
    }

    void requestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO))
        {
            Toast.makeText(this, "Please Give Permission For Reading Audio File", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_MEDIA_AUDIO},100);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(recyclerView!=null)
        {
            recyclerView.setAdapter(new MusicListAdapter(MainActivity.this,songList));
        }
    }
}