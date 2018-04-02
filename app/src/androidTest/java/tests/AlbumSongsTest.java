package tests;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.develop.awong.musicplayer2.AlbumSongs;
import com.develop.awong.musicplayer2.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by AWong on 2/18/18.
 */

public class AlbumSongsTest {
    @Rule
    public ActivityTestRule<AlbumSongs> albumSongs = new ActivityTestRule<AlbumSongs>(AlbumSongs.class);

    private TextView albumSongsTextView;

    @Before
    public void setup() {
        albumSongsTextView = (TextView) albumSongs.getActivity().findViewById(R.id.albumMenuBtn);
    }

    @Test
    public void testMenuBtn() {
        String value = albumSongsTextView.getText().toString();
        assertEquals("Album Menu", value);
    }

}
