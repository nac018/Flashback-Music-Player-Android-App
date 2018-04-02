package tests;

import android.location.Location;

import com.develop.awong.musicplayer2.Song;
import com.develop.awong.musicplayer2.TimeMachine;
import com.develop.awong.musicplayer2.VibePlayer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static junit.framework.Assert.assertEquals;

/**
 * Created by AWong on 3/16/18.
 */

public class PriorityComparatorTest {
    VibePlayer vibePlayer; //Vibe player only used for its priority queue.
    Location loc;

    Song song1;
    Song song2;
    Song song3;

    List<String> song1Friends;
    List<String> song2Friends;
    List<String> song3Friends;

    String time;
    long currTime;

    @Before
    public void setup() {
        loc =  new Location("");
        loc.setLatitude(0);
        loc.setLongitude(0);
        time = "0";
        currTime = Long.parseLong(time) ;

        vibePlayer = new VibePlayer(loc, null);

        //Artist: Keaton Simons, Album: New & Best of Keaton Simons
        song1 = new Song();
        song1.setSongTitle("123 Go");
        song1.setLocation(loc);
        song1.setUserEmail("Anthony@");
        song1.setTimeInMillis("1000");

        song1Friends = new ArrayList<>();
        song1Friends.add("Sheng@");
        song1Friends.add("Nang@");



        //Artist: Terry Oldfield, Album: Origins - The Best of Terry Oldfield
        song2 = new Song();
        song2.setSongTitle("After the Storm");
        song2.setLocation(loc);
        song2.setUserEmail("Sheng@");
        song2.setTimeInMillis("1000");

        song2Friends = new ArrayList<>();
        song2Friends.add("Anthony@");

        //Artist: Forum, Album: Take Yourself Too Seriously
        song3 = new Song();
        song3.setSongTitle("Dead Dove Do Not Eat");
        song3.setLocation(loc);
        song3.setUserEmail("Nang@");
        song3.setTimeInMillis("1000");

        song3Friends = new ArrayList<>();
    }

    @Test
    public void testAddedInOrder() {
        //Current user Anthony, Location 0 0, time 0

        PriorityQueue<Song> priorityQueue = vibePlayer.getPlaylist();
        String currentUserEmail = "Anthony@";


        Location newLoc =  new Location("");
        newLoc.setLatitude(50);
        newLoc.setLongitude(50);

        vibePlayer.scoreGenerator(loc, currTime, newLoc, currentUserEmail, song1);
        vibePlayer.scoreGenerator(loc, currTime, newLoc, currentUserEmail, song2);
        vibePlayer.scoreGenerator(loc, currTime, newLoc, currentUserEmail, song3);

        //Songs are "played"
        priorityQueue.add(song1);
        priorityQueue.add(song2);
        priorityQueue.add(song3);

        assertEquals(song1.getSongTitle(), priorityQueue.poll().getSongTitle());
        assertEquals(song3.getSongTitle(), priorityQueue.poll().getSongTitle());
        assertEquals(song2.getSongTitle(), priorityQueue.poll().getSongTitle());

        String newtime = "1234567891012";
        song3.setTimeInMillis(newtime);

        vibePlayer.scoreGenerator(loc, currTime, newLoc, currentUserEmail, song1);
        vibePlayer.scoreGenerator(loc, currTime, newLoc, currentUserEmail, song2);
        vibePlayer.scoreGenerator(loc, currTime, newLoc, currentUserEmail, song3);

        priorityQueue.add(song1);
        priorityQueue.add(song2);
        priorityQueue.add(song3);

        assertEquals(song1.getSongTitle(), priorityQueue.poll().getSongTitle());
        assertEquals(song2.getSongTitle(), priorityQueue.poll().getSongTitle());
        assertEquals(song3.getSongTitle(), priorityQueue.poll().getSongTitle());
    }



}
