package tests;

import com.develop.awong.musicplayer2.Song;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by cleve on 3/16/2018.
 */

public class PopTest {
    public Song song;

    @Before
    public void setup(){
        song = new Song();
        song.setUser("Bob");
        song.setUserEmail("bob@gmail.com");
    }

    @Test
    public void testUserName(){
        String value = song.getUser();
        assertEquals("Bob",value);
    }

    @Test
    public void testUserEmail(){
        String value = song.getUserEmail();
        assertEquals("bob@gmail.com",value);
    }
}
