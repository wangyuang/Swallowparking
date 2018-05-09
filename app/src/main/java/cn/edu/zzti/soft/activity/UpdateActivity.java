package cn.edu.zzti.soft.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.dao.impl.PersonDao;
import cn.edu.zzti.soft.data.LQPreferences;
import cn.edu.zzti.soft.entity.Person;

/**
 * Created by WYA on 2018/5/1.
 */

public class UpdateActivity extends Activity implements View.OnClickListener {
    private ImageView image_title;
    private TextView text_name;

    //private TextView text_info;
    private EditText edit_info;
    private Button btn_info;

    private  int updateinfo;

    private PersonDao persondao;
    private Person person;

    private LQPreferences lq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        lq = LQPreferences.getLq(UpdateActivity.this);

        persondao = new PersonDao(UpdateActivity.this);
        person = persondao.getPersons(lq.getPhone());

        init();
        listener();

    }
    private void init(){
        image_title = (ImageView)findViewById(R.id.title_img);
        text_name = (TextView)findViewById(R.id.title_name);
        image_title.setImageResource(R.drawable.fx_icon_back_n);
        text_name.setText("修改信息");

        //text_info = (TextView) findViewById(R.id.text_info);
        edit_info = (EditText) findViewById(R.id.edit_info);
        btn_info = (Button) findViewById(R.id.btn_info);

        Intent intent = getIntent();

        updateinfo =intent.getIntExtra("data", 0);

        switch (updateinfo) {
            case 1:
                if(person!=null){
                    edit_info.setText(person.getName());
                }
                break;
            case 2:
                if(person!=null){
                    edit_info.setText(person.getPhone());
                }
                break;
            case 3:
                edit_info.setHint("输入修改你的性别");
                break;

            default:
                break;
        }

    }
    private void  listener(){
        image_title.setOnClickListener(this);
        btn_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.title_img:
                finish();
                break;
            case R.id.btn_info:

                switch (updateinfo) {
                    case 1:
                        String name =edit_info.getText().toString();
                        if(name!=null){
                            persondao.updateName(lq.getPhone(), name);
                            lq.saveName(name);
                        }
                        break;
                    case 2:
                        String phone =edit_info.getText().toString();
                        if(phone!=null){
                            persondao.updatePhone(lq.getPhone(), phone);
                            lq.savePhone(phone);
                        }
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }

                break;

            default:
                break;
        }
    }
}

