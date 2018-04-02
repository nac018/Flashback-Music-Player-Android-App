package tests;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.develop.awong.musicplayer2.CalendarActivity;
import com.develop.awong.musicplayer2.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by cleve on 3/15/2018.
 */

public class CalendarActivityTest {
    @Rule
    public ActivityTestRule<CalendarActivity> calendarActivity = new ActivityTestRule<CalendarActivity>(CalendarActivity.class);

    private TextView setTextView;
    private TextView tvTextView;
    public String str = "year: 2018 \n month: 8 \n day: 12";
    public String val;
    @Before
    public void setup(){
        setTextView = (TextView) calendarActivity.getActivity().findViewById(R.id.b_pick);
        tvTextView = (TextView) calendarActivity.getActivity().findViewById(R.id.tv_result);
    }

    @Test
    public void testSetBtn() {
        String value = setTextView.getText().toString();
        assertEquals("Set", value);
    }

    @Test
    public void testTV(){
        String value = tvTextView.getText().toString();
        val = str;
        assertEquals("year: 2018 \n month: 8 \n day: 12",val);
    }

    @Test
    public void test(){
        assertEquals(null,CalendarActivity.c);
    }


}
