package com.example.vstar.videodemo.Player;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.vstar.videodemo.R;

import java.io.File;
import java.io.IOException;

public class MediaPlayer_SurfaceView extends Activity implements OnClickListener{
	private static final String TAG = "MediaPlayer_SurfaceView";
    /* 功能按钮 */
    private Button btn_play, btn_pause, btn_stop, btn_low, btn_height;
    /* SurfaceView */
    private SurfaceView mSurfaceView;
    /* 播放视频对象 */
    private MediaPlayer mediaPlayer;
    /* 系统声音 */
    private AudioManager audioManager;
    /* 记录播放位置 */
    private int position;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate");
		
		/* 设置窗口无title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /* 全屏显示 */
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.surfaceview_mediaplayerlayout);

        findViews();
        setListeners();
        mediaPlayer = new MediaPlayer();
        setSurfaceView();
	}

    /* 实例化UI */
    private void findViews() {
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_low = (Button) findViewById(R.id.btn_low);
        btn_height = (Button) findViewById(R.id.btn_hight);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceVIew);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    /* 为5个按钮设置监听 */
    private void setListeners() {
        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_low.setOnClickListener(this);
        btn_height.setOnClickListener(this);
    }

    // 横竖屏切换时的处理
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {  // 如果在播放的时候切换屏幕则保存当前观看的位置
            outState.putInt("position", mediaPlayer.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    // 横竖屏切换后的处理
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("position")) {
            position = savedInstanceState.getInt("position");// 取得切换屏幕时保存的位置
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /* 设置surfaceView视图 */
    private void setSurfaceView() {
        Log.e(TAG,"setSurfaceView");
        // creates a "push" surface
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置事件，回调函数
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            /* SurfaceView创建时 */
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (position > 0) {
                    playMedia();
                    position = 0;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
            }

            /* SurfaceView销毁视图 */
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                /* Activity销毁时停止播放，释放资源。不做这个操作，即使退出，还是能听到视频的声音 */
                mediaPlayer.release();
            }
        });
    }

    /* 播放视频 */
    private void playMedia() {
        /* 初始化状态 */
        mediaPlayer.reset();
        /* 设置声音流类型 */
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        /* 设置mp3,mp4加载路径 */
//        String dir = Environment.getExternalStorageDirectory()+"/Movies";
//        Log.e(TAG,"playMedia---dir="+dir);
//        File file = new File(dir, "刘德华 - 浪子心声.mkv");

        String dir = Environment.getExternalStorageDirectory()+"/DCIM/Eye4";
        Log.e(TAG,"playMedia---dir="+dir);
        File file = new File(dir, "Camera1_20171116224723.mp4");
        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            // 缓冲
            mediaPlayer.prepare();
            // 开始播放
            mediaPlayer.start();
            // 具体位置
            mediaPlayer.seekTo(position);
            // 视频输出到View
            mediaPlayer.setDisplay(mSurfaceView.getHolder());
            // 重置位置为0
            position = 0;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_play:// 播放
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return;
            } else {
                playMedia();
            }
            break;
        case R.id.btn_pause:// 暂停
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                position = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            } else {
                return;
            }
            break;
        case R.id.btn_stop:// 停止
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                position = 0;
            } else {
                return;
            }
            break;
        case R.id.btn_low:// 调小音量�
            // 获取当前的音乐音量��
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            // 音量>0
            if (volume > 0) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, 0);
            } else {
                return;
            }
            break;
        case R.id.btn_hight:// 调大音量������
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            // 音量<100
            if (volume < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, 0);
            } else {
                return;
            }
            break;
        }
    }
}
