package com.develop.awong.musicplayer2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AWong on 2/11/18.
 */

public class Album {


    List<String> songTitles = new ArrayList<>();
    List<String> fileNames = new ArrayList<>();
    private String albumName;

    public Album(String albumName) {
        this.albumName = albumName;
    }

    protected void addSong(String newSong) {
        fileNames.add(newSong);
    }

    protected void addSongTitle(String newSongTitle) {
        songTitles.add(newSongTitle);
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
