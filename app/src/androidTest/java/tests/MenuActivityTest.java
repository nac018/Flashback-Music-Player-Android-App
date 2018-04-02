package tests;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.develop.awong.musicplayer2.MenuActivity;
import com.develop.awong.musicplayer2.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MenuActivityTest {

    @Rule
    public ActivityTestRule<MenuActivity> menuActivity = new ActivityTestRule<MenuActivity>(MenuActivity.class);

    private TextView albumTextView;
    private TextView songTextView;
    private ToggleButton vibeTextView;


    @Before
    public void setup() {
        albumTextView = (TextView) menuActivity.getActivity().findViewById(R.id.albums);
        songTextView = (TextView) menuActivity.getActivity().findViewById(R.id.songs);
        vibeTextView = (ToggleButton) menuActivity.getActivity().findViewById(R.id.vibe);
    }

    @Test
    public void testVibeBtnOn(){
        String value = vibeTextView.getTextOff().toString();
        assertEquals("Vibe on",value);
    }

    @Test
    public void testVibeBtnOff(){
        String value = vibeTextView.getTextOn().toString();
        assertEquals("Vibe off",value);
    }

    @Test
    public void testAlbumBtn() {
        String value = albumTextView.getText().toString();
        assertEquals("Albums", value);
    }

    @Test
    public void testSongsBtn() {
        String value = songTextView.getText().toString();
        assertEquals("Songs", value);
    }

}
