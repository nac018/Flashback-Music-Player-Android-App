package tests;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.develop.awong.musicplayer2.LoginActivity;
import com.develop.awong.musicplayer2.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by cleve on 3/15/2018.
 */

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivity = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private TextView signOutTextView;
    private TextView menuTextView;

    @Before
    public void setup(){
        signOutTextView = (TextView) loginActivity.getActivity().findViewById(R.id.sign_out_button);
        menuTextView = (TextView) loginActivity.getActivity().findViewById(R.id.menu_button);
    }

    @Test
    public void testSignOutBtn(){
        String value = signOutTextView.getText().toString();
        assertEquals("Sign out",value);
    }

    @Test
    public void testMenuBtn(){
        String value = menuTextView.getText().toString();
        assertEquals("Menu",value);
    }
}
