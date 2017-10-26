package com.example.multimediasd;

import java.io.File;
import java.io.IOException;

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
import android.widget.Toast;
import android.widget.VideoView;

public class MediaPlayer_SurfaceView extends Activity implements OnClickListener{
	private static final String TAG = "MediaPlayer_SurfaceView";
	 /* ���ܰ�ť */
    private Button btn_play, btn_pause, btn_stop, btn_low, btn_height;
    /* SurfaceView */
    private SurfaceView mSurfaceView;
    /* ������Ƶ���� */
    private MediaPlayer mediaPlayer;
    /* ϵͳ���� */
    private AudioManager audioManager;
    /* ��¼����λ�� */
    private int position;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		/* ���ô�����title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /* ȫ����ʾ */
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.surfaceview_mediaplayerlayout);

        findViews();
        setListeners();
        mediaPlayer = new MediaPlayer();
        setSurfaceView();
	}
	// �������л�ʱ�Ĵ���
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {// ����ڲ��ŵ�ʱ���л���Ļ�򱣴浱ǰ�ۿ���λ��
            outState.putInt("position", mediaPlayer.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    // �������л���Ĵ���
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("position")) {
            position = savedInstanceState.getInt("position");// ȡ���л���Ļʱ�����λ��
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
	/* ʵ����UI */
    private void findViews() {
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_low = (Button) findViewById(R.id.btn_low);
        btn_height = (Button) findViewById(R.id.btn_hight);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceVIew);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }
    /* Ϊ5����ť���ü��� */
    private void setListeners() {
        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_low.setOnClickListener(this);
        btn_height.setOnClickListener(this);
    }
    /* ����surfaceView��ͼ */
    private void setSurfaceView() {
        // creates a "push" surface
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // �����¼����ص�����
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            /* SurfaceView����ʱ */
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

            /* SurfaceView������ͼ */
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                /* Activity����ʱֹͣ���ţ��ͷ���Դ�����������������ʹ�˳���������������Ƶ������ */
                mediaPlayer.release();
            }
        });
    }
    /* ������Ƶ */
    private void playMedia() {
        /* ��ʼ��״̬ */
        mediaPlayer.reset();
        /* �������������� */
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        /* ����mp3\mp4����·�� */
        String dir = Environment.getExternalStorageDirectory()+"/Movies";
        //File file = new File(Environment.getExternalStorageDirectory(), "1.mp4");
        File file = new File(dir, "���»� - ��������.mkv");
        //File file = new File(Environment.getExternalStorageDirectory(), "1.mp4");
        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            // ����
            mediaPlayer.prepare();
            // ��ʼ����
            mediaPlayer.start();
            // ����λ��
            //Log.i("position", position + "------");
            mediaPlayer.seekTo(position);
            // ��Ƶ�����View
            mediaPlayer.setDisplay(mSurfaceView.getHolder());
            // ����λ��Ϊ0
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
        case R.id.btn_play:// ����
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return;
            } else {
                playMedia();
            }
            break;
        case R.id.btn_pause:// ��ͣ
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                position = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            } else {
                return;
            }
            break;
        case R.id.btn_stop:// ֹͣ
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                position = 0;
            } else {
                return;
            }
            break;
        case R.id.btn_low:// ��С����
            // ��ȡ��ǰ����������
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            //Log.v("Volume", "volume" + volume);
            // ����>0
            if (volume > 0) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, 0);
            } else {
                return;
            }
            break;
        case R.id.btn_hight:// ��������
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            // ����<100
            if (volume < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, 0);
            } else {
                return;
            }
            break;
        }
    }
}
