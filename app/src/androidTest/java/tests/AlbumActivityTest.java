package tests;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.develop.awong.musicplayer2.AlbumActivity;
import com.develop.awong.musicplayer2.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by AWong on 2/18/18.
 */

public class AlbumActivityTest {
    @Rule
    public ActivityTestRule<AlbumActivity> albumActivity = new ActivityTestRule<AlbumActivity>(AlbumActivity.class);

    private TextView albumTextView;

    @Before
    public void setup() {
        albumTextView = (TextView) albumActivity.getActivity().findViewById(R.id.menuButton2);
    }

    @Test
    public void testMenuBtn() {
        String value = albumTextView.getText().toString();
        assertEquals("Menu", value);
    }

}
