package com.develop.awong.musicplayer2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by AWong on 2/13/18.
 */

public class MusicPlayer {
    private static MediaPlayer mediaPlayer;
    private static Song song;
    private static String currentSong = "";
    private static String songTitle = "";
    Intent intent;

    public MusicPlayer() {
    }

    public MusicPlayer(MediaPlayer mediaPlayer ) {
        this.mediaPlayer = mediaPlayer;
    }

    public void playSong(String songName, String songTitle) {

    }

    public void popUp(String songName, String songTitle, Context context, Class classTo) {

        if(currentSong.equals(songName)) {
            launchActivity(currentSong, context, classTo);
            System.out.println("Current song is " + currentSong);
            System.out.println("If same song don't play it from beginning again");
        }
        else {
            System.out.println("songName is " + songName);
            System.out.println("This current song is " + currentSong);

            System.out.println("This songName is " + this.songTitle);


            if (mediaPlayer != null && mediaPlayer.isPlaying() && !currentSong.equals(songName)) {
                mediaPlayer.stop();
            }
            this.currentSong = songName;
            this.songTitle = songTitle;

            System.out.println("songName is after " + songName);
            System.out.println("This current song is after " + this.currentSong);
            System.out.println("This songTitle is after " + this.songTitle);


            /*
            int song_id = context.getResources().getIdentifier(currentSong, "raw",
                    context.getPackageName());
            */


            Uri path = Uri.parse(MenuActivity.songMap.get(songTitle).getPath());

            if(path.toString().equalsIgnoreCase("raw")) {
                System.out.println("if statement");
                int song_id = context.getResources().getIdentifier(currentSong, "raw",
                        context.getPackageName());
                mediaPlayer = MediaPlayer.create(context, song_id);
            }
            else {
                mediaPlayer = MediaPlayer.create(context, path);
            }



            /*
            mediaPlayer = MediaPlayer.create(context, song_id);
            */
            launchActivity(songName, context, classTo);

        }
    }



    public void launchActivity(String songName, Context activity1, Class classTo) {
        intent = new Intent(activity1, classTo);
        intent.putExtra("songName", songName);
        activity1.startActivity(intent);
    }

    public String getCurrentSong() {
        return currentSong;
    }


    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public void setCurrentSong(String newSong) {
        currentSong = newSong;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

}
