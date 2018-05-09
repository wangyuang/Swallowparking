package cn.edu.zzti.soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import cn.edu.zzti.soft.R;

/**
 * Created by WYA on 2018/5/1.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        //这个里面是色图ContentView(View view) //这个里面是View 每一个xml是继承与 View组件的
        setContentView(R.layout.activity_splash); //给其添加一个界面
        //Hander 主要是主线程和副线程之间的通信数据的传输
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //从一个线程到另一个线程之间的方法 。activity之间的通信
                Intent intent = new Intent(SplashActivity.this,NewuserActivity.class);
                //startActivity是启动另一个的Activity
                startActivity(intent);//开始跳转界面
                SplashActivity.this.finish();//将开始界面
            }
        };
        Message mes = new Message();
        handler.sendMessageDelayed(mes, 4000);
    }
}

