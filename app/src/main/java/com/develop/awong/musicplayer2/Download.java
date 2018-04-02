package com.develop.awong.musicplayer2;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by parkn on 3/6/2018.
 */

public final class Download {
    private DownloadManager downloadManager;
    private boolean success = false;

    public Download(Context v) {
        downloadManager = (DownloadManager)v.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public long DownloadData (Uri uri,Context context, String songName) {

        long downloadReference;

        // Create request for android download manager
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setTitle(songName)
                .setDescription("Song download via MusicPlayer.")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverRoaming(false)
                .setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS,songName);

        request.allowScanningByMediaScanner();

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);
        return downloadReference;
    }



    public void cancelDownload (long downloadID) {
        downloadManager.remove(downloadID);
    }

}