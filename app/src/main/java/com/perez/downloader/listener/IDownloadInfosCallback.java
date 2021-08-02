package com.perez.downloader.listener;

import com.perez.downloader.model.VideoTaskItem;

import java.util.List;

public interface IDownloadInfosCallback {

    void onDownloadInfos(List<VideoTaskItem> items);
}
