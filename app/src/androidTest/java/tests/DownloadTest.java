package tests;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.test.rule.ActivityTestRule;

import com.develop.awong.musicplayer2.Download;
import com.develop.awong.musicplayer2.MenuActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;



/**
 * Created by parkn on 3/8/2018.
 */

public class DownloadTest {
    Download download;

    long songID;
    long songID2;
    Context context;

    @Rule
    public ActivityTestRule<MenuActivity> menuActivity = new ActivityTestRule<MenuActivity>(MenuActivity.class);


    @Before
    public void setup() {
        download = new Download(menuActivity.getActivity());
    }

    @Test
    public void testDownloadData() {
        Uri sampleSong = Uri.parse("http://androidtutorialpoint.comli.com/DownloadManager/AndroidDownloadManager.mp3");
        songID = download.DownloadData(sampleSong,menuActivity.getActivity(),"Sample.mp3");
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "Sample.mp3");
        assertFalse(file.exists());
    }

    @Test
    public void testDownloadData2(){
        Uri sampleSong = Uri.parse("https://www.dropbox.com/s/fg7gc1tmdl1jre8/transmission-002-the-blackhole.mp3?dl=1");
        songID2 = download.DownloadData(sampleSong,menuActivity.getActivity(),"hi.mp3");
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "hi.mp3");
        assertFalse(file.exists());
    }

    @Test
    public void testAlbum(){
        Uri sampleAlbum = Uri.parse("https://www.dropbox.com/s/pd8bcp31w6hjiqj/in-which-we-drift-endlessly.zip?dl=1");
        download.DownloadData(sampleAlbum,menuActivity.getActivity(),"Sample.zip");
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "Sample.zip");
        //String str = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + file.getName();
        try {
            MenuActivity.extractAlbum(file,path,"https://www.dropbox.com/s/pd8bcp31w6hjiqj/in-which-we-drift-endlessly.zip?dl=1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(file.exists());
    }

}
