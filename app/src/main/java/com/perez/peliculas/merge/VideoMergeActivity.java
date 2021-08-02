package com.perez.peliculas.merge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.perez.downloader.utils.LogUtils;
import com.jeffmony.m3u8library.VideoProcessManager;
import com.jeffmony.m3u8library.listener.IVideoTransformListener;
import com.perez.peliculas.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import abhishekti7.unicorn.filepicker.UnicornFilePicker;
import abhishekti7.unicorn.filepicker.utils.Constants;

public class VideoMergeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "VideoMergeActivity";

    private EditText mSrcTxt;
    private EditText mDestTxt;
    private Button mConvertBtn, mSelBtn;
    private TextView mTransformProgressTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_merge);
        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQ_UNICORN_FILE && resultCode == RESULT_OK) {
            ArrayList<String> files = data.getStringArrayListExtra("filePaths");
            String src = files.get(0), dst = src.substring(0, src.lastIndexOf(".")+1) + "mp4";
            mSrcTxt.setText(src);
            mDestTxt.setText(dst);
        }
    }

    private void initViews() {
        mSrcTxt = findViewById(R.id.src_path_txt);
        mDestTxt = findViewById(R.id.dest_path_txt);
        mConvertBtn = findViewById(R.id.convert_btn);
        mTransformProgressTxt = findViewById(R.id.video_transform_progress_txt);
        mSelBtn = findViewById(R.id.select_btn);
        mConvertBtn.setOnClickListener(this);
        mSelBtn.setOnClickListener(this);
    }

    public static void doConvertVideo(String inputPath, String outputPath, TextView med) {
        if (TextUtils.isEmpty(inputPath) || TextUtils.isEmpty(outputPath)) {
            LogUtils.i(TAG, "InputPath or OutputPath is null");
            return;
        }
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            return;
        }
        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (Exception e) {
                LogUtils.w(TAG, "Create file failed, exception = " + e);
                return;
            }
        }
        LogUtils.i(TAG, "inputPath="+inputPath+", outputPath="+outputPath);
        VideoProcessManager.getInstance().transformM3U8ToMp4(inputPath, outputPath, new IVideoTransformListener() {
            @Override
            public void onTransformProgress(float progress) {
                LogUtils.i(TAG, "onTransformProgress progress = "+progress);
                DecimalFormat format = new DecimalFormat(".00");
                med.setText("Progreso Convertido: " + format.format(progress) + "%");
            }

            @Override
            public void onTransformFinished() {
                LogUtils.i(TAG, "onTransformFinished, and will delete the old '.video' suffix file");
                File f = new File(inputPath);
                if(f.exists()) f.delete();
            }

            @Override
            public void onTransformFailed(Exception e) {
                LogUtils.i(TAG, "onTransformFailed, e="+e.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mConvertBtn.equals(v)) {
            doConvertVideo(mSrcTxt.getText().toString(), mDestTxt.getText().toString(), mTransformProgressTxt);
        }
        if(mSelBtn.equals(v)) {
            UnicornFilePicker.from(VideoMergeActivity.this)
                    .addConfigBuilder()
                    .selectMultipleFiles(false)
                    .showOnlyDirectory(false)
                    .setRootDirectory(Environment.getExternalStorageDirectory().getAbsolutePath())
                    .showHiddenFiles(false)
                    .setFilters(new String[]{"video"})
                    .addItemDivider(true)
                    .theme(R.style.UnicornFilePicker_Dracula)
                    .build()
                    .forResult(Constants.REQ_UNICORN_FILE);
        }
    }
}
