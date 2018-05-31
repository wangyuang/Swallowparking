package cn.edu.zzti.soft.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.fragment.MainFragment;
import cn.edu.zzti.soft.fragment.MyInfoFragment;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button[] mTabs;

    private Fragment[] fragments;
    private MainFragment mainfragment;
    private MyInfoFragment myinfofFragment;
    //private FuJinFragment fujinfragment;
    private int index;  // 这个是选择其中将其中的 index 变换
    private int currentTabIndex; // 这个是记录当前位置下标的位置

    // 弹出提示框
    private Toast toast;
    // 记录第一次按下的时间
    private long firstPressTime = -1;
    // 记录第二次按下的时间
    private long lastPressTime;
    // 两次按下的时间间隔
    private final long INTERVAL = 2000;

    public static String  realToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);

        mainfragment = new MainFragment();
        myinfofFragment = new MyInfoFragment();
        //fujinfragment = new FuJinFragment();

        getFragmentManager().beginTransaction().add(R.id.fragment_container, mainfragment)
                .show(mainfragment).commit();

        fragments = new Fragment[]{mainfragment,myinfofFragment};

        initView();
        toast=Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT);

        getRealToken();

    }

    public  void getRealToken(){
        SharedPreferences pref=getSharedPreferences("phone",MODE_PRIVATE);
        realToken=pref.getString("realToken","");
    }


    @Override
    public void onBackPressed() {
        showQuitTips();
    }

    private void showQuitTips(){
        // 如果是第一次按下 直接提示
        if (firstPressTime == -1) {
            firstPressTime = System.currentTimeMillis();
            toast.show();
        }
        // 如果是第二次按下，需要判断与上一次按下的时间间隔，这里设置2秒
        else {
            lastPressTime = System.currentTimeMillis();
            if (lastPressTime - firstPressTime <= INTERVAL) {
                System.exit(0);
            } else {
                firstPressTime = lastPressTime;
                toast.show();
            }
        }
    }

    private void initView(){
        mTabs = new Button[3];
        mTabs[0] = (Button) findViewById(R.id.btn_zhuye);
        //mTabs[1] = (Button) findViewById(R.id.btn_fujin);
        mTabs[1] = (Button) findViewById(R.id.btn_myinfo);
        mTabs[0].setSelected(true);

        mTabs[0].setOnClickListener(this);
        mTabs[1].setOnClickListener(this);
        //mTabs[2].setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_zhuye:
                index = 0;
                break;
//		case R.id.btn_fujin:
//			index = 1;
//			break;
            case R.id.btn_myinfo:
                index =1;
                break;
            default:
                break;
        }

        if (currentTabIndex != index) {
            // 又开启一个fragment增加一个 这个里面又增加了一个
            FragmentTransaction trx = getFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            // 然后进行显示在界面上
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//		if(event.){

//		}
        return super.onTouchEvent(event);
    }


}

