package com.perez.peliculas.download;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.perez.downloader.VideoDownloadManager;
import com.perez.downloader.listener.DownloadListener;
import com.perez.downloader.listener.IDownloadInfosCallback;
import com.perez.downloader.model.VideoTaskItem;
import com.perez.downloader.utils.LogUtils;
import com.perez.downloader.utils.VideoStorageUtils;
import com.perez.peliculas.R;
import com.perez.peliculas.merge.VideoMergeActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VideoDownloadListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DownloadFeatureActivity";
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private Button mPauseAllBtn;
    private Button mStartAllBtn;
    private ListView mDownloadListView;

    private VideoDownloadListAdapter mAdapter;
    private List<VideoTaskItem> items = new LinkedList<>();

    private void requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int index = 0; index < grantResults.length; index++) {
                if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Permission [" + permissions[index] + "] has been denied.", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }

            File file = VideoStorageUtils.getVideoCacheDir();
            if (!file.exists()) {
                if(!file.mkdir()) System.out.println("Cannot make directory");
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);
        requestPermissions();
        initViews();
        VideoDownloadManager.getInstance().setGlobalDownloadListener(mListener);
        initDatas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.amarillo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                AnadirTask();
                return true;
            case R.id.action_settings:
                Intent it = new Intent(this, DownloadSettingsActivity.class);
                startActivity(it);
                return true;
            case R.id.action_merge:
                Intent intent = new Intent(this, VideoMergeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        mPauseAllBtn = findViewById(R.id.pause_task_btn);
        mStartAllBtn = findViewById(R.id.start_task_btn);
        mDownloadListView = findViewById(R.id.download_listview);
        mStartAllBtn.setOnClickListener(this);
        mPauseAllBtn.setOnClickListener(this);
    }

    private void initDatas() {
        mAdapter = new VideoDownloadListAdapter(this, R.layout.download_item, items);
        mDownloadListView.setAdapter(mAdapter);
        VideoDownloadManager.getInstance().fetchDownloadItems(mInfosCallback);
        mDownloadListView.setOnItemClickListener((parent, view, position, id) -> {
            VideoTaskItem item = items.get(position);
            if (item.isInitialTask()) {
                VideoDownloadManager.getInstance().startDownload(item);
            } else if (item.isRunningTask()) {
                VideoDownloadManager.getInstance().pauseDownloadTask(item.getUrl());
            } else if (item.isInterruptTask()) {
                VideoDownloadManager.getInstance().resumeDownload(item.getUrl());
            }
        });
    }


    private long mLastProgressTimeStamp;
    private long mLastSpeedTimeStamp;

    private DownloadListener mListener = new DownloadListener() {
        @Override
        public void onDownloadDefault(VideoTaskItem item) {
            LogUtils.w(TAG,"onDownloadDefault: " + item);
            notifyChanged(item);
        }

        @Override
        public void onDownloadPending(VideoTaskItem item) {
            LogUtils.w(TAG,"onDownloadPending: " + item);
            notifyChanged(item);
        }

        @Override
        public void onDownloadPrepare(VideoTaskItem item) {
            LogUtils.w(TAG,"onDownloadPrepare: " + item);
            notifyChanged(item);
        }

        @Override
        public void onDownloadStart(VideoTaskItem item) {
            LogUtils.w(TAG,"onDownloadStart: " + item);
            notifyChanged(item);
        }

        @Override
        public void onDownloadProgress(VideoTaskItem item) {
            long currentTimeStamp = System.currentTimeMillis();
            if (currentTimeStamp - mLastProgressTimeStamp > 1000) {
                LogUtils.w(TAG, "onDownloadProgress: " + item.getPercentString() + ", curTs=" + item.getCurTs() + ", totalTs=" + item.getTotalTs());
                notifyChanged(item);
                mLastProgressTimeStamp = currentTimeStamp;
            }
        }

        @Override
        public void onDownloadSpeed(VideoTaskItem item) {
            long currentTimeStamp = System.currentTimeMillis();
            if (currentTimeStamp - mLastSpeedTimeStamp > 1000) {
                notifyChanged(item);
                mLastSpeedTimeStamp = currentTimeStamp;
            }
        }

        @Override
        public void onDownloadPause(VideoTaskItem item) {
            LogUtils.w(TAG,"onDownloadPause: " + item.getUrl());
            notifyChanged(item);
        }

        @Override
        public void onDownloadError(VideoTaskItem item) {
            LogUtils.w(TAG,"onDownloadError: " + item.getUrl());
            notifyChanged(item);
        }

        @Override
        public void onDownloadSuccess(VideoTaskItem item) {
            LogUtils.w(TAG,"onDownloadSuccess: " + item);
            notifyChanged(item);
        }
    };

    private void notifyChanged(final VideoTaskItem item) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyChanged(items, item);
            }
        });
    }

    private IDownloadInfosCallback mInfosCallback =
            items -> {
                for (VideoTaskItem item : items) {
                    notifyChanged(item);
                }
            };

    private void AnadirTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add download task");
        final EditText input = new EditText(this);
        input.setHint("Please paste your video URL here and click OK to start this download task!");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            VideoTaskItem vit = new VideoTaskItem(input.getText().toString());
            items.add(vit);
            initDatas();
            VideoDownloadManager.getInstance().startDownload(vit);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    @Override
    public void onClick(View v) {
        if (v == mStartAllBtn) {
            VideoDownloadManager.getInstance().resumeAllDownloads();
        } else if (v == mPauseAllBtn) {
            VideoDownloadManager.getInstance().pauseAllDownloadTasks();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoDownloadManager.getInstance().removeDownloadInfosCallback(mInfosCallback);
    }
}
