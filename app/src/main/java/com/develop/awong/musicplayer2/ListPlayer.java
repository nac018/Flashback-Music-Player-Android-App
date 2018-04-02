package com.develop.awong.musicplayer2;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jeremyjiang on 2/18/18.
 */

public class ListPlayer {
    List<String> listOfSongTitles;
    int count = 0;
    MediaPlayer mediaPlayer;
    Context context;
    ImageView albumArt;


    public ListPlayer (List<String> listOfSongTitles, Context context) {
        this.listOfSongTitles = listOfSongTitles;
        this.context = context;
    }

    public void playCurrentList() {
        if (MenuActivity.musicPlayer != null && MenuActivity.musicPlayer.getMediaPlayer() != null){
            if (MenuActivity.musicPlayer.getMediaPlayer().isPlaying()) {
                MenuActivity.musicPlayer.getMediaPlayer().stop();

            }
        }

        mediaPlayer = new MediaPlayer();

        if(MenuActivity.musicPlayer != null) {
            MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
            String songTitle = listOfSongTitles.get(count);
            MenuActivity.musicPlayer.setSongTitle(songTitle);


            setMusicActivity(songTitle);


            Song songObject = MenuActivity.songMap.get(songTitle);
            MainActivity.fileName = songObject.getFileName();
            MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());




            while ((songObject.getPriority() == 0 && count < listOfSongTitles.size()-1)) {
                count++;
                MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
                songTitle = listOfSongTitles.get(count);

                //Updates Song Title text on UI
                setMusicActivity(songTitle);


                MenuActivity.musicPlayer.setSongTitle(songTitle);
                songObject = MenuActivity.songMap.get(songTitle);
                MainActivity.fileName = songObject.getFileName();
                MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());
            }

            boolean canplay = true;
            if(songObject.getPriority() == 0 &&
                    songObject == MenuActivity.songMap.get(listOfSongTitles.get(listOfSongTitles.size()-1))){
               canplay = false;

            }

            if (canplay) {

                int resourceId = getSongId(count);

                AssetFileDescriptor assetFileDescriptor = context.getResources().openRawResourceFd(resourceId);

                try {
                    //
                    String songPath = songObject.getPath();
                    if(songPath.equalsIgnoreCase("")) {
                        MenuActivity.musicPlayer.getMediaPlayer().setDataSource(assetFileDescriptor);
                    }
                    else {
                        MenuActivity.musicPlayer.getMediaPlayer().setDataSource(songPath);
                    }
                    //
                    MenuActivity.musicPlayer.getMediaPlayer().prepare();
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                MenuActivity.musicPlayer.getMediaPlayer().start();
                Calendar currentCalendar = Calendar.getInstance();
                Song currentSongObj = MenuActivity.songMap.get(songTitle);
                currentSongObj.setCal(currentCalendar);


                MenuActivity.gps = new GPSTracker(context);

                // check if GPS enabled
                if (MenuActivity.gps.canGetLocation()) {

                    Location loc = MenuActivity.gps.getLocation();
                    currentSongObj.setLocation(loc);
                    // \n is for new line
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    MenuActivity.gps.showSettingsAlert();
                }
            }
            count++;



            MenuActivity.musicPlayer.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (count < listOfSongTitles.size()) {
                        mp.stop();
                        mp.reset();

                        System.out.println("count     " + count);

                        MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
                        String songTitle = listOfSongTitles.get(count);
                        MenuActivity.musicPlayer.setSongTitle(songTitle);

                        if(MusicActivity.songTitle != null) {
                            MusicActivity.songTitle.setText(songTitle);
                        }

                        if(MusicActivity.albumArt != null) {
                            albumArt = MusicActivity.albumArt;
                            byte[] data = MenuActivity.songMap.get(songTitle).getAlbumArt();

                            if(data != null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                albumArt.setImageBitmap(bitmap);
                            }
                        }


                        Song songObject = MenuActivity.songMap.get(songTitle);
                        MainActivity.fileName = songObject.getFileName();
                        MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());

                        while (songObject.getPriority() == 0 && count < listOfSongTitles.size()-1) {
                            count++;
                            MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
                            songTitle = listOfSongTitles.get(count);
                            MenuActivity.musicPlayer.setSongTitle(songTitle);

                            setMusicActivity(songTitle);


                            songObject = MenuActivity.songMap.get(songTitle);
                            MainActivity.fileName = songObject.getFileName();
                            MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());
                        }

                        if(songObject.getPriority() == 0 &&
                                songObject == MenuActivity.songMap.get(listOfSongTitles.get(listOfSongTitles.size()-1))){
                            System.out.println("LAST SONG IS PLAYING");
                            mp.pause();
                        }

                        int resourceId = getSongId(count);
                        System.out.println("res     " + resourceId);

                        AssetFileDescriptor assetFileDescriptor = context.getResources().openRawResourceFd(resourceId);
                        try {
                            //
                            String songPath = songObject.getPath();
                            if(songPath.equalsIgnoreCase("")) {
                                mp.setDataSource(assetFileDescriptor);
                            }
                            else {
                                MenuActivity.musicPlayer.getMediaPlayer().setDataSource(songPath);
                            }
                            //

                            mp.prepare();
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }


                        mp.start();

                        Calendar currentCalendar = Calendar.getInstance();
                        Song currentSongObj = MenuActivity.songMap.get(songTitle);
                        currentSongObj.setCal(currentCalendar);

                        MenuActivity.gps = new GPSTracker(context);

                        // check if GPS enabled
                        if(MenuActivity.gps.canGetLocation()){

                            Location loc = MenuActivity.gps.getLocation();
                            currentSongObj.setLocation(loc);
                            // \n is for new line
                        }
                        else{
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            MenuActivity.gps.showSettingsAlert();
                        }



                        //USED FOR SETTING PROGRESS IN MUSIC ACTIVITY FOR EACH SONG
                        MusicActivity.totalTime = MenuActivity.musicPlayer.getMediaPlayer().getDuration();
                        if (MusicActivity.positionBar != null) {
                            MusicActivity.positionBar.setMax(MenuActivity.musicPlayer.getMediaPlayer().getDuration());
                        }
                        //USED FOR SETTING PROGRESS IN MUSIC ACTIVITY FOR EACH SONG

                        count++;
                    }
                }
            });
        }
    }

    private void setMusicActivity(String songTitle) {
        if(MusicActivity.songTitle != null) {
            MusicActivity.songTitle.setText(songTitle);
        }

        if(MusicActivity.albumArt != null) {
            albumArt = MusicActivity.albumArt;
            byte[] data = MenuActivity.songMap.get(songTitle).getAlbumArt();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            albumArt.setImageBitmap(bitmap);
        }
    }


    public int getSongId(int count) {
        String songTitle = listOfSongTitles.get(count);
        Song song = MenuActivity.songMap.get(songTitle);
        String songName = song.getFileName();
        int song_id = context.getResources().getIdentifier(songName, "raw",
                context.getPackageName());
        return song_id;
    }




}

