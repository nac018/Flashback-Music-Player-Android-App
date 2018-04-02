package com.develop.awong.musicplayer2;

import java.util.Comparator;

/**
 * Created by parkn on 2/11/2018.
 */

public class PriorityComparator implements Comparator<Song> {
    @Override
    public int compare(Song a, Song b) {
        if (a.getScore() < b.getScore()) {
            return 1;
        } else if (a.getScore() > b.getScore()) {
            return -1;
        }
        return 0;
    }
}
