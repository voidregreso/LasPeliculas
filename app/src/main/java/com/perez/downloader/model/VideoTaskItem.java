package com.perez.downloader.model;

import com.perez.downloader.m3u8.M3U8;
import com.perez.downloader.utils.VideoDownloadUtils;
import com.perez.downloader.utils.VideoStorageUtils;

public class VideoTaskItem implements Cloneable {

    private String mUrl;                 
    private String mCoverUrl;            
    private String mCoverPath;           
    private String mTitle;               
    private String mGroupName;           
    private long mDownloadCreateTime;    
    private int mTaskState;              
    private String mMimeType;            
    private String mFinalUrl;            
    private int mErrorCode;              
    private int mVideoType;              
    private M3U8 mM3U8;                  
    private int mTotalTs;                
    private int mCurTs;                  
    private float mSpeed;                
    private float mPercent;              
    private long mDownloadSize;          
    private long mTotalSize;             
    private String mFileHash;            
    private String mSaveDir;             
    private boolean mIsCompleted;        
    private boolean mIsInDatabase;       
    private long mLastUpdateTime;        
    private String mFileName;            
    private String mFilePath;            
    private boolean mPaused;

    public VideoTaskItem(String url) {
        this(url, "", "", "");
    }

    public VideoTaskItem(String url, String coverUrl, String title, String groupName) {
        mUrl = url;
        mCoverUrl = coverUrl;
        mTitle = title;
        mGroupName = groupName;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setCoverUrl(String coverUrl) { mCoverUrl = coverUrl; }

    public String getCoverUrl() { return mCoverUrl; }

    public void setCoverPath(String coverPath) { mCoverPath = coverPath; }

    public String getCoverPath() { return mCoverPath; }

    public void setTitle(String title) { mTitle = title; }

    public String getTitle() { return mTitle; }

    public void setGroupName(String groupName) { mGroupName = groupName; }

    public String getGroupName() { return mGroupName; }

    public void setDownloadCreateTime(long time) {
        mDownloadCreateTime = time;
    }

    public long getDownloadCreateTime() {
        return mDownloadCreateTime;
    }

    public void setTaskState(int state) {
        mTaskState = state;
    }

    public int getTaskState() {
        return mTaskState;
    }

    public void setMimeType(String mimeType) {
        mMimeType = mimeType;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public void setFinalUrl(String finalUrl) {
        mFinalUrl = finalUrl;
    }

    public String getFinalUrl() {
        return mFinalUrl;
    }

    public void setErrorCode(int errorCode) {
        mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setVideoType(int type) {
        mVideoType = type;
    }

    public int getVideoType() {
        return mVideoType;
    }

    public void setM3U8(M3U8 m3u8) {
        mM3U8 = m3u8;
    }

    public M3U8 getM3U8() {
        return mM3U8;
    }

    public void setTotalTs(int count) {
        mTotalTs = count;
    }

    public int getTotalTs() {
        return mTotalTs;
    }

    public void setCurTs(int count) {
        mCurTs = count;
    }

    public int getCurTs() {
        return mCurTs;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public String getSpeedString() {
        return VideoStorageUtils.getSizeStr((long) mSpeed) + "/s";
    }

    public void setPercent(float percent) {
        mPercent = percent;
    }

    public float getPercent() {
        return mPercent;
    }

    public String getPercentString() {
        return VideoDownloadUtils.getPercent(mPercent);
    }

    public void setDownloadSize(long size) {
        mDownloadSize = size;
    }

    public long getDownloadSize() {
        return mDownloadSize;
    }

    public String getDownloadSizeString() {
        return VideoStorageUtils.getSizeStr(mDownloadSize);
    }

    public void setTotalSize(long size) {
        mTotalSize = size;
    }

    public long getTotalSize() {
        return mTotalSize;
    }

    public void setFileHash(String md5) {
        mFileHash = md5;
    }

    public String getFileHash() {
        return mFileHash;
    }

    public void setSaveDir(String path) {
        mSaveDir = path;
    }

    public String getSaveDir() {
        return mSaveDir;
    }

    public void setIsCompleted(boolean completed) {
        mIsCompleted = completed;
    }

    public boolean isCompleted() {
        return mIsCompleted;
    }

    public void setIsInDatabase(boolean in) {
        mIsInDatabase = in;
    }

    public boolean isInDatabase() {
        return mIsInDatabase;
    }

    public void setLastUpdateTime(long time) {
        mLastUpdateTime = time;
    }

    public long getLastUpdateTime() {
        return mLastUpdateTime;
    }

    public void setFileName(String name) {
        mFileName = name;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFilePath(String path) {
        mFilePath = path;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setPaused(boolean paused) {
        mPaused = paused;
    }

    public boolean isPaused() {
        return mPaused;
    }

    public boolean isRunningTask() {
        return mTaskState == VideoTaskState.DOWNLOADING;
    }

    public boolean isPendingTask() {
        return mTaskState == VideoTaskState.PENDING || mTaskState == VideoTaskState.PREPARE;
    }

    public boolean isErrorState() {
        return mTaskState == VideoTaskState.ERROR;
    }

    public boolean isSuccessState() {
        return mTaskState == VideoTaskState.SUCCESS;
    }

    public boolean isInterruptTask() {
        return mTaskState == VideoTaskState.PAUSE || mTaskState == VideoTaskState.ERROR;
    }

    public boolean isInitialTask() {
        return mTaskState == VideoTaskState.DEFAULT;
    }

    public boolean isHlsType() {
        return mVideoType == Video.Type.HLS_TYPE;
    }

    public void reset() {
        mTaskState = VideoTaskState.DEFAULT;
        mDownloadCreateTime = 0L;
        mMimeType = null;
        mErrorCode = 0;
        mVideoType = Video.Type.DEFAULT;
        mTaskState = VideoTaskState.DEFAULT;
        mM3U8 = null;
        mSpeed = 0.0f;
        mPercent = 0.0f;
        mDownloadSize = 0;
        mTotalSize = 0;
        mFileName = "";
        mFilePath = "";
        mCoverUrl = "";
        mCoverPath = "";
        mTitle = "";
        mGroupName = "";
    }

    @Override
    public Object clone() {
        VideoTaskItem taskItem = new VideoTaskItem(mUrl);
        taskItem.setDownloadCreateTime(mDownloadCreateTime);
        taskItem.setTaskState(mTaskState);
        taskItem.setMimeType(mMimeType);
        taskItem.setErrorCode(mErrorCode);
        taskItem.setVideoType(mVideoType);
        taskItem.setPercent(mPercent);
        taskItem.setDownloadSize(mDownloadSize);
        taskItem.setSpeed(mSpeed);
        taskItem.setTotalSize(mTotalSize);
        taskItem.setFileHash(mFileHash);
        taskItem.setFilePath(mFilePath);
        taskItem.setFileName(mFileName);
        taskItem.setCoverUrl(mCoverUrl);
        taskItem.setCoverPath(mCoverPath);
        taskItem.setTitle(mTitle);
        taskItem.setGroupName(mGroupName);
        return taskItem;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof VideoTaskItem) {
            String objUrl = ((VideoTaskItem) obj).getUrl();
            if (mUrl.equals(objUrl)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "VideoTaskItem[Url=" + mUrl +
                ", Type=" + mVideoType +
                ", Percent=" + mPercent +
                ", DownloadSize=" + mDownloadSize +
                ", State=" + mTaskState +
                ", FilePath=" + mFileName +
                ", LocalFile=" + mFilePath +
                ", CoverUrl=" + mCoverUrl +
                ", CoverPath=" + mCoverPath +
                ", Title=" + mTitle +
                "]";
    }
}
