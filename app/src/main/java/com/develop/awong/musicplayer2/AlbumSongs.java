package com.develop.awong.musicplayer2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AlbumSongs extends AppCompatActivity {

    ListView albumSongList;
    List<String> listOfSongs;
    List<String> listOfSongTitles;
    ListAdapter adapter;
    MediaPlayer mediaPlayer;
    int count = 0;

    ListPlayer listPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);

        Button albumMenuBtn = (Button) findViewById(R.id.albumMenuBtn);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            String albumName = extras.getString("albumName");
            listOfSongs = MenuActivity.albumMap.get(albumName).fileNames;
            albumSongList = (ListView) findViewById(R.id.albumSongList);
            listOfSongTitles = MenuActivity.albumMap.get(albumName).songTitles;
            albumSongList.setAdapter(new MyListAdapter(this, R.layout.list_item, listOfSongTitles));
            Log.d("AlbumSongs","Examine name: " + albumName);
        }


        albumMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AlbumSongs", "onClick: User clicks ALBUM MENU button");
                Intent i = new Intent(AlbumSongs.this, AlbumActivity.class);
                AlbumSongs.this.startActivity(i);
                finish();
            }
        });



        if (MenuActivity.musicPlayer != null && MenuActivity.musicPlayer.getMediaPlayer() != null){
            if (MenuActivity.musicPlayer.getMediaPlayer().isPlaying()) {
                MenuActivity.musicPlayer.getMediaPlayer().stop();

            }
        }


        listPlayer = new ListPlayer(listOfSongTitles, AlbumSongs.this);
        listPlayer.playCurrentList();
    }

    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects){
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public  View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.title =  (TextView) convertView.findViewById(R.id.list_item_text);

            viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
            String currTitle = getItem(position);
            Song currSongObj= MenuActivity.songMap.get(currTitle);

            viewHolder.button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String clickedSong = getItem(position);
                    Song clickedSongObj = MenuActivity.songMap.get(clickedSong);


                    if(viewHolder.button.getText().equals("+")){
                        Log.d("AlbumActivity", "onClick: User clicks +");
                        viewHolder.button.setText("✓");
                        clickedSongObj.setPriority(2);

                    }

                    else if(viewHolder.button.getText().equals("✓")){
                        Log.d("AlbumActivity", "onClick: User clicks ✓");
                        viewHolder.button.setText("X");
                        clickedSongObj.setPriority(0);

                        //Skips song when song is disliked
                        int totalTime = MenuActivity.musicPlayer.getMediaPlayer().getDuration();
                        MenuActivity.musicPlayer.getMediaPlayer().seekTo(totalTime);

                    }
                    else{
                        Log.d("AlbumActivity", "onClick: User clicks X");
                        viewHolder.button.setText("+");
                        clickedSongObj.setPriority(1);
                    }

                    System.out.println(clickedSongObj.getPriority());
                }


            });

            if(currSongObj.getPriority() == 0)
                viewHolder.button.setText("X");
            else if (currSongObj.getPriority() == 1)
                viewHolder.button.setText("+");
            else
                viewHolder.button.setText("✓");

            convertView.setTag(viewHolder);
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.title.setText(currTitle);

            return convertView;
        }
    }
    public class ViewHolder{
        TextView title;
        Button button;
    }
}
