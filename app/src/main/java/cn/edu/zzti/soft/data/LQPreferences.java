package cn.edu.zzti.soft.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.EditText;

public class LQPreferences {
	
	private static SharedPreferences lqPreferences;
	private final String PHONE = "phone";
	private final String PASSWORD ="password";
	private final String NAME = "name"; // 用户名字
	private final String MONEY = "money"; // 用户的余额
	
	private final String YZM = "yzm";
	
	
	private static LQPreferences lq = new LQPreferences();
	
	public static LQPreferences getLq(Context context) {
		init(context);
		return lq;
	}
	
	
	// 初始化
	public static void init(Context ctx){
		if (lqPreferences==null){
			lqPreferences = ctx.getSharedPreferences("gxcw", Context.MODE_PRIVATE);
		}
	}

	public void savePhone(String phone){
		Editor editor = lqPreferences.edit();
		String Bike_Id = new String(phone);
		editor.putString(PHONE, Bike_Id);
		editor.commit();
	}
	public String getPhone() {
		
		String phone = lqPreferences.getString(PHONE, "");
		Log.d("scccccccccccccccccccc", phone);

		return new String(phone);
		
	}
	
	
	public void savePass(String password){
		Editor editor = lqPreferences.edit();
		editor.putString(PASSWORD, password);
		editor.commit();
	}
	public String getPassword(){
		return lqPreferences.getString(PASSWORD, "");
	}
	
//	public void saveYZM(String yzm){
//		Editor editor = lqPreferences.edit();
//		
//		editor.putString(YZM, yzm);
//		editor.commit();
//	}
//	
//	public String getYZM(){
//		return lqPreferences.getString(YZM, "");
//	}
	
	public void saveMoney(float money){
		Editor ed = lqPreferences.edit();
		ed.putFloat(MONEY, money);
		ed.commit();
	}
	public float getMoney(){
		return lqPreferences.getFloat(MONEY, 0f);
	}
	
	public void saveName(String name){
		Editor ed = lqPreferences.edit();
		ed.putString(NAME, name);
		ed.commit();
	}
	
	public String getName(){
		return lqPreferences.getString(NAME, "");
	}

}
