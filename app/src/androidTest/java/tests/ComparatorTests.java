package tests;

import android.support.test.rule.ActivityTestRule;

import com.develop.awong.musicplayer2.MainActivity;
import com.develop.awong.musicplayer2.MenuActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jeremyjiang on 3/10/18.
 */

public class ComparatorTests {
    List<String> listSongs;
    List<String> testList;

    int numSongs;
    String lastSong;
    String firstSong;
    String song1;
    String song2;
    String song3;


    String newFirstSong;
    String newSecSong;
    String newThirdSong;
    String newLastSong;

    @Rule
    public ActivityTestRule<MenuActivity> menuActivity = new ActivityTestRule<MenuActivity>(MenuActivity.class);

    @Before
    public void setup() {

        numSongs = MenuActivity.getlistTitles().size();
        listSongs = MenuActivity.getlistTitles();

        lastSong = MenuActivity.getlistTitles().get(numSongs - 1);
        firstSong = MenuActivity.getlistTitles().get(0);

        testList = new ArrayList<>();

        //Artist: Keaton Simons, Album: New & Best of Keaton Simons
        song1 = "123 Go";

        //Artist: Terry Oldfield, Album: Origins - The Best of Terry Oldfield
        song2 = "After The Storm";

        //Artist: Forum, Album: Take Yourself Too Seriously
        song3 = "Dead Dove Do Not Eat";

        testList.add(song1);
        testList.add(song2);
        testList.add(song3);


    }

    @Test
    public void testSortTitles() {

        //Descending Sort Test for Titles
        Collections.sort(listSongs, MainActivity.SongDescComparator);
        newFirstSong = listSongs.get(0);
        newLastSong = listSongs.get(numSongs - 1);

        //The first song is now the last song of the original list
        assertEquals(newFirstSong, lastSong);
        assertEquals(newLastSong, firstSong);
        assertEquals(listSongs.size(), numSongs);

        //Ascending sort test after descending for titles
        Collections.sort(listSongs, MainActivity.SongAscComparator);
        newFirstSong = listSongs.get(0);
        newLastSong = listSongs.get(numSongs - 1);

        //The first song is now the first song of the original list
        assertEquals(newFirstSong, firstSong);
        assertEquals(newLastSong, lastSong);
        assertEquals(listSongs.size(), numSongs);
    }

    @Test
    public void testSortArtists() {

        //Descending Sort Test for Artists
        Collections.sort(testList, MainActivity.ArtistDescComparator);
        newFirstSong = testList.get(0);
        newSecSong = testList.get(1);
        newThirdSong = testList.get(2);

        //Terry, Keaton, Forum
        assertEquals(song2,newFirstSong);
        assertEquals(song1,newSecSong);
        assertEquals(song3,newThirdSong);

        //Ascending sort test for Artists
        Collections.sort(testList, MainActivity.ArtistAscComparator);
        newFirstSong = testList.get(0);
        newSecSong = testList.get(1);
        newThirdSong = testList.get(2);

        //Forum, Keaton, Terry
        assertEquals(song3,newFirstSong);
        assertEquals(song1,newSecSong);
        assertEquals(song2,newThirdSong);

    }

    @Test
    public void testSortAlbums() {
        //Descending Sort Test for Albums
        Collections.sort(testList, MainActivity.AlbumDescComparator);
        newFirstSong = testList.get(0);
        newSecSong = testList.get(1);
        newThirdSong = testList.get(2);

        //3. Take, 2. Origins, 1. New
        assertEquals(song3,newFirstSong);
        assertEquals(song2,newSecSong);
        assertEquals(song1,newThirdSong);

        //Ascending sort test for Artists
        Collections.sort(testList, MainActivity.AlbumAscComparator);
        newFirstSong = testList.get(0);
        newSecSong = testList.get(1);
        newThirdSong = testList.get(2);

        //New, Origins, Take
        assertEquals(song1,newFirstSong);
        assertEquals(song2,newSecSong);
        assertEquals(song3,newThirdSong);
    }

    @Test
    public void testSortFavorites() {
        newFirstSong = testList.get(0);
        newSecSong = testList.get(1);
        newThirdSong = testList.get(2);

        MenuActivity.songMap.get(newFirstSong).setPriority(2);
        MenuActivity.songMap.get(newSecSong).setPriority(1);
        MenuActivity.songMap.get(newThirdSong).setPriority(0);

        //Descending Sort Test for Favorites
        Collections.sort(testList, MainActivity.FavDescComparator);
        newFirstSong = testList.get(0);
        newSecSong = testList.get(1);
        newThirdSong = testList.get(2);

        assertEquals(song1,newFirstSong);
        assertEquals(song2,newSecSong);
        assertEquals(song3,newThirdSong);

        //Descending Sort Test for Favorites
        Collections.sort(testList, MainActivity.FavAscComparator);
        newFirstSong = testList.get(0);
        newSecSong = testList.get(1);
        newThirdSong = testList.get(2);

        assertEquals(song3,newFirstSong);
        assertEquals(song2,newSecSong);
        assertEquals(song1,newThirdSong);

    }
}
