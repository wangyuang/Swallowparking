package cn.edu.zzti.soft.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.entity.Person;
import cn.edu.zzti.soft.entity.ResultJSONBean;
import cn.edu.zzti.soft.entity.registeRequest;
import cn.edu.zzti.soft.view.MyEditView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.edu.zzti.soft.R.id.btn_login;
import static cn.edu.zzti.soft.R.id.btn_yzm;
import static cn.edu.zzti.soft.R.id.edit_phone;
import static cn.edu.zzti.soft.R.id.edit_pwd;
import static cn.edu.zzti.soft.R.id.edit_yzm;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private MyEditView edit_tel;
    private MyEditView edit_pwd;
    private MyEditView edit_ensurepwd;
    private Button btn_registeruser;
    String phone;
    String pwd;
    String ensurepwd;

    public void init() {
        edit_tel = (MyEditView) this.findViewById(R.id.edit_tel);
        edit_pwd = (MyEditView) this.findViewById(R.id.edit_pwd);
        edit_ensurepwd = (MyEditView) this.findViewById(R.id.edit_ensurepwd);
        btn_registeruser = (Button) this.findViewById(R.id.btn_registeruser);
        btn_registeruser.setOnClickListener(this);
        edit_tel.addTextChangedListener(new MyTextWatcher(edit_tel));
        edit_pwd.addTextChangedListener(new MyTextWatcher(edit_pwd));
        edit_ensurepwd.addTextChangedListener(new MyTextWatcher(edit_ensurepwd));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(RegisterActivity.this,"该手机已被注册",Toast.LENGTH_SHORT).show();
                    break;
              /*  case 1:
                    SharedPreferences pref=getSharedPreferences("phone",MODE_PRIVATE);
                    phone=pref.getString("phone","");
                    edit_tel.setText(phone);
                    break;*/
                case 1:
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                default:
                    break;

            }
        }
    };

    public void SavePhone(){
        SharedPreferences.Editor editor=getSharedPreferences("phone",MODE_PRIVATE).edit();
        editor.putString("phone",edit_tel.getText().toString());
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        phone=edit_tel.getText().toString();
        pwd=edit_pwd.getText().toString();
        ensurepwd=edit_ensurepwd.getText().toString();
        if(phone.equals("")){
            Toast.makeText(RegisterActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
        }else{
            if (pwd.equals("")){
                Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            }else{
                if (ensurepwd.equals("")){
                    Toast.makeText(RegisterActivity.this,"请再次输入密码",Toast.LENGTH_SHORT).show();
                }
            }
            if (!pwd.equals(ensurepwd)){
                Toast.makeText(RegisterActivity.this,"两次输入密码不一致,请重新输入",Toast.LENGTH_SHORT).show();
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        registeRequest request= new registeRequest(edit_tel.getText().toString());
                        checkPhone();


                      /*  OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
                        //实体类(用户的实体类,字段要与数据库中的字段一致),因为不知道后台数据库表格设计,在这里先不写,暂时使用以前的person实体类
                        //登录只需要验证用户名和密码是否正确即可
                        Person person = new Person();
                        person.setPhone(edit_tel.getText().toString());
                        person.setPassword(edit_pwd.getText().toString());

                        Gson gson = new Gson();
                        String json = gson.toJson(person); //括号中应填实体类的变量名
                        RequestBody requestbody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);
                        String url = "http://10.133.1.148:8080/Yuang_Client_Server_Data_Exchange/test?action=register";//要填写后台的访问路径
                        Request request = new Request.Builder().url(url).post(requestbody).build();
                        Response response=null;
                        try {
                            response= okHttpClient.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            //Gson gson1=new Gson();
                            //String resultJson=gson.toJson(response.body().string());
                            String result=response.body().string();
                            ResultJSONBean resultJsonBean=new ResultJSONBean();
                            resultJsonBean=gson.fromJson(result,ResultJSONBean.class);
                            System.out.println(resultJsonBean);

                            String responseResult=resultJsonBean.getResult();
                            if(responseResult.equals("成功了")){
                                SavePhone();
                                handler1.sendEmptyMessage(1);
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                handler1.sendEmptyMessage(0);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                }).start();
            }
        }
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    // 验证手机号
    private boolean judgePhoneNumber(String phoneNum) {
        if (isMachLength(phoneNum, 11) && isMobileNum(phoneNum)) {
            return true;
        }
//        Toast.makeText(LoginActivity.this, "手机号码输入有误", 0).show();
        return false;
    }

    // 判断手机格式
    private boolean isMobileNum(String phoneNum) {
        String telRegex = "[1][358]\\d{9}";
        if (TextUtils.isEmpty(phoneNum))
            return false;
        return phoneNum.matches(telRegex);
    }

    // 判断位数
    private boolean isMachLength(String phoneNum, int len) {
        if (phoneNum.isEmpty())
            return false;
        return phoneNum.length() == len ? true : false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class MyTextWatcher implements TextWatcher {

        private MyEditView view;

        public MyTextWatcher(MyEditView view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // Toast.makeText(LoginActivity.this, s.toString(), 1000).show();

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }


        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_tel:
                    if (judgePhoneNumber(editable.toString())) {
                        phone = editable.toString();
                        //Toast.makeText(RegisterActivity.this,"手机号格式错误",Toast.LENGTH_SHORT).show();
                        // lq.savePhone(phone);
                        btn_registeruser.setEnabled(true);
                    } else {
                        //btn_register.setEnabled(false);
                        btn_registeruser.setEnabled(false);
                    }
                    break;
                case R.id.edit_pwd:
                        /*if(!edit_tel.getText().toString().equals("")&&!editable.toString() .equals("")){
                            pwd = editable.toString();
                            //Toast.makeText(LoginActivity.this, yzm, 1000).show();
                            btn_registeruser.setEnabled(true);
                        }else{
                            btn_registeruser.setEnabled(false);
                        }*/
                    break;
                case R.id.edit_ensurepwd:
                      /*  if(!edit_tel.getText().toString().equals("")&&!edit_pwd.getText().toString().equals("")&&!editable.toString() .equals("")) {
                            ensurepwd = editable.toString();
                            //Toast.makeText(LoginActivity.this, yzm, 1000).show();
                            btn_registeruser.setEnabled(true);
                        }else{
                            btn_registeruser.setEnabled(false);
                        }*/

                    break;

                default:
                    break;
            }

        }

    }
}
