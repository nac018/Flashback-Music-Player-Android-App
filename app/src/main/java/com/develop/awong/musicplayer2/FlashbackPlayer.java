package com.develop.awong.musicplayer2;

import java.util.*;
import android.location.Location;

/**
 * Created by parkn on 2/11/2018.
 */

public class FlashbackPlayer implements ModePlayer {
    private Location currentLoc;
    private HashMap<String,Song> allSongs;
    private PriorityQueue<Song> playlist;

    public FlashbackPlayer(Location loc, HashMap<String,Song> s){
        this.allSongs = s;
        this.playlist = new PriorityQueue<Song>(11, new PriorityComparator());
        this.currentLoc = loc;
    }

    // populate playlist from allSongs
    public void populatePlaylist(){
        /* this for-loop would determine what songs in the allSongs list is a
           candidate for the playlist in flashback mode*/
        for (Map.Entry<String,Song> entry : allSongs.entrySet()){
            Song curSong = entry.getValue();
            Random rand = new Random();
            if (curSong.getLocation() == null) continue;
            if (currentLoc.distanceTo(curSong.getLocation()) <= 300){
                if (curSong.getPriority() == 1) {
                    playlist.add(curSong);
                    int temp = rand.nextInt(2999);
                    curSong.setRandNum(temp);
                } else if (curSong.getPriority() == 2){
                    playlist.add(curSong);
                    int temp = rand.nextInt(7000)+3000;
                    curSong.setRandNum(temp);
                }
            } else {
                continue;
            }
        }
    }

    //getter for playlist
    public PriorityQueue<Song> getPlaylist(){
        return playlist;
    }

    public void setFireBaseSongs(List<Song> songs){}

}
