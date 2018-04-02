package com.develop.awong.musicplayer2;

import android.location.Location;
import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.util.Calendar;

/**
 * Created by parkn on 2/11/2018.
 */

public class Song {
    private Location location;
    private double latitude;
    private double longitude;
    private Calendar cal;

    private int priority;
    private int randNum;  //used for priority_queue sorting
    private int score;

    private String timeInMillis;
    private String fileName;
    private String songTitle;
    private String albumName;
    private String artist;
    private String user;
    private String userEmail;
    private byte[] albumArt;

    private String link;
    private String path;

    //default constructor
    public Song() {
        this.location = null;
        this.cal = null;
        this.link = null;
        this.path = null;

        this.priority = 1;
        this.randNum = 0;
        this.score = 0;
        this.fileName = "Unknown";
        this.songTitle = "Unknown";
        this.albumName = "Unknown";
        this.artist = "Unknown";
        this.user = "Unknown";
        this.userEmail = "Unknown";

    }

    public Song(String fileName, String songTitle, String album, String artist, byte[] albumArt,
                String user, String userEmail, String Link, String path) {
        this.location = null;
        this.cal = null;

        this.priority = 1;
        this.randNum = 0;
        this.score = 0;
        this.fileName = fileName;
        this.songTitle = songTitle;
        this.albumName = album;
        this.artist = artist;
        this.albumArt = albumArt;
        this.user = user;
        this.userEmail = userEmail;
        this.link = Link;
        this.path = path;
    }





    public void setLink(String Link) {this.link = Link;}

    public void setPath(String Path) {this.path = Path;}

    @Exclude
    public void setCal(Calendar c) {
        this.cal = c;
        this.timeInMillis = Long.toString(c.getTimeInMillis());
    }

    @Exclude
    public Calendar getCal() {
        return this.cal;
    }

    @Exclude
    public void setLocation(Location loc) {
        this.location = loc;
        setLatitude(loc.getLatitude());
        setLongitude(loc.getLongitude());
    }

    @Exclude
    public Location getLocation() {
        return location;
    }

    // set the priority lv ranging from 0(disliked) to 2(favorite)
    public void setPriority(int lv) {
        this.priority = lv;
    }

    //getter for priority lv
    public int getPriority() {
        return this.priority;
    }

    public void setRandNum(int rand) {
        this.randNum = rand;
    }

    public int getRandNum() {
        return this.randNum;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongTitle() {
        return this.songTitle;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setTimeInMillis(String timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Exclude
    public byte[] getAlbumArt() {
        return this.albumArt;
    }


    @Exclude
    public void setAlbumArt(byte[] art) {
        this.albumArt = art;
    }

    public String getTimeInMillis() {
        return this.timeInMillis;
    }


    public String getUser() {
        return this.user;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getLink() {return this.link;}

    public String getPath() {return this.path;}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
