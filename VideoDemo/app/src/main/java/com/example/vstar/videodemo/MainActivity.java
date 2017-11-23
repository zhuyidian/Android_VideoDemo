package com.example.vstar.videodemo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.vstar.videodemo.Player.MediaPlayer_SurfaceView;
import com.example.vstar.videodemo.Player.VideoViewPlayer;

public class MainActivity extends Activity implements OnClickListener{
    private static final String TAG = "MainActivity";
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
		 * 使用多媒体数据库进行文件查询
		 */
        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        // MediaStore.Video.Media.DATA：视频文件路径；
        // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
        // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
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

        //querySdVedioFile();
        //querySdAudioFile();

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
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
	 * 从SD卡中获取视频文件,支持格式mkv,
	 */
    public void querySdVedioFile(){
        //第一步：得到一个ContentResolver实例
        ContentResolver contentResolver = this.getContentResolver();
        String[] projection = new String[]{MediaStore.Video.Media.TITLE};
        //如果获取路径，需要讲projection修改为String[] projection = new String[]{MediaStore.Video.Media.DATA};
        //获取的语句也需要修改为：cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
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
     * 查询SD中的音频文件
     */
    public void querySdAudioFile(){
		/*Cursor cursor = resolver.query(_uri, prjs, selections, selectArgs, order);
		 * 参数说明：
		 * Uri：这个Uri代表要查询的数据库名称加上表的名称。这个Uri一般都直接从MediaStore里取得，例如我要取所有歌的信息，就必须利用MediaStore.Audio.Media. EXTERNAL _CONTENT_URI这个Uri。
		 * 专辑信息要利用MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI这个Uri来查询，其他查询也都类似。
		 * Prjs：这个参数代表要从表中选择的列，用一个String数组来表示。
		 * Selections：相当于SQL语句中的where子句，就是代表你的查询条件。
		 * selectArgs：这个参数是说你的Selections里有？这个符号是，这里可以以实际值代替这个问号。如果Selections这个没有？的话，那么这个String数组可以为null。
		 * Order：说明查询结果按什么来排序。
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
     * 向音频数据库中增加音频
     */
    public void addSdAudioFile(){
        ContentResolver contentResolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,0);
		/*
		 * 这个insert传递的参数只有两个，一个是Uri(同查询那个Uri)，另一个是ContentValues。这个ContentValuses对应于数据库的一行数据，
		 * 只要用put方法把每个列的设置好之后，直接利用insert方法去插入就好了。
		 */
        contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
    }
    /*
     *更新音频数据库中的数据
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
     *删除音频数据库中的数据
     */
    public void deleteSdAudioFile(){
        ContentResolver resolver = this.getContentResolver();

        //resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,where, selectionArgs);
    }
}
