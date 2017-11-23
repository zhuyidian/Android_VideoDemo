package com.example.vstar.videodemo.Player;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vstar.videodemo.R;

import java.io.File;

public class VideoViewPlayer extends Activity implements OnClickListener{
	 private static final String TAG = "VideoViewPlayer";
	 private VideoView video;
	 private Button play;
	 private Button pause;
	 private Button replay;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoviewlayout);
        video = (VideoView) findViewById(R.id.videoview);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        replay = (Button) findViewById(R.id.replay);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);
        //logcat("onCreate");
        Log.e(TAG,"onCreate");
        initVediopath();
	}
	private void initVediopath() {
        //logcat("initVediopath");
        Log.e(TAG,"initVediopath");
        String dir = Environment.getExternalStorageDirectory()+"/Movies";
        //File file = new File(Environment.getExternalStorageDirectory(), "1.mp4");
        File file = new File(dir, "���»� - ��������.mkv");
        Log.e(TAG,"initVediopath---dir="+dir);
        //logcat("" + Environment.getExternalStorageDirectory());
        if (file.exists()) {
            //logcat("setPath");
            Log.e(TAG,"initVediopath---setVideoPath");
            video.setVideoPath(file.getPath());
        } else {
            Toast.makeText(this, "�ļ�������", Toast.LENGTH_SHORT).show();
            //logcat("file not exsit");
        }
    }
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != video) {
            video.suspend();
            Log.e(TAG,"onDestroy");
        }
    }

	@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.play:
        	
            if (!video.isPlaying()) {
            	Log.e(TAG,"onClick---play");
                video.start();
            }
            break;
        case R.id.pause:
            if (video.isPlaying()) {
            	Log.e(TAG,"onClick---pause");
                video.pause();
            }
            break;
        case R.id.replay:
            if (video.isPlaying()) {
            	Log.e(TAG,"onClick---replay");
                video.resume();
            }
            break;

        default:
            break;
        }
    }
}
