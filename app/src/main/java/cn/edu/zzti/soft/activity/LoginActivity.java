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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.entity.registeRequest;
import cn.edu.zzti.soft.util.OkHttpUtil;
import cn.edu.zzti.soft.util.URLAddress;
import cn.edu.zzti.soft.view.MyEditView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Response;

import static cn.edu.zzti.soft.R.id.edit_yzm;

/**
 * Created by WYA on 2018/5/1.
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button btn_wj_password;
    private Button btn_register;
    private Button btn_login;
    private Button btn_yzm;
    private Button btn_yzm_message;

    private MyEditView edit_phone;
    private MyEditView edit_yzm_password;

    private String phone;
    private String yzm;
    private  String password;

    //private LQPreferences lq;
    //private PersonDao p_dao;
    private boolean flag = true;
    private boolean b_yzm = false;
    private int time = 30;

    // 弹出提示框
    private Toast toast;
    // 记录第一次按下的时间
    private long firstPressTime = -1;
    // 记录第二次按下的时间
    private long lastPressTime;
    // 两次按下的时间间隔
    private final long INTERVAL = 2000;

    public static String  realToken;


    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(LoginActivity.this,"该账号未注册,请注册",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    SharedPreferences pref=getSharedPreferences("phone",MODE_PRIVATE);
                    phone=pref.getString("phone","");
                    edit_phone.setText(phone);
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this,"登录失败,请重试",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this,"连接服务器失败,请重试",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(LoginActivity.this,"密码输入错误",Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                b_yzm = false;
                btn_yzm.setText("重新发送（" + time + "）");
            } else if (msg.what == 0) {
                btn_yzm.setText("获取验证码");
                btn_yzm.setClickable(true);
                time = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.d("result", "结果" + msg.arg2);
                Log.d("event", "事件" + msg.arg1);
                // 操作结果成功
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 校验验证码，返回校验的手机和国家代码
                        // 提交验证码成功
                        // Toast.makeText(LoginActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
                        Log.d("success", "成功");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                registeRequest request=new registeRequest(edit_phone.getText().toString());
                                checkPhone(request);
                            }
                        }).start();

                       /* if (p_dao.getPerson(phone) == false) {

                            Intent intent = new Intent(LoginActivity.this, PasswordActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        } else {
                            lq.savePass(password);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }*/
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {// 请求发送验证码
                        // 验证码已经发送
                        Toast.makeText(LoginActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "验证码输入错误", Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

//        p_dao = new PersonDao(LoginActivity.this);
//        lq = LQPreferences.getLq(LoginActivity.this);
//        password = p_dao.getPassword(lq.getPhone());
//        if (!lq.getPhone().equals("") && !lq.getPassword().equals("")) {
//            //lq.savePass(password);
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class)
//                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else {
//
//        }
        setContentView(R.layout.activity_login);
        init();
        initlister();
        toast=Toast.makeText(LoginActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT);
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

    private void init() {
        btn_wj_password = (Button) findViewById(R.id.btn_wj_password);
        btn_yzm_message = (Button) findViewById(R.id.btn_yzm_message);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login=(Button) findViewById(R.id.btn_login);
        btn_yzm = (Button) findViewById(R.id.btn_yzm);
        edit_phone = (MyEditView) findViewById(R.id.edit_phone);
        edit_yzm_password = (MyEditView) findViewById(edit_yzm);
        edit_yzm_password.setHint("密码");
        handler1.sendEmptyMessage(1);
      /*  if (!lq.getPhone().equals("")) {
            // btn_yzm.setVisibility(View.GONE);
            edit_phone.setText(lq.getPhone());
            phone = lq.getPhone();

            btn_register.setText("登  录");
        }*/

        if (judgePhoneNumber(edit_phone.getText().toString())) {
            btn_yzm.setEnabled(true);
        }

        // 验证验证码是否正确
        EventHandler eventHandler = new EventHandler() {
            // 在回调对象被注册的时候被调用
            @Override
            public void onRegister() {
                super.onRegister();
            }

            // 在操作之前被触发
            @Override
            public void beforeEvent(int i, Object o) {
                super.beforeEvent(i, o);
            }

            // 在操作结束的时候被触发
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                Log.d("msg", "event:" + event + "result:" + result + "data:" + data);
                Message msg = new Message();

                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                if (data != null) {
                    msg.what = 2;
                } else {
                    msg.what = 0;
                }
                handler.sendMessage(msg);
            }

            //在反注册的时候被触发
            @Override
            public void onUnregister() {
                super.onUnregister();
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initlister() {
        btn_wj_password.setOnClickListener(this);
        btn_yzm_message.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_yzm.setOnClickListener(this);
        edit_phone.addTextChangedListener(new MyTextWatcher(edit_phone));
        edit_yzm_password.addTextChangedListener(new MyTextWatcher(edit_yzm_password));
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    public void SavePhone(){
        SharedPreferences.Editor editor=getSharedPreferences("phone",MODE_PRIVATE).edit();
        editor.putString("phone",edit_phone.getText().toString());
        editor.putString("realToken",realToken);
        editor.apply();
    }

    public void checkPhone(registeRequest request) {
        String url = URLAddress.getCheckPhone();
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", request.getPhone());
        OkHttpUtil.get(url, new okhttp3.Callback() {
            public void onFailure(Call arg0, IOException arg1) {
                // TODO Auto-generated method stub
                // 提示错误
                Log.i("checkPhone()","onFailure");
                handler1.sendEmptyMessage(3);
            }

            public void onResponse(Call arg0, Response response)
                    throws IOException {
                //Log.i("info",response.body().string() );
                //下面解析JSON，处理
                String result=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    JSONObject data=jsonObject.getJSONObject("data");
                    Boolean exists=data.getBoolean("exists");
                    if(true==exists){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        handler1.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, map);
    }

    public void login( registeRequest request ) {
        String url = URLAddress.getLoginURL();
        Map<String,String> map = new HashMap<String, String>();
        map.put("phone", request.getPhone());
        map.put("password",request.getPassword());
        OkHttpUtil.post(url, new okhttp3.Callback() {
            public void onFailure(Call arg0, IOException arg1) {
                // TODO Auto-generated method stub
                // 提示错误
                Log.i("login()","onFailure");
                handler1.sendEmptyMessage(2);
            }

            public void onResponse(Call arg0, Response response) throws IOException {
                //  Log.i("info",response.body().string() );
                //下面解析JSON，处理
                String result=response.body().string();
                try {
                    JSONObject jsonObject =new JSONObject(result);
                    String code=jsonObject.getString("code");
                    String msg=jsonObject.getString("msg");

                    if("500".equals(code)&&"用户未注册".equals(msg)){
                        handler1.sendEmptyMessage(0);
                        Log.d("11","用户未注册");
                    }else if("500".equals(code)&&"密码错误".equals(msg)){
                        handler1.sendEmptyMessage(4);
                        Log.d("11","密码错误");
                    }else if("200".equals(code)&&"成功".equals(msg)){
                        JSONObject data=jsonObject.getJSONObject("data");
                        JSONObject token=data.getJSONObject("token");
                        JSONObject userDetail=data.getJSONObject("userDetail");
                        String uid=userDetail.getString("uid");
                        String name=userDetail.getString("name");
                        String nick=userDetail.getString("nick");
                        String registTime=userDetail.getString("registTime");
                        String portrait=userDetail.getString("portrait");
                        String intro=userDetail.getString("intro");
                        String gender=userDetail.getString("gender");
                        String phone=userDetail.getString("phone");
                        String password=userDetail.getString("password");
                        String status=userDetail.getString("status");
                        String label=userDetail.getString("label");
                        String email=userDetail.getString("email");
                        String tempToken=token.getString("tempToken");
                        realToken=token.getString("realToken");
                        SavePhone();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if(btn_yzm.getVisibility()!=View.GONE){
                    SMSSDK.submitVerificationCode("86", phone, edit_yzm_password.getText()
                            .toString());
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            registeRequest request = new registeRequest(edit_phone.getText().toString(), edit_yzm_password.getText().toString());
                            login(request);
                       /* OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
                        //实体类(用户的实体类,字段要与数据库中的字段一致),因为不知道后台数据库表格设计,在这里先不写,暂时使用以前的person实体类
                        //登录只需要验证用户名和密码是否正确即可
                        Person person = new Person();
                        person.setPhone(edit_phone.getText().toString());
                        person.setPassword(edit_yzm_password.getText().toString());

                        Gson gson = new Gson();
                        String json = gson.toJson(person); //括号中应填实体类的变量名
                        RequestBody requestbody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);
                        String url = "http://10.133.1.148:8080/Yuang_Client_Server_Data_Exchange/test?action=loginResponseJson";//要填写后台的访问路径
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
                            token=resultJsonBean.getToken();
                            if(responseResult.equals("成功了")){
                                SavePhone();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class)
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


              /*  if (flag != true) {

                } else {

                    if (edit_yzm.getText().toString().equals(password)) {
                        lq.savePass(password);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "密码输入错误", Toast.LENGTH_SHORT).show();
                    }
                }
*/
                break;
            case  R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_yzm_message:
                edit_yzm_password.setHint("请输入验证码:");
                flag = false;
                Log.d("flag", "" + flag);
                btn_yzm.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_wj_password:
                flag = true;
                Log.d("flag", "" + flag);
                edit_yzm_password.setHint("请输入密码");
                btn_yzm.setVisibility(View.GONE);
                break;
            case R.id.btn_yzm:

                phone = edit_phone.getText().toString();
                // 1.首先判断手机号
                if (!judgePhoneNumber(phone)) {
                    return;
                }
                // 2.通过sdk发送验证信息
                // 请求获取验证码
                SMSSDK.getVerificationCode("86", phone);
                // 3.把按钮变成不可点击，并显示倒计时
                btn_yzm.setClickable(false);
                btn_yzm.setText("重新发送（" + time + "）");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; time > 0; time--) {
                            handler.sendEmptyMessage(-1);
                            if (time <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(0);
                    }
                }).start();
                break;
            default:
                break;
        }

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
        SMSSDK.unregisterAllEventHandler();
    }


//	private boolean phoneZZ(String phone) {
//		String regExp = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$";
//
//		Pattern p = Pattern.compile(regExp);
//		Matcher m = p.matcher(phone);
//
//		return m.find();
//	}

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
                case R.id.edit_phone:

                    if (judgePhoneNumber(editable.toString())) {
                        phone = editable.toString();
                        // lq.savePhone(phone);
                        btn_yzm.setEnabled(true);
                    } else {
                        //btn_register.setEnabled(false);
                        btn_yzm.setEnabled(false);

                    }
                    break;
                case edit_yzm:
                    if (!edit_phone.getText().toString().equals("")&&!editable.toString().equals("")) {
                        yzm = editable.toString();
                        //Toast.makeText(LoginActivity.this, yzm, 1000).show();
                        btn_login.setEnabled(true);
                    } else {
                        btn_login.setEnabled(false);
                    }
                    break;
                default:
                    break;
            }

        }

    }

}
