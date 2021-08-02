package com.perez.downloader.model;

public class VideoTaskState {
    public static final int DEFAULT = 0;
    public static final int PENDING = -1;
    public static final int PREPARE = 1;
    public static final int START = 2;
    public static final int DOWNLOADING = 3;
    public static final int PROXYREADY = 4; // Video can be played while being downloaded
    public static final int SUCCESS = 5;
    public static final int ERROR = 6;
    public static final int PAUSE = 7;
}
