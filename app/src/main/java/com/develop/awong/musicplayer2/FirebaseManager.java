package com.develop.awong.musicplayer2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by AWong on 3/13/18.
 */

public class FirebaseManager {

    public static FirebaseDatabase database;
    DatabaseReference myFirebaseRef;
    DataSnapshot contactSnapshot;

    List<Song> fireBaseSongs;

    public FirebaseManager() {
        fireBaseSongs = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myFirebaseRef = database.getReference();
    }

    public void submit(Song songObject, Activity activity) {
        DatabaseReference newPostRef = myFirebaseRef.push();
        newPostRef.setValue(songObject);

        Intent output = new Intent();
        activity.setResult(RESULT_OK, output);
    }

    public void retrieve(final ModePlayer vibePlayer)
    {
        DatabaseReference ref = myFirebaseRef;

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Song a  = dsp.getValue(Song.class);
                    System.out.println("song  a title : "+ a.getSongTitle());
                    System.out.println("user : "+ a.getUser());

                    fireBaseSongs.add(dsp.getValue(Song.class));


                }

                if(fireBaseSongs.size() == 0) {
                    Log.d("Firebase is empty", "Play some songs");
                }
                else {
                    System.out.println("size" + fireBaseSongs.size());

                    vibePlayer.setFireBaseSongs(fireBaseSongs);
                    vibePlayer.populatePlaylist();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });



    }


    public void setFireBaseSongs(List<Song> fireBaseSongs) {
        this.fireBaseSongs = fireBaseSongs;
    }
}
