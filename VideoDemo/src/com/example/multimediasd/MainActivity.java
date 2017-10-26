package com.example.multimediasd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.media.*;

public class MainActivity extends Activity implements OnClickListener{
	private final static String TAG="MainActivity";
	private Button mVideoviewPlayer;
	private Button mSurfaceviewMediapalyer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mVideoviewPlayer = (Button) findViewById(R.id.btn_videoviewpalyer);
		mVideoviewPlayer.setOnClickListener(this);
		mSurfaceviewMediapalyer = (Button) findViewById(R.id.btn_surfaceviewMediapalyer);
		mSurfaceviewMediapalyer.setOnClickListener(this);
		
		/*
		 * ʹ�ö�ý�����ݿ�����ļ���ѯ
		 */
		// MediaStore.Video.Thumbnails.DATA:��Ƶ����ͼ���ļ�·��
        // MediaStore.Video.Media.DATA����Ƶ�ļ�·����
        // MediaStore.Video.Media.DISPLAY_NAME : ��Ƶ�ļ������� testVideo.mp4
        // MediaStore.Video.Media.TITLE: ��Ƶ���� : testVideo
        // MediaStore.Video.Media.DATE_ADDED
        // MediaStore.Video.Media.DATE_MODIFIED
        // MediaStore.Video.Media.HEIGHT
        // MediaStore.Video.Media.MIME_TYPE
        // MediaStore.Video.Media.SIZE
        // MediaStore.Video.VideoColumns.ALBUM
        // MediaStore.Video.VideoColumns.ARTIST
        // MediaStore.Video.VideoColumns.BOOKMARK
        // MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME
        // MediaStore.Video.VideoColumns.BUCKET_ID
        // MediaStore.Video.VideoColumns.CATEGORY
        // MediaStore.Video.VideoColumns.DATE_TAKEN
        // MediaStore.Video.VideoColumns.DESCRIPTION
        // MediaStore.Video.VideoColumns.DURATION
        // MediaStore.Video.VideoColumns.LANGUAGE
        // String[] projection = { MediaStore.Video.VideoColumns.DATA,
        // MediaStore.Video.Media.DISPLAY_NAME,
        // MediaStore.Video.Media.TITLE,
        // MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.SIZE,
        // MediaStore.Video.VideoColumns.ALBUM,
        // MediaStore.Video.VideoColumns.ARTIST,
        // MediaStore.Video.VideoColumns.BOOKMARK,
        // MediaStore.Video.VideoColumns.DURATION, };
		
		querySdVedioFile();
		querySdAudioFile();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
        case R.id.btn_videoviewpalyer:
        	Log.e(TAG,"onClick---btn_videoviewpalyer");
        	Intent intent1 = new Intent(MainActivity.this,VideoViewPlayer.class);
			startActivity(intent1);
            break;
        case R.id.btn_surfaceviewMediapalyer:
        	Log.e(TAG,"onClick---btn_surfaceviewMediapalyer");
        	Intent intent2 = new Intent(MainActivity.this,MediaPlayer_SurfaceView.class);
			startActivity(intent2);
            break;
        default:
            break;
        }
	}
	
	/*
	 * ��SD���л�ȡ��Ƶ�ļ�,֧�ָ�ʽmkv,
	 */
	public void querySdVedioFile(){
		//��һ�����õ�һ��ContentResolverʵ��
		ContentResolver contentResolver = this.getContentResolver();
		String[] projection = new String[]{MediaStore.Video.Media.TITLE};
		//�����ȡ·������Ҫ��projection�޸�ΪString[] projection = new String[]{MediaStore.Video.Media.DATA};
        //��ȡ�����Ҳ��Ҫ�޸�Ϊ��cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
		for (String string : projection) {
			Log.e(TAG,"getSdVedioFile---string="+string);
		}
		
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int fileNum = cursor.getCount();
        Log.e(TAG,"getSdVedioFile---fileNum="+fileNum);
        
        for(int counter = 0; counter < fileNum; counter++){        
            //Log.w(TAG, "----------------------file is: " + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)) );
            Log.e(TAG,"getSdVedioFile---file is:"+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)) );
            cursor.moveToNext();
        }
        cursor.close();
	}
	/*
	 * ��ѯSD�е���Ƶ�ļ�
	 */
	public void querySdAudioFile(){
		/*Cursor cursor = resolver.query(_uri, prjs, selections, selectArgs, order);
		 * ����˵����
		 * Uri�����Uri����Ҫ��ѯ�����ݿ����Ƽ��ϱ�����ơ����Uriһ�㶼ֱ�Ӵ�MediaStore��ȡ�ã�������Ҫȡ���и����Ϣ���ͱ�������MediaStore.Audio.Media. EXTERNAL _CONTENT_URI���Uri��
		 * ר����ϢҪ����MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI���Uri����ѯ��������ѯҲ�����ơ�
		 * Prjs�������������Ҫ�ӱ���ѡ����У���һ��String��������ʾ��
		 * Selections���൱��SQL����е�where�Ӿ䣬���Ǵ�����Ĳ�ѯ������
		 * selectArgs�����������˵���Selections���У���������ǣ����������ʵ��ֵ��������ʺš����Selections���û�У��Ļ�����ô���String�������Ϊnull��
		 * Order��˵����ѯ�����ʲô������
		 */
		Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int counter = cursor.getCount();
        Log.e(TAG,"getSdAudioFile---counter="+counter);
        //String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        //Log.e(TAG,"getSdAudioFile---title="+title);
        
        //Log.w(TAG, "------------before looping, title = " + title);
        for(int j = 0 ; j < counter; j++){
            //Log.w(TAG, "-----------title = " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            Log.e(TAG,"getSdAudioFile---file is:"+ cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            cursor.moveToNext();
        }
        cursor.close();
	}
	/*
	 * ����Ƶ���ݿ���������Ƶ
	 */
	public void addSdAudioFile(){
		ContentResolver contentResolver = this.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,0);
		/*
		 * ���insert���ݵĲ���ֻ��������һ����Uri(ͬ��ѯ�Ǹ�Uri)����һ����ContentValues�����ContentValuses��Ӧ�����ݿ��һ�����ݣ�
		 * ֻҪ��put������ÿ���е����ú�֮��ֱ������insert����ȥ����ͺ��ˡ�
		 */
		contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
	}
	/*
	 *������Ƶ���ݿ��е�����
	 */
	public void updateSdAudioFile(){
		ContentResolver resolver = this.getContentResolver();

		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		ContentValues values = new ContentValues();
		//public void put(String key, Integer value) {
		//values.put(MediaStore.Audio.Media.DATE_MODIFIED, sid);
		//public final int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String where, @Nullable String[] selectionArgs) {
		//resolver.update(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,values, where, selectionArgs);
	}
	/*
	 *ɾ����Ƶ���ݿ��е�����
	 */
	public void deleteSdAudioFile(){
		ContentResolver resolver = this.getContentResolver();

		//resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,where, selectionArgs);
	}

	
	
}
