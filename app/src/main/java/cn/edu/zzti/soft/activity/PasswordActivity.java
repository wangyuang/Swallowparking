package cn.edu.zzti.soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.data.LQPreferences;

/**
 * Created by WYA on 2018/5/1.
 */

public class PasswordActivity extends Activity {
    private LQPreferences lq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        lq=LQPreferences.getLq(PasswordActivity.this);

        Intent intent = getIntent();

        lq.savePhone(intent.getStringExtra("phone"));


    }

}
