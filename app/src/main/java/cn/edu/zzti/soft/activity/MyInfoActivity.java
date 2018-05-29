package cn.edu.zzti.soft.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.util.OkHttpUtil;
import cn.edu.zzti.soft.util.URLAddress;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by WYA on 2018/5/1.
 */

public class MyInfoActivity extends Activity implements View.OnClickListener {
    private ImageView image_title;
    private TextView text_name;

    private TextView t_name;
    private TextView t_phone;
    private TextView t_sex;
    private TextView t_email;

    private RelativeLayout re_name;
    private RelativeLayout re_phone;
    private RelativeLayout re_sex;
    private RelativeLayout re_email;

    public  String  nick;
	public String myphone;
	public  String email;
    public String sex;
	public  static String realToken;

//    PersonDaoImpl person;
//    LQPreferences lq =null;
//    Person p =null;

    public void getRealToken(){
	SharedPreferences pref=getSharedPreferences("phone",MODE_PRIVATE);
	realToken=pref.getString("realToken","");
}

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(MyInfoActivity.this,"服务器响应失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    t_phone.setText(myphone);
                    break;
                case 2:
                    t_name.setText(nick);
                    break;
                case 3:
                    t_email.setText(email);
                    break;
                case 4:
                    t_sex.setText("未设置");
                    break;
                case 5:
                    t_phone.setText("未设置");
                    break;
                case 6:
                    t_name.setText("未设置");
                    break;
                case 7:
                    t_email.setText("未设置");
                    break;
                case 8:
                    t_sex.setText(sex);
                    break;
              /*  case 1:
                    SharedPreferences pref=getSharedPreferences("phone",MODE_PRIVATE);
                    phone=pref.getString("phone","");
                    edit_tel.setText(phone);
                    break;*/
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        init();
        listener();

        seeUserDetail();

//        if(myphone!=null){
//            t_phone.setText(myphone);
//        }else{
//            t_phone.setText("未设置");
//        }
//
//        if (nick!=null){
//            t_name.setText(nick);
//        }else{
//            t_name.setText("未设置");
//        }
//        if(email!=null){
//            t_email.setText(email);
//        }else{
//            t_email.setText("未设置");
//        }
//        t_sex.setText("未设置");

//        lq= LQPreferences.getLq(MyInfoActivity.this);
//        person = new PersonDao(MyInfoActivity.this);
//        System.out.println(lq.getPhone());
//        p =person.getPersons(lq.getPhone());
//        if(p!=null){
//            if(p.getName()!=null){
//                t_name.setText(p.getName());
//
//            }else{
//                t_name.setText("未设置");
//            }
//            t_phone.setText(p.getPassword());
//        }
    }

//    @Override
//    protected void onStart() {
//        t_phone.setText(MyInfoFragment.myphone);
//        Log.d("到了吗","快出来");
//       // Log.d("电话",MyInfoFragment.myphone);
//        if (MyInfoFragment.nick!=null){
//            t_name.setText(MyInfoFragment.nick);
//        }else{
//            t_name.setText("未设置");
//        }
//        if(MyInfoFragment.email!=null){
//            t_email.setText(MyInfoFragment.email);
//        }else{
//            t_email.setText("未设置");
//        }
//        t_sex.setText("未设置");
//
////        lq= LQPreferences.getLq(MyInfoActivity.this);
////        person = new PersonDao(MyInfoActivity.this);
////        System.out.println(lq.getPhone());
////        p =person.getPersons(lq.getPhone());
////        if(p!=null){
////            if(p.getName()!=null){
////                t_name.setText(p.getName());
////
////            }else{
////                t_name.setText("未设置");
////            }
////            t_phone.setText(p.getPassword());
////        }
//        super.onStart();
//    }

public void seeUserDetail() {
		String url = URLAddress.getuserDetail();
        getRealToken();
		Map<String, String> map = new HashMap<String, String>();
		map.put("realToken", realToken);
		OkHttpUtil.postRequestHeader(url, new okhttp3.Callback() {
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				// 提示错误
				Log.i("seeUserDetail()","onFailure");
				handler.sendEmptyMessage(0);
			}

			public void onResponse(Call arg0, Response response)
					throws IOException {
				// Log.i("info",response.body().string() );
				//下面解析JSON，处理
				String result=response.body().string();
				try {
					JSONObject jsonObject =new JSONObject(result);
					String code=jsonObject.getString("code");
					String msg=jsonObject.getString("msg");
					if("200".equals(code)&&"成功".equals(msg)){
						JSONObject data=jsonObject.getJSONObject("data");
						if(data.getString("phone")!=null&&data.getString("phone")!=""){
                            myphone=data.getString("phone");
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(5);
                        }
						if(data.getString("nick")!=null&&data.getString("nick")!=""){
							nick=data.getString("nick");
							handler.sendEmptyMessage(2);
						}else{
                            handler.sendEmptyMessage(6);
                        }
						if( data.getString("email")!=null&&data.getString("email")!=""){
							email=data.getString("email");
							handler.sendEmptyMessage(3);
						}else{
                            handler.sendEmptyMessage(7);
                        }
                        if(data.getString("gender")!=null||data.getString("gender")!=""){
                            sex=data.getString("gender");
                            handler.sendEmptyMessage(8);
                        }else{
                            handler.sendEmptyMessage(4);
                        }

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		//, map
		);
	}

    private void init(){

        image_title = (ImageView)findViewById(R.id.title_img);
        text_name = (TextView)findViewById(R.id.title_name);
        image_title.setImageResource(R.drawable.fx_icon_back_n);

        t_name = (TextView) findViewById(R.id.tv_name);
        t_phone = (TextView)findViewById(R.id.tv_phone);
        t_sex = (TextView)findViewById(R.id.tv_sex);
        t_email=(TextView)findViewById(R.id.tv_email);

        text_name.setText("详细信息");

        re_name =(RelativeLayout) findViewById(R.id.re_name);
        re_phone = (RelativeLayout) findViewById(R.id.re_phone);
        re_sex = (RelativeLayout) findViewById(R.id.re_sex);
        re_email = (RelativeLayout) findViewById(R.id.re_email);

    }

    private void  listener(){
        image_title.setOnClickListener(this);
        //re_loaction.setOnClickListener(this);
        re_name.setOnClickListener(this);
        re_sex.setOnClickListener(this);
        re_phone.setOnClickListener(this);
        re_email.setOnClickListener(this);
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
