package com.develop.awong.musicplayer2;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by jeremyjiang on 3/16/18.
 */

public class VibeListPlayer {
    int count = 0;
    MediaPlayer mediaPlayer;
    Context context;
    ImageView albumArt;
    public static PriorityQueue<Song> playListPQ;
    private PriorityQueue<Song> downloadPQ;
    private Download download;




    public VibeListPlayer (PriorityQueue<Song> playListPQ, Context context) {
        download = MenuActivity.download;

        MenuActivity.playListPQ = playListPQ;
        this.playListPQ = MenuActivity.playListPQ;
        this.downloadPQ = new PriorityQueue<Song>(11, new PriorityComparator());
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
            System.out.println("pq size: "+ playListPQ.size());
            //String songTitle = listOfSongTitles.get(count);
            String songTitle = playListPQ.peek().getSongTitle();

            MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
            MenuActivity.musicPlayer.setSongTitle(songTitle);

            setMusicActivity(songTitle);

            Song songObject = MenuActivity.songMap.get(songTitle);
            MainActivity.fileName = songObject.getFileName();
            MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());

            System.out.println("11111 pq size: "+ playListPQ.size());
            // skip function
            while ((songObject.getPriority() == 0 && playListPQ.size() > 1) || (!isFilePresent(songObject)  && playListPQ.size() > 1)) {
                System.out.println(" in while pq size: "+ playListPQ.size());
                if (!isFilePresent(songObject)) {

                    //long songID = download.DownloadData(Uri.parse(songObject.getLink()),context,songObject.getSongTitle());

                    playListPQ.poll();
                    //go download
                    long refid = download.DownloadData(Uri.parse(songObject.getLink()),context,songObject.getFileName());
                    System.out.println("start downloading: " + songObject.getSongTitle());


                    MenuActivity.downloadList.add(refid);
                    MenuActivity.downloadSongList.add(songObject);


                }
                else if (songObject.getPriority() == 0) {
                    playListPQ.poll();
                }
                //count++;
                MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
                //songTitle = listOfSongTitles.get(count);
                songTitle = playListPQ.peek().getSongTitle();

                //Updates Song Title text on UI
                setMusicActivity(songTitle);


                MenuActivity.musicPlayer.setSongTitle(songTitle);
                songObject = MenuActivity.songMap.get(songTitle);
                MainActivity.fileName = songObject.getFileName();
                MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());
            }
            System.out.println("22222 pq size: "+ playListPQ.size());

            boolean canplay = true;

            if((songObject.getPriority() == 0 && playListPQ.size() == 1)
                    || (!isFilePresent(songObject) && playListPQ.size() == 1)){

                if (!isFilePresent(songObject)) {
                    System.out.println("1st file does not exist");
                    playListPQ.poll();

                    //playListPQ.poll();
                    //go download
                    long refid = download.DownloadData(Uri.parse(songObject.getLink()),context,songObject.getFileName());
                    System.out.println("start downloading: " + songObject.getSongTitle());


                    MenuActivity.downloadList.add(refid);
                    MenuActivity.downloadSongList.add(songObject);


                }
                canplay = false;

            }

            if (canplay) {

                Uri path = Uri.parse(MenuActivity.songMap.get(songTitle).getPath());
                if(path.toString().equalsIgnoreCase("raw")) {
                    int resourceId = getSongId(songObject.getSongTitle());

                    AssetFileDescriptor assetFileDescriptor = context.getResources().openRawResourceFd(resourceId);
                    try {
                        MenuActivity.musicPlayer.getMediaPlayer().setDataSource(assetFileDescriptor);
                        MenuActivity.musicPlayer.getMediaPlayer().prepare();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
                else {
                    try {
                        MenuActivity.musicPlayer.getMediaPlayer().setDataSource(context,path);
                        MenuActivity.musicPlayer.getMediaPlayer().prepare();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }

                }



                System.out.println("first time pq size "+playListPQ.size() +"  current song "+ songTitle );
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
            playListPQ.poll();



            MenuActivity.musicPlayer.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (playListPQ.size() > 0) {
                        mp.stop();
                        mp.reset();

                        System.out.println("pq size here  " + playListPQ.size());

                        MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
                        String songTitle = playListPQ.peek().getSongTitle();
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

                        System.out.println("cur song : " + songObject.getSongTitle() + " is fire present??  " + isFilePresent(songObject) );
                        System.out.println("file path: " + songObject.getPath() );
                        while ((songObject.getPriority() == 0 && playListPQ.size() > 1) || (!isFilePresent(songObject)  && playListPQ.size() > 1)) {
                            if (!isFilePresent(songObject)) {
                                System.out.println("file does not exist");
                                playListPQ.poll();
                                //go download
                                long refid = download.DownloadData(Uri.parse(songObject.getLink()),context,songObject.getFileName());
                                System.out.println("start downloading: " + songObject.getSongTitle());


                                MenuActivity.downloadList.add(refid);
                                MenuActivity.downloadSongList.add(songObject);



                            }
                            else if (songObject.getPriority() == 0) {
                                playListPQ.poll();
                            }
                            //count++;
                            MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
                            //songTitle = listOfSongTitles.get(count);
                            songTitle = playListPQ.peek().getSongTitle();

                            //Updates Song Title text on UI
                            setMusicActivity(songTitle);


                            MenuActivity.musicPlayer.setSongTitle(songTitle);
                            songObject = MenuActivity.songMap.get(songTitle);
                            MainActivity.fileName = songObject.getFileName();
                            MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());
                        }


                        if((songObject.getPriority() == 0 && playListPQ.size() == 1) || (!isFilePresent(songObject) && playListPQ.size() == 1)){
                            System.out.println("LAST SONG IS PLAYING");
                            mp.pause();

                            long refid = download.DownloadData(Uri.parse(songObject.getLink()),context,songObject.getFileName());
                            System.out.println("start downloading: " + songObject.getSongTitle());

                            while(!isFilePresent(songObject)) {
                                System.out.println("Downloading");
                            }
                            System.out.println("Done Downloading");
                        }

                        System.out.println(MenuActivity.songMap.get(songTitle).getPath());
                        Uri path = Uri.parse(MenuActivity.songMap.get(songTitle).getPath());

                        if(path.toString().equalsIgnoreCase("raw")) {
                            int resourceId = getSongId(songObject.getSongTitle());

                            AssetFileDescriptor assetFileDescriptor = context.getResources().openRawResourceFd(resourceId);
                            try {
                                System.out.println("First Try for set data");
                                MenuActivity.musicPlayer.getMediaPlayer().setDataSource(assetFileDescriptor);
                                MenuActivity.musicPlayer.getMediaPlayer().prepare();
                                System.out.println("End first Try for set data");

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }
                        }
                        else {
                            try {
                                System.out.println("Second Try for set data");
                                MenuActivity.musicPlayer.getMediaPlayer().setDataSource(context,path);
                                System.out.println("After set data source");
                                MenuActivity.musicPlayer.getMediaPlayer().prepare();
                                System.out.println("End Second Try for set data");

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }

                        }
                        System.out.println("pq size "+playListPQ.size() +"  current song "+ songTitle );
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

                        playListPQ.poll();
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
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                albumArt.setImageBitmap(bitmap);
            }
        }
    }


    public int getSongId(String songTitle) {
        //String songTitle = listOfSongTitles.get(count);
        Song song = MenuActivity.songMap.get(songTitle);
        String songName = song.getFileName();
        int song_id = context.getResources().getIdentifier(songName, "raw",
                context.getPackageName());
        return song_id;
    }


    public boolean isFilePresent(Song songObject) {
        if (songObject.getPath().equalsIgnoreCase("raw")) {
            return true;
        }
        String path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + songObject.getFileName();
        File file = new File(path);
        return file.exists();
    }


}


