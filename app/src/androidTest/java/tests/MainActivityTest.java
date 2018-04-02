package tests;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.develop.awong.musicplayer2.MainActivity;
import com.develop.awong.musicplayer2.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by AWong on 2/18/18.
 */

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    private TextView menuTextView;

    @Before
    public void setup() {
        menuTextView = (TextView) mainActivity.getActivity().findViewById(R.id.menuButton);
    }

    @Test
    public void testMenuBtn() {
        String value = menuTextView.getText().toString();
        assertEquals("Menu", value);
    }


}
