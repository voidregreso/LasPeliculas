package com.perez.downloader.utils;

import com.perez.downloader.VideoDownloadException;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class DownloadExceptionUtils {

    private static final int UNKNOWN_ERROR = -1;
    private static final int SOCKET_TIMEOUT_ERROR = 5000;
    private static final int FILE_NOT_FOUND_ERROR = 5001;
    private static final int UNKNOWN_HOST_ERROR = 5002; 

    //Custom Exception
    private static final int FILE_LENGTH_FETCHED_ERROR = 5100;
    private static final int M3U8_FILE_CONTENT_ERROR = 5101; 
    private static final int MIMETYPE_NULL_ERROR = 5102;
    private static final int MIMETYPE_NOT_FOUND = 5103; 
    public static final int LIVE_M3U8_ERROR = 5104;

    public static final String FILE_LENGTH_FETCHED_ERROR_STRING = "File Length Cannot be fetched";
    public static final String M3U8_FILE_CONTENT_ERROR_STRING = "M3U8 File content error";
    public static final String MIMETYPE_NULL_ERROR_STRING = "MimeType is null";
    public static final String MIMETYPE_NOT_FOUND_STRING = "MimeType not found";
    public static final String VIDEO_INFO_EMPTY = "Video info is null";
    public static final String URL_SCHEMA_ERROR = "Cannot parse the request resource's schema";
    public static final String CREATE_CONNECTION_ERROR = "Create connection failed";
    public static final String FINAL_URL_EMPTY = "FinalUrl is null";
    public static final String REMOTE_M3U8_EMPTY = "Cannot find remote.m3u8 file";
    public static final String PROTOCOL_UNEXPECTED_END_OF_STREAM = "unexpected end of stream";
    public static final String RETRY_COUNT_EXCEED_WITH_THREAD_CONTROL_STRING = "Retry count exceeding with thread control";
    public static final String VIDEO_REQUEST_FAILED = "Video request failed";

    public static int getErrorCode(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            return SOCKET_TIMEOUT_ERROR;
        } else if (e instanceof FileNotFoundException) {
            return FILE_NOT_FOUND_ERROR;
        } else if (e instanceof VideoDownloadException) {
            if (((VideoDownloadException) e).getMsg().equals(FILE_LENGTH_FETCHED_ERROR_STRING)) {
                return FILE_LENGTH_FETCHED_ERROR;
            } else if (((VideoDownloadException) e).getMsg().equals(M3U8_FILE_CONTENT_ERROR_STRING)) {
                return M3U8_FILE_CONTENT_ERROR;
            } else if (((VideoDownloadException) e).getMsg().equals(MIMETYPE_NULL_ERROR_STRING)) {
                return MIMETYPE_NULL_ERROR;
            } else if (((VideoDownloadException) e).getMsg().equals(MIMETYPE_NOT_FOUND_STRING)) {
                return MIMETYPE_NOT_FOUND;
            }
        } else if (e instanceof UnknownHostException) {
            return UNKNOWN_HOST_ERROR;
        }
        return UNKNOWN_ERROR;
    }
}
