package cn.edu.zzti.soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.data.LQPreferences;
import cn.edu.zzti.soft.fragment.MyInfoFragment;

import static android.R.id.edit;

/**
 * Created by WYA on 2018/5/1.
 */

public class SetActivity extends Activity implements View.OnClickListener{
    private ImageView image_title;
    private TextView text_name;

    private Button btn_tui;
    private Button btn_updatepwd;

    //private LQPreferences lq = new LQPreferences();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        init();
        listener();
    }

    private void init(){

        //lq.init(this);
        image_title = (ImageView)findViewById(R.id.title_img);
        text_name = (TextView)findViewById(R.id.title_name);
        btn_tui = (Button)findViewById(R.id.btn_tuichu);
        btn_updatepwd=(Button)findViewById(R.id.btn_updatepwd);
        image_title.setImageResource(R.drawable.fx_icon_back_n);
        text_name.setText("设置");

    }
    private void  listener(){
        image_title.setOnClickListener(this);
        btn_tui.setOnClickListener(this);
        btn_updatepwd.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SetActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_tuichu:
               // lq.savePass("");
                SharedPreferences.Editor editor=getSharedPreferences("phone",MODE_PRIVATE).edit();
                editor.remove("realToken");
                editor.apply();
                SharedPreferences pref=getSharedPreferences("phone",MODE_PRIVATE);
                String realToken=pref.getString("realToken","");
                Intent intent = new Intent(SetActivity.this,LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_updatepwd:
                Intent intent1 = new Intent(SetActivity.this,EditPasswordActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
            case R.id.title_img:
                finish();
                break;

            default:
                break;
        }
    }


}
