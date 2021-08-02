package com.perez.peliculas;

import android.app.Application;

import com.perez.downloader.common.DownloadConstants;
import com.perez.downloader.VideoDownloadConfig;
import com.perez.downloader.VideoDownloadManager;
import com.perez.downloader.utils.VideoStorageUtils;

import java.io.File;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        File file = VideoStorageUtils.getVideoCacheDir();
        VideoDownloadConfig config = new VideoDownloadManager.Build(this)
                .setCacheRoot(file.getAbsolutePath())
                .setTimeOut(DownloadConstants.READ_TIMEOUT, DownloadConstants.CONN_TIMEOUT)
                .setConcurrentCount(DownloadConstants.CONCURRENT)
                .setIgnoreCertErrors(true)
                .setShouldM3U8Merged(true)
                .buildConfig();
        VideoDownloadManager.getInstance().initConfig(config);
    }
}
