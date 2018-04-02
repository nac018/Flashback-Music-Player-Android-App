package tests;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.develop.awong.musicplayer2.R;
import com.develop.awong.musicplayer2.TrackListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class TrackListActivityTest {

    @Rule
    public ActivityTestRule<TrackListActivity> trackListActivity
            = new ActivityTestRule<TrackListActivity>(TrackListActivity.class);

    private TextView popBtnTextView;
    private TextView menuTextView;
    List<String> listSongs;
    String firstSong;

    @Before
    public void setup(){
        popBtnTextView = (TextView) trackListActivity.getActivity().findViewById(R.id.currentTrack);
        menuTextView = (TextView) trackListActivity.getActivity().findViewById(R.id.menuBtnCurTracks);
        listSongs = new ArrayList<>();
        firstSong = "123 Go";
        listSongs.add(firstSong);
        listSongs.add("After The Storm");
        listSongs.add("Dead Dove Do Not Eat");
    }

    @Test
    public void testPopBtnTextView(){
        String value = popBtnTextView.getText().toString();
        assertEquals("Currently Playing: ", value);
    }

    @Test
    public void testMenuTextView(){
        String value = menuTextView.getText().toString();
        assertEquals("Menu", value);
    }

    @Test
    public void testSize(){
        assertEquals(3, listSongs.size());
    }

    @Test
    public void testSongName(){
        assertEquals("123 Go", listSongs.get(0));
    }

}
