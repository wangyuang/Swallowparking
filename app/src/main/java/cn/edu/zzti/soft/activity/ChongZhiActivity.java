package cn.edu.zzti.soft.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.dao.impl.PersonDao;
import cn.edu.zzti.soft.data.LQPreferences;

/**
 * Created by WYA on 2018/5/1.
 */

public class ChongZhiActivity extends Activity implements View.OnClickListener{
    private EditText edit_cz_money;

    private TextView text_money;
    private Button btn_cz;
    private Button btn_zc;

    private PersonDao person_dao;

    private float money = 0.0f;
    private LQPreferences lq = new LQPreferences();

    private ImageView title_img;
    private TextView title_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congzhi);

        init();
        listener();


    }
    private void init(){
        person_dao = new PersonDao(this);
        lq.init(this);
        money =person_dao.getMoney(lq.getPhone());

        title_img = (ImageView)findViewById(R.id.title_img);
        title_img.setImageResource(R.drawable.fx_icon_back_n);
        title_img.setOnClickListener(this);

        title_name = (TextView)findViewById(R.id.title_name);
        title_name.setText("钱包");
        edit_cz_money =(EditText) findViewById(R.id.edit_cz_money);
        text_money = (TextView) findViewById(R.id.text_money);
        btn_zc = (Button)findViewById(R.id.btn_zc);
        text_money.setText(""+person_dao.getMoney(lq.getPhone()));
        btn_cz = (Button)findViewById(R.id.btn_cz);
    }
    private void listener(){
        btn_cz.setOnClickListener(this);
        btn_zc.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cz:
                if(!edit_cz_money.getText().toString().equals("")
                        &&edit_cz_money.getText().toString()!=null){
                    float newmoney =Float.parseFloat(edit_cz_money.getText().toString());

                    newmoney = money+newmoney;
                    money = newmoney;
                    if(person_dao.updateMoney(lq.getPhone(), newmoney)){
                        Toast.makeText(ChongZhiActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                    }
                    lq.saveMoney(newmoney);
                    text_money.setText(newmoney+"");
                    edit_cz_money.setText("");
                }else{
                    Toast.makeText(ChongZhiActivity.this, "输入充值金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_zc:
                if(!edit_cz_money.getText().toString().equals("")
                        &&edit_cz_money.getText().toString()!=null){
                    float newmoney =Float.parseFloat(edit_cz_money.getText().toString());
                    newmoney = money-newmoney;
                    money = newmoney;
                    if(person_dao.updateMoney(lq.getPhone(), newmoney)){
                        Toast.makeText(ChongZhiActivity.this, "转出成功", Toast.LENGTH_SHORT).show();
                    }
                    lq.saveMoney(newmoney);
                    text_money.setText(newmoney+"");
                    edit_cz_money.setText("");
                }else{
                    Toast.makeText(ChongZhiActivity.this, "输入转出金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.title_img:
                finish();
                break;
            default:
                break;
        }

    }

}
