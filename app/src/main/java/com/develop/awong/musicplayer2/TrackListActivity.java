package com.develop.awong.musicplayer2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class TrackListActivity extends AppCompatActivity {
    ListView listView;
    List<String> trackList;
    private static MediaPlayer mediaPlayer;
    private static Song song;
    private static String currentSong = "";
    private static String songTitle = "";
    public static Button currentTrackBtn;
    Button menuButton;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);

        menuButton = (Button) findViewById(R.id.menuBtnCurTracks);
        currentTrackBtn = (Button) findViewById(R.id.currentTrack);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TrackListActivity", "onClick: User clicks the MENU button");
                Intent i = new Intent(TrackListActivity.this, MenuActivity.class);
                TrackListActivity.this.startActivity(i);
            }
        });

        if(MenuActivity.musicPlayer != null) {
            currentTrackBtn.setText("Currently Playing: " + MenuActivity.musicPlayer.getSongTitle());
        }

        currentTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TrackListActivity", "onClick: User clicks Currently Playing:");
                if (!MenuActivity.musicPlayer.getCurrentSong().equals("")) {
                    String songTitle = MenuActivity.musicPlayer.getSongTitle();
                    String fileName = MenuActivity.musicPlayer.getCurrentSong();
                    if(MenuActivity.songMap.get(songTitle).getPriority() != 0) {
                        MenuActivity.musicPlayer.popUp(fileName, songTitle,
                                TrackListActivity.this, MusicActivity.class);
                    }
                }
            }
        });


        listView = (ListView) findViewById(R.id.currentTracks);
        trackList = MenuActivity.currentTrackList;

        if (trackList != null) {
            listView.setAdapter(new MyListAdapter(this, R.layout.list_item, trackList));
        }
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;

        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TrackListActivity.ViewHolder mainViewHolder = null;

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);

            viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);

            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TrackListActivity", "onClick: User clicks a song");
                    String clickedSong = getItem(position);
                    Song clickedSongObj = MenuActivity.songMap.get(clickedSong);

                    if (viewHolder.button.getText().equals("+")) {
                        Log.d("TrackListActivity", "onClick: User clicks + ");
                        viewHolder.button.setText("✓");
                        clickedSongObj.setPriority(2);

                    } else if (viewHolder.button.getText().equals("✓")) {
                        Log.d("TrackListActivity", "onClick: User clicks ✓ ");
                        viewHolder.button.setText("X");
                        clickedSongObj.setPriority(0);

                        //Skips song when song is disliked
                        int totalTime = MenuActivity.musicPlayer.getMediaPlayer().getDuration();
                        MenuActivity.musicPlayer.getMediaPlayer().seekTo(totalTime);


                    } else {
                        Log.d("TrackListActivity", "onClick: User clicks X ");
                        viewHolder.button.setText("+");
                        clickedSongObj.setPriority(1);
                    }

                    System.out.println(clickedSongObj.getPriority());
                }
            });

            String currTitle = getItem(position);
            Song currSongObj = MenuActivity.songMap.get(currTitle);


            if (currSongObj.getPriority() == 0)
                viewHolder.button.setText("X");
            else if (currSongObj.getPriority() == 1)
                viewHolder.button.setText("+");
            else
                viewHolder.button.setText("✓");

            convertView.setTag(viewHolder);
            mainViewHolder = (TrackListActivity.ViewHolder) convertView.getTag();

            if (MenuActivity.musicPlayer.getSongTitle().equals(currTitle)) {
                mainViewHolder.title.setText(currTitle + "   **Playing**");
            }
            else {
                mainViewHolder.title.setText(currTitle);
            }

            return convertView;
        }
    }

    public class ViewHolder{
        TextView title;
        Button button;
    }

}

