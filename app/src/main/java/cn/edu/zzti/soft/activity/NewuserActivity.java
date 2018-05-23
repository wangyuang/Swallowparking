package cn.edu.zzti.soft.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.utils.SharePrefrenceHelper;

import java.util.ArrayList;

import cn.edu.zzti.soft.R;

/**
 * Created by WYA on 2018/5/1.
 */

public class NewuserActivity extends Activity {

    private ViewPager view;

    private int[] imgIds = {R.drawable.hint_card_1,R.drawable.hint_card_2,
            R.drawable.hint_card_3,R.drawable.hint_card_4};

    private ArrayList<ImageView> imageslist;

    private LinearLayout ll_points;
    private View red_point;

    private TextView tg;
    int widthPoint =0;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);
        initView();
        view.setAdapter(new MyPagerAdapter());
        view.setOnPageChangeListener(new MyPageChangeListener());
        initListener();
    }

    private void initListener(){
        tg.setOnClickListener(new View.OnClickListener() {
            SharedPreferences pref=getSharedPreferences("phone",MODE_PRIVATE);
            String token=pref.getString("tokenResponseJson","");
            @Override
            public void onClick(View v) {
                if (!token.equals("")) {
                    Intent intent = new Intent(NewuserActivity.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(NewuserActivity.this, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
    private void initView(){
        tg = (TextView)findViewById(R.id.btn_tiaoguo);
        view = (ViewPager) findViewById(R.id.view_pager);
        ll_points = (LinearLayout) findViewById(R.id.ll_points);
        red_point = findViewById(R.id.red_point);
        imageslist = new ArrayList<ImageView>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView image = new ImageView(NewuserActivity.this);

            image.setImageResource(imgIds[i]);

            imageslist.add(image);
        }
        for (int i = 0; i < imgIds.length; i++) {
            View point = new View(this);

            point.setBackgroundResource(R.drawable.point_grade);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30,30);

            if(i>0){
                params.leftMargin = 25;
            }

            point.setLayoutParams(params);

            ll_points.addView(point);
        }

        ll_points.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {

            @Override
            public void onDraw() {
                System.out.println("onDraw 执行完了");

            }
        });
        ll_points.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {

                        System.out.println("layout 执行完了");
                        ll_points.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        // 计算出两个点之间的距离
                        // 之所以写在这里面。是因为这个方法在onCreate中。
                        // 配置一个Layout在一个Activity中 控件 measure 是用来测量这个控件大小
                        // layout 方法是控件放置在那个位置 OnDraw是来描绘其中控件内容
                        // 这个监听是监听layout 是否完成
                        widthPoint= ll_points.getChildAt(1).getLeft()
                                - ll_points.getChildAt(0).getLeft();
                        System.out.println(widthPoint);
                    }
                });


    }


    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return imgIds.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0==arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(imageslist.get(position));
            return imageslist.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View)object);
        }
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {


        }
        // 滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // System.out.println("当前位置:" + position(0,1,2) + ";百分比:" + positionOffset
            // + ";移动距离:" + positionOffsetPixels);
            int po = (int) (widthPoint*positionOffset)+widthPoint*position;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    red_point.getLayoutParams();// 获取当前红点的布局参数

            params.leftMargin = po;

            red_point.setLayoutParams(params);

        }

        @Override
        public void onPageSelected(int arg0) {


        }

    }
}

