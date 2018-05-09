package cn.edu.zzti.soft.dao.impl;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import cn.edu.zzti.soft.dao.PersonDaoImpl;
import cn.edu.zzti.soft.entity.Person;
import cn.edu.zzti.soft.util.PersonSQListerOpenHelper;

public class PersonDao implements PersonDaoImpl {

	private PersonSQListerOpenHelper helper;
	private Context context;
	public PersonDao(Context context) {
		this.context = context;
		helper = new PersonSQListerOpenHelper(context);
	}

	@Override
	public void addPerson(String phone, String password) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("password", password);
		try{
			db.insert("person", null, values);
			db.close();
			Toast.makeText(context, "插入成功", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(context, "插入失败", Toast.LENGTH_SHORT).show();
		}
		db.close();

	}

	@Override
	public boolean updateName(String phone,String name) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();		
		values.put("username", name);
		
		try{
			db.update("person", values, "phone=?", new String[]{phone});
			db.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}



	@Override
	public boolean updateMoney(String phone,float money) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("money", money);
		try{
			 float newmoney = db.update("person", values, "phone=?", new String[]{phone});
			 db.close();
			 return true;
		}catch(Exception e){
			return false;
		}
	}


	/**
	 * 得到某个人
	 * */
	
	@Override
	public boolean getPerson(String phone) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		Cursor c = db.query("person", null, "phone=?", new String[]{phone},
				null, null, null);
		
		boolean r = c.moveToNext();
		c.close();
		db.close();
		return r;
	}
	@Override
	public boolean CheckUser(String phone,String password) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		Cursor c = db.query("person", null, "phone=?", new String[]{phone},
				null, null, null);
	
		while (c.moveToNext()) {
			
			String passwords = c.getString(c.getColumnIndex(password));
			if(password.equals(passwords)){
				c.close();
				db.close();
				return true;
			}
		}
		c.close();
		db.close();
		return false;
	}
	@Override
	public Person getPersons(String phone){
		
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		Cursor c =db.query("person", null, "phone=?", new String[]{phone},
				null, null, null);
		Person person = new Person();
		
		while(c.moveToNext()){
		 	String password = c.getString(c.getColumnIndex("password"));
			int id = c.getInt(c.getColumnIndex("id"));
			String name = c.getString(c.getColumnIndex("username"));
			float money = c.getFloat(c.getColumnIndex("money"));
			
		    //System.out.println("password:"+password+"id:"+id+"name:"+name+"money:");
			person.setPassword(password);
			person.setId(id);
			person.setPhone(phone);
			person.setMoney(money);
			person.setName(name);
		}
		c.close();
		db.close();
		return person;
	}
	
	/**
	 * 
	 * 得到密码
	 * */
	@Override
	public String getPassword(String phone){
		
		SQLiteDatabase db = helper.getWritableDatabase();
		String password =null;
		Cursor c = db.query("person", null, "phone=?", new String[]{phone},
				null, null, null);
		if(c.moveToNext()){
			password=c.getString(c.getColumnIndex("password"));
		}
		c.close();
		db.close();
		return password;
	}
	@Override
	public float getMoney(String phone){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		float money = 0.0f;
		
		Cursor c = db.query("person", null, "phone=?",
					new String[]{phone}, null, null, null);
		if(c.moveToNext()){
			money = c.getFloat(c.getColumnIndex("money"));
		}
		c.close();
		db.close();
		return money;
	}
	public float JianMoney(float money){
		return 0f;
	}
	@Override
	public boolean UpdateUserImage(String phone ,byte[] userImage){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("userImage", userImage);
		try{
			db.update("person", values, "phone=?", new String[]{phone});
			db.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}
	/**
	 * 
	 * 得到数据库中储存的图片
	 * 
	 * */
	@Override
	public byte[] readImage(String phone){
		
		SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cur=db.query("person", new String[]{"userImage"}, "phone=?", new String[]{phone}, null, null, null);
        byte[] imgData=null;
        if(cur.moveToNext()){
        //将Blob数据转化为字节数组
            imgData=cur.getBlob(cur.getColumnIndex("userImage"));
        }
        return imgData;
		
	}

	
	/***
	 * 
	 * 更新数据库中的密码密码
	 * */
	@Override
	public boolean updatePassword(String phone, String password) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put("password", password);
		try{
			 db.update("person", values, "phone=?", new String[]{phone});
			 db.close();
			 return true;
		}catch(Exception e){
			return false;
		}
	}

	
	/***
	 * 
	 * 更改手机号
	 * */
	@Override
	public boolean updatePhone(String oldphone, String newphone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put("phone", newphone);
		try{
			 db.update("person", values, "phone=?", new String[]{oldphone});
			 db.close();
			 return true;
		}catch(Exception e){
			return false;
		}
	}
	

}
