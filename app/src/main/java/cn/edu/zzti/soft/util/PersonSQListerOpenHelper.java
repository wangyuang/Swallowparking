package cn.edu.zzti.soft.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonSQListerOpenHelper extends SQLiteOpenHelper{

	public PersonSQListerOpenHelper(Context context) {
		super(context, "person.db", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table person(id Integer primary key autoincrement,"
				+ "name varchar(20),"
				+ "phone varchar(20),"
				+ "username varchar(20),"
				+ "password varchar(20),"
				+ "money float,"
				+ "userImage BLOB)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}
	
	
	

}
