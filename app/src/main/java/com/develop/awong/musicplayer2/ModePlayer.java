package com.develop.awong.musicplayer2;

import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by AWong on 3/4/18.
 */

public interface ModePlayer {
    public void populatePlaylist();
    public PriorityQueue<Song> getPlaylist();
    public void setFireBaseSongs(List<Song> songs);

}
