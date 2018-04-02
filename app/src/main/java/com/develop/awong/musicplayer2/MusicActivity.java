package com.develop.awong.musicplayer2;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicActivity extends AppCompatActivity {

    Button playBtn;
    Button detailsButton;
    Button backBtn;
    protected static SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    protected static ImageView albumArt;
    protected static TextView songTitle;
    protected static MediaPlayer mediaPlayer;
    protected static int totalTime;
    private Context mContext;
    String songName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_music);

        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);


        // Get the application context
        mContext = getApplicationContext();

        detailsButton = (Button) findViewById(R.id.details);

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MusicActivity", "onClick: User clicks the details button");
                startActivity(new Intent(MusicActivity.this, Pop.class));
            }
        });

        //Back Button
        backBtn = (Button) findViewById(R.id.backBtn);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MusicActivity", "onClick: User clicks BACK");
                Intent i = new Intent(MusicActivity.this, MainActivity.class);
                i.putExtra("songName", songName);

                MusicActivity.this.startActivity(i);
                finish();
            }
        });

        String currentSongTitle = MenuActivity.musicPlayer.getSongTitle();

        //Song Title set
        songTitle = (TextView) findViewById(R.id.songTitle);
        songTitle.setText(currentSongTitle);

        //Album Art set
        albumArt = (ImageView) findViewById(R.id.art);
        byte[] data = MenuActivity.songMap.get(currentSongTitle).getAlbumArt();

        if(data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            albumArt.setImageBitmap(bitmap);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String songName = extras.getString("songName");
            int song_id = this.getResources().getIdentifier(songName, "raw", this.getPackageName());
        }

        //Media Player set up

        if(MenuActivity.musicPlayer != null && MenuActivity.musicPlayer.getMediaPlayer() != null) {
            mediaPlayer = MenuActivity.musicPlayer.getMediaPlayer();

            mediaPlayer.setLooping(false);
            totalTime = mediaPlayer.getDuration();
            playBtn.setBackgroundResource(R.drawable.stop);
            mediaPlayer.start();
        }

        //Position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            Log.d("MusicActivity", "onClick: User clicks the music progress seek bar");
                            mediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }

        );


        //Volume Bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Log.d("MusicActivity", "onClick: User clicks the volume seek bar");
                        float volumeNum = progress / 100f;
                        mediaPlayer.setVolume(volumeNum, volumeNum);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Thread (Update positionBar and timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    //Handler that handles the seekbar for the position of our song.
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            //Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    //Creates the time and remaining time for song.
    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public void playBtnClick(View view) {
        if(!mediaPlayer.isPlaying()) {
            //Stopping
            mediaPlayer.start();
            Log.d("MusicActivity", "onClick: User clicks the play button");
            playBtn.setBackgroundResource(R.drawable.stop);
        } else {
            //Playing
            mediaPlayer.pause();
            Log.d("MusicActivity", "onClick: User clicks the stop button");
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }

    public void skipBtnClick(View view) {
        if(mediaPlayer != null) {
            mediaPlayer.seekTo(totalTime);
        }
    }

    public void backSkipClick(View view) {
        if(mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    public void loadMedia (int resourceId){
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }

        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(resourceId);
        try{
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
        }catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
