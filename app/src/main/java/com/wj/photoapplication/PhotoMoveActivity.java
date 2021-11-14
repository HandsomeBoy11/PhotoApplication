package com.wj.photoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;

/**
 * @author wangjun
 * @date 2021/9/6 15:37
 * @Des :
 */
public class PhotoMoveActivity extends AppCompatActivity implements PhotoMoveService.StopImg{

    //创建一个ImageView用于关联布局文件中的控件
    private ImageView show;
    //该布尔值变量用于判断点击事件，因为这里是点击屏幕就开始相片轮播，防止用户点击多次发生紊乱，这里限制一下
    private Boolean click = false;
    //用于判断图片是否轮播
    private Boolean run = true;
    //数组，存放的是图片
    private int[] imgs = {R.drawable.a1,R.drawable.a1,R.drawable.a1,R.drawable.a1,
            R.drawable.a1,R.drawable.a1,R.drawable.a1,R.drawable.a1,
            R.drawable.a1,R.drawable.a1,R.drawable.a1,R.drawable.a1,R.drawable.a1};
    //数组，存放的是各种动画效果
    private Effectstype[] types={Effectstype.Fadein,Effectstype.Fall,Effectstype.Fliph,Effectstype.Flipv,
            Effectstype.Newspager,Effectstype.RotateBottom,Effectstype.RotateLeft,Effectstype.Shake,
            Effectstype.Sidefill,Effectstype.SlideBottom,Effectstype.Slideright,Effectstype.Slidetop,Effectstype.Slideleft,
            Effectstype.Slit};
    //用于获取子线程传过来的值，并在UI线程做出响应
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            start(msg.arg1,4000,msg.what);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_move);
        show = (ImageView) findViewById(R.id.show);
        //监听屏幕中的ImageView控件被点击，开启服务，奏起音乐，并且调用子线程，开始轮播相册。
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click){
                    return;
                }
                click = true;
                Intent intent = new Intent(PhotoMoveActivity.this,PhotoMoveService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
                start(6,4000,6);
                PhotoMoveService.setMusicStop(PhotoMoveActivity.this);
                new Thread(new ImgNext()).start();
            }
        });
    }

    //实现Service中接口的方法，用来停止轮播。
    @Override
    public void stop() {
        run = false;
    }

    //子线程，用于循环轮播相册，每次循环，将参数传给显示图片的方法。
    public class ImgNext implements Runnable{

        @Override
        public void run() {
            int i = -1;
            int which = -1;
            Message message;
            while (i<imgs.length&&run){
                if (which == types.length-1){
                    which = -1;
                }
                if (i == imgs.length-1){
                    i = -1;
                }
                try {
                    Thread.sleep(5000);
                    message = new Message();
                    i++;
                    which++;
                    message.what = i;
                    message.arg1 = which;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!run){
                message = new Message();
                message.what = 10;
                message.arg1 = 0;
                handler.sendMessage(message);
            }
        }
    }

    //将图片放到ImageView，再给ImageView附加动画的方法
    private void start(int which,int mDuration,int img) {
        show.setImageResource(imgs[img]);
        BaseEffects animator = types[which].getAnimator();
        if (mDuration != -1) {
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(show);
    }

}
