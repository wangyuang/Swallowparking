package cn.edu.zzti.soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.dao.PersonDaoImpl;
import cn.edu.zzti.soft.dao.impl.PersonDao;
import cn.edu.zzti.soft.data.LQPreferences;
import cn.edu.zzti.soft.entity.Person;

/**
 * Created by WYA on 2018/5/1.
 */

public class MyInfoActivity extends Activity implements View.OnClickListener {
    private ImageView image_title;
    private TextView text_name;

    private TextView t_name;
    private TextView t_phone;
    private TextView t_sex;


    private RelativeLayout re_name;
    private RelativeLayout re_phone;
    private RelativeLayout re_sex;
    //private RelativeLayout re_loaction;

    PersonDaoImpl person;
    LQPreferences lq =null;
    Person p =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        init();
        listener();
    }

    @Override
    protected void onStart() {
        lq= LQPreferences.getLq(MyInfoActivity.this);
        person = new PersonDao(MyInfoActivity.this);
        System.out.println(lq.getPhone());
        p =person.getPersons(lq.getPhone());
        if(p!=null){
            if(p.getName()!=null){
                t_name.setText(p.getName());

            }else{
                t_name.setText("未设置");
            }
            t_phone.setText(p.getPassword());
        }

        super.onStart();
    }

    private void init(){

        image_title = (ImageView)findViewById(R.id.title_img);
        text_name = (TextView)findViewById(R.id.title_name);
        image_title.setImageResource(R.drawable.fx_icon_back_n);

        t_name = (TextView) findViewById(R.id.tv_name);
        t_phone = (TextView)findViewById(R.id.tv_phone);
        t_sex = (TextView)findViewById(R.id.tv_sex);


        text_name.setText("详细信息");

        re_name =(RelativeLayout) findViewById(R.id.re_name);
        re_phone = (RelativeLayout) findViewById(R.id.re_phone);
        re_sex = (RelativeLayout) findViewById(R.id.re_sex);
        //re_loaction = (RelativeLayout) findViewById(R.id.re_loaction);


    }

    private void  listener(){
        image_title.setOnClickListener(this);
        //re_loaction.setOnClickListener(this);
        re_name.setOnClickListener(this);
        re_sex.setOnClickListener(this);
        re_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.title_img:
                finish();
                break;
            case R.id.re_name:
                Intent intent = new Intent(MyInfoActivity.this,UpdateActivity.class);
                intent.putExtra("data", 1);

                startActivity(intent);

                break;
            case R.id.re_phone:
                Intent intent2 = new Intent(MyInfoActivity.this,UpdateActivity.class);
                intent2.putExtra("data", 2);

                startActivity(intent2);
                break;
            case R.id.re_sex:
                Intent intent3 = new Intent(MyInfoActivity.this,UpdateActivity.class);
                intent3.putExtra("data", 3);

                startActivity(intent3);
                break;

            default:
                break;
        }
    }


}
