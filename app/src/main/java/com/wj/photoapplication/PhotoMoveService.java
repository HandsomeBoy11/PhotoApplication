package com.wj.photoapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * @author wangjun
 * @date 2021/9/6 15:32
 * @Des :
 */
public class PhotoMoveService extends Service {
    public PhotoMoveService() {
    }

    private static StopImg stopImg;
    private MediaPlayer mediaPlayer;
    private Boolean aBoolean = false;

    @Override
    public void onCreate() {
        //创建音乐媒体播放器，音乐文件放到res/raw文件夹中。
        mediaPlayer = MediaPlayer.create(this,R.raw.active_second_step);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开始播放音乐
        mediaPlayer.start();
        //监听音乐是否播放完毕，播放完了就运用回调停止Activity中的图片轮播。
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopImg.stop();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //获取Activity，将Service和Activity关联起来，为回调做准备。
    public static void setMusicStop(Context context){
        stopImg = (StopImg) context;
    }

    //实现一个接口，用于回调
    public interface StopImg{
        public void stop();
    }
}
