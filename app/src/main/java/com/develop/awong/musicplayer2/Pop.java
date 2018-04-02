package com.develop.awong.musicplayer2;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tom on 2/10/2018.
 */

public class Pop extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate((savedInstanceState));
        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        NameGenerator nameGenerator = new NameGenerator();

        getWindow().setLayout((int) (width * 0.8),  (int) (height *0.6));

        TextView title = (TextView) findViewById(R.id.title);
        TextView date = (TextView) findViewById(R.id.date);
        TextView time = (TextView) findViewById(R.id.time);
        TextView artist = (TextView) findViewById(R.id.Artist);
        TextView album = (TextView) findViewById(R.id.album);
        TextView place = (TextView) findViewById(R.id.place);
        TextView userName = (TextView) findViewById(R.id.userName);

        String currentSongTitle = MenuActivity.musicPlayer.getSongTitle();
        Calendar calendar = MenuActivity.songMap.get(currentSongTitle).getCal();
        String dateText = new SimpleDateFormat("EEE MMM dd yyyy").format(calendar.getTime());
        String timeText = new SimpleDateFormat("hh:mm:ss 'GMT'Z").format(calendar.getTime());

        title.setText("Title: " + currentSongTitle);
        date.setText("Date Last Played: " + dateText);
        time.setText("Time Last Played " + timeText);
        artist.setText("Artist: " + MenuActivity.songMap.get(currentSongTitle).getArtist());
        album.setText("Album: " + MenuActivity.songMap.get(currentSongTitle).getAlbumName());
        place.setText("Location Last Played: " + MenuActivity.songMap.get(currentSongTitle).getLocation());

        userName.setText("Last Played User: " + MenuActivity.songMap.get(currentSongTitle).getUser());


        if(MenuActivity.songMap.get(currentSongTitle).getUser() != null &&
                MenuActivity.songMap.get(currentSongTitle).getUser().equalsIgnoreCase("You")) {
            userName.setTypeface(userName.getTypeface(), Typeface.ITALIC);
        }




    }
}
