package com.develop.awong.musicplayer2;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.develop.awong.musicplayer2.MenuActivity.songMap;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    private List<String> listOfTitles;
    private ListAdapter adapter;

    protected static String fileName;

    private static boolean sortTitle;
    private static boolean sortArtist;
    private static boolean sortAlbum;
    private static boolean sortFavorite;

    FirebaseManager firebaseManager;

    public static Calendar cal = Calendar.getInstance();

    public static boolean isTimeMock = false;


    //public static final int MEDIA_RES_ID = R.raw.waves;
    //public static final int MEDIA_RES_ID_2 = R.raw.dreamatorium;
    //private int currentResource;
   // private LocalTime intervalStart, intervalEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //intervalStart = LocalTime.parse("11:00:00");
        //intervalEnd = LocalTime.parse("16:00:00");

        Button menuButton = (Button) findViewById(R.id.menuButton);
        Button popBtn = (Button) findViewById(R.id.popupMain);


        //firebaseManager = new FirebaseManager();
        firebaseManager = MenuActivity.firebaseManager;

        if(MenuActivity.musicPlayer != null) {
            popBtn.setText(popBtn.getText() + " " + MenuActivity.musicPlayer.getSongTitle());
        }

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onClick: User clicks the MENU button");
                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

        popBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onClick: User clicks Current Playing:");

                if (!MenuActivity.musicPlayer.getCurrentSong().equals("")) {
                    String songTitle = MenuActivity.musicPlayer.getSongTitle();
                    fileName = MenuActivity.musicPlayer.getCurrentSong();

                    if(songMap.get(songTitle).getPriority() != 0) {
                        MenuActivity.musicPlayer.popUp(fileName, songTitle, MainActivity.this,
                                MusicActivity.class);
                        Log.d("MainActivity","Download Link of " + songTitle + " is :" + songMap.get(songTitle).getLink());
                    }
                }
            }
        });


        listView = (ListView) findViewById(R.id.listView);

        if(listOfTitles == null) {
            System.out.println("MainActivities listOfTitles is empty");
        }

        //Deep copy
        listOfTitles = MenuActivity.getlistTitles();

        adapter = new MyListAdapter(this, R.layout.list_item, listOfTitles);

        if(listOfTitles != null) {
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("MainActivity", "onClick: User clicks a song");

                    String songTitle = listOfTitles.get(i);
                    fileName = songMap.get(songTitle).getFileName();

                    songMap.get(songTitle).setUser(LoginActivity.personName);

                    //Getting Data and Location for Flashback
                    Song currentSongObj = songMap.get(songTitle);

                    if(!isTimeMock) {
                        Calendar currentCalendar = Calendar.getInstance();
                        currentSongObj.setCal(currentCalendar);
                    }
                    else {
                        //Calendar currentCalendar = Calendar.getInstance();
                        currentSongObj.setCal(cal);
                    }

                    MenuActivity.gps = new GPSTracker(MainActivity.this);

                    // check if GPS enabled
                    if(MenuActivity.gps.canGetLocation()){
                        Location loc = MenuActivity.gps.getLocation();
                        if(loc == null) {
                            System.out.println("NULLLLLLLL");
                        }
                        currentSongObj.setLocation(loc);
                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        MenuActivity.gps.showSettingsAlert();
                    }

                    if(currentSongObj == null) {
                        System.out.println("CURRENT SONG OBJ IS NULL");
                    }

                    System.out.println("FIREBASE SUBMIT");
                    firebaseManager.submit(currentSongObj, MainActivity.this);

                    if(songMap.get(songTitle).getPriority() != 0) {
                        MenuActivity.musicPlayer.setSongTitle(songTitle);
                        MenuActivity.musicPlayer.popUp(fileName, songTitle, MainActivity.this, MusicActivity.class);
                        Log.d("MainActivity","Download Link of " + songTitle + " is :" + songMap.get(songTitle).getLink());
                        MenuActivity.currentTrackList = null;
                    }
                }
            });
        }
    }

    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        private List<String> listSongs;

        private MyListAdapter(Context context, int resource, List<String> objects){
            super(context, resource, objects);
            layout = resource;
            listSongs = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.title =  (TextView) convertView.findViewById(R.id.list_item_text);

            viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);

            viewHolder.button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String clickedSong = getItem(position);
                    Song clickedSongObj = songMap.get(clickedSong);

                    if(viewHolder.button.getText().equals("+")){
                        Log.d("MainActivity", "onClick: User clicks + ");
                        viewHolder.button.setText("✓");
                        clickedSongObj.setPriority(2);

                    }

                    else if(viewHolder.button.getText().equals("✓")){
                        Log.d("MainActivity", "onClick: User clicks ✓ ");
                        viewHolder.button.setText("X");
                        clickedSongObj.setPriority(0);

                        //Fixes bug when disliking then playing again.
                        String currentSong = MenuActivity.musicPlayer.getSongTitle();

                        Log.d("MainActivity", "onClick: Clicked song is " + clickedSong);
                        Log.d("MainActivity", "onClick: Current song is " + currentSong);


                        if(clickedSong.equals(currentSong) && MenuActivity.musicPlayer.getMediaPlayer() != null) {
                            MenuActivity.musicPlayer.getMediaPlayer().seekTo(0);
                            MenuActivity.musicPlayer.getMediaPlayer().pause();
                        }

                    }
                    else{
                        Log.d("MainActivity", "onClick: User clicks X ");
                        viewHolder.button.setText("+");
                        clickedSongObj.setPriority(1);
                    }

                    System.out.println(clickedSongObj.getPriority());
                }
            });

            String currTitle = getItem(position);
            Song currSongObj= songMap.get(currTitle);


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


    //Comparator for Descending Order of songs
    public static Comparator<String> SongDescComparator = new Comparator<String>() {

        public int compare(String string1, String string2) {

            String stringName1 = string1;
            String stringName2 = string2;

            return stringName2.compareToIgnoreCase(stringName1);
        }
    };

    // Comparator for Ascending Order og songs
    public static Comparator<String> SongAscComparator = new Comparator<String>() {

        public int compare(String string1, String string2) {

            String stringName1 = string1;
            String stringName2 = string2;

            return stringName1.compareToIgnoreCase(stringName2);
        }
    };

    //Comparator for Descending Order of artists
    public static Comparator<String> ArtistDescComparator = new Comparator<String>() {

        public int compare(String string1, String string2) {

            String stringName1 = songMap.get(string1).getArtist();
            String stringName2 = songMap.get(string2).getArtist();

            return stringName2.compareToIgnoreCase(stringName1);
        }
    };

    // Comparator for Ascending Order of artists
    public static Comparator<String> ArtistAscComparator = new Comparator<String>() {

        public int compare(String string1, String string2) {

            String stringName1 = songMap.get(string1).getArtist();
            String stringName2 = songMap.get(string2).getArtist();

            return stringName1.compareToIgnoreCase(stringName2);
        }
    };

    //Comparator for Descending Order of albums
    public static Comparator<String> AlbumDescComparator = new Comparator<String>() {

        public int compare(String string1, String string2) {

            String stringName1 = songMap.get(string1).getAlbumName();
            String stringName2 = songMap.get(string2).getAlbumName();

            return stringName2.compareToIgnoreCase(stringName1);
        }
    };

    // Comparator for Ascending Order of albums
    public static Comparator<String> AlbumAscComparator = new Comparator<String>() {

        public int compare(String string1, String string2) {

            String stringName1 = songMap.get(string1).getAlbumName();
            String stringName2 = songMap.get(string2).getAlbumName();

            return stringName1.compareToIgnoreCase(stringName2);
        }
    };


    //Comparator for Descending Order of favorites
    public static Comparator<String> FavDescComparator = new Comparator<String>() {

        public int compare(String string1, String string2) {

            int stringName1 = songMap.get(string1).getPriority();
            int stringName2 = songMap.get(string2).getPriority();

            return stringName2 - stringName1;
        }
    };

    // Comparator for Ascending Order of favorites
    public static Comparator<String> FavAscComparator = new Comparator<String>() {

        public int compare(String string1, String string2) {

            int stringName1 = songMap.get(string1).getPriority();
            int stringName2 = songMap.get(string2).getPriority();

            return stringName1 - stringName2;
        }
    };



    public void sortTitle(View view) {
        if(!sortTitle){
            Toast.makeText(MainActivity.this, "Sorting titles in Descending Order", Toast.LENGTH_SHORT).show();
            Collections.sort(listOfTitles, SongDescComparator);
            ((BaseAdapter) adapter).notifyDataSetChanged();
            sortTitle = true;
        }
        else {
            Toast.makeText(MainActivity.this, "Sorting titles in Ascending Order", Toast.LENGTH_SHORT).show();
            Collections.sort(listOfTitles, SongAscComparator);
            ((BaseAdapter)adapter).notifyDataSetChanged();
            sortTitle = false;
        }

    }

    public void sortArtist(View view) {
        if(!sortArtist){
            Toast.makeText(MainActivity.this, "Sorting artists in Descending Order", Toast.LENGTH_SHORT).show();
            Collections.sort(listOfTitles, ArtistDescComparator);
            ((BaseAdapter) adapter).notifyDataSetChanged();
            sortArtist = true;
        }
        else {
            Toast.makeText(MainActivity.this, "Sorting artists in Ascending Order", Toast.LENGTH_SHORT).show();
            Collections.sort(listOfTitles, ArtistAscComparator);
            ((BaseAdapter)adapter).notifyDataSetChanged();
            sortArtist = false;
        }
    }

    public void sortAlbum(View view) {
        if(!sortAlbum){
            Toast.makeText(MainActivity.this, "Sorting in albums Descending Order", Toast.LENGTH_SHORT).show();
            Collections.sort(listOfTitles, AlbumDescComparator);
            ((BaseAdapter) adapter).notifyDataSetChanged();
            sortAlbum = true;
        }
        else {
            Toast.makeText(MainActivity.this, "Sorting in albums Ascending Order", Toast.LENGTH_SHORT).show();
            Collections.sort(listOfTitles, AlbumAscComparator);
            ((BaseAdapter)adapter).notifyDataSetChanged();
            sortAlbum = false;
        }
    }

    public void sortFavorite(View view) {
        if(!sortFavorite){
            Toast.makeText(MainActivity.this, "Sorting in favorites Descending Order", Toast.LENGTH_SHORT).show();
            Collections.sort(listOfTitles, FavDescComparator);
            ((BaseAdapter) adapter).notifyDataSetChanged();
            sortFavorite = true;
        }
        else {
            Toast.makeText(MainActivity.this, "Sorting in favorites Ascending Order", Toast.LENGTH_SHORT).show();
            Collections.sort(listOfTitles, FavAscComparator);
            ((BaseAdapter)adapter).notifyDataSetChanged();
            sortFavorite = false;
        }
    }

    public void search()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myFirebaseRef = database.getReference();

        final String songWant = "Beautiful Pain";

        Query queryRef = myFirebaseRef.orderByChild("songTitle").equalTo(songWant);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null)
                    Toast.makeText(MainActivity.this, "No record found", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    //Log.d("Song", snapshot.child("123 Go/score").getValue().toString());
                    Log.d("Song2", snapshot.child(songWant + "/score").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Faile to read value
                Log.w("TAG1", "Failed to read value.", error.toException());
            }
        });

    }


    /**
    public int getCurrentResource(){
        int cr;
        System.out.println("*******************************");
        LocalTime currentTime = TimeMachine.now().toLocalTime();
        if(currentTime.isAfter(intervalStart) && currentTime.isBefore(intervalEnd)){
            cr = MEDIA_RES_ID;
        }
        else {
            cr = MEDIA_RES_ID_2;
        }
        return cr;
    }
     **/

}




