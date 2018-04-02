package com.develop.awong.musicplayer2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class AlbumActivity extends AppCompatActivity {
    ListView listView2;
    List<String> list;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Button menuButton2 = (Button) findViewById(R.id.menuButton2);
        Button popBtnAlbum = (Button) findViewById(R.id.popupAlbum);

        if(MenuActivity.musicPlayer != null) {
            popBtnAlbum.setText(popBtnAlbum.getText() + " " + MenuActivity.musicPlayer.getSongTitle());
        }
        menuButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AlbumActivity", "onClick: User clicks MENU button");
                Intent i = new Intent(AlbumActivity.this, MenuActivity.class);
                AlbumActivity.this.startActivity(i);
            }
        });

        popBtnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AlbumActivity", "onClick: User clicks Currently Playing:");
                if (!MenuActivity.musicPlayer.getCurrentSong().equals("")) {
                    String songTitle = MenuActivity.musicPlayer.getSongTitle();
                    String fileName = MenuActivity.musicPlayer.getCurrentSong();
                    if(MenuActivity.songMap.get(songTitle).getPriority() != 0) {
                        MenuActivity.musicPlayer.popUp(fileName, songTitle, AlbumActivity.this,
                                MusicActivity.class);
                    }
                }
            }
        });

        listView2 = (ListView)findViewById(R.id.listView2);


        if(MenuActivity.albumList != null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MenuActivity.albumList);
            listView2.setAdapter(adapter);

            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("AlbumActivity", "onClick: User clicks an album");
                    String albumName = MenuActivity.albumList.get(i);

                    //Currently playing list for track list activity
                    MenuActivity.currentTrackList = MenuActivity.albumMap.get(albumName).songTitles;

                    Intent intent = new Intent(AlbumActivity.this, AlbumSongs.class);
                    intent.putExtra("albumName", albumName);
                    startActivity(intent);
                }
            });
        }

    }
}
