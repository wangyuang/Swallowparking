package cn.edu.zzti.soft.dao;

import android.R.fraction;

import cn.edu.zzti.soft.entity.Person;

public interface PersonDaoImpl {
	
	public void addPerson(String phone, String password);
	// 修改用户名	
	public boolean updateName(String phone, String name);
	
	// 修改密码
	public boolean updatePassword(String phone, String password);
	
	//修改余额
	public boolean updateMoney(String phone, float money);
	
	// 修改手机号
	public boolean updatePhone(String oldphone, String newphone);
	
	//得到数据
	public boolean getPerson(String phone);

	public float getMoney(String phone);
	public String getPassword(String phone);
	public boolean CheckUser(String phone, String password);
	
	public Person getPersons(String phone);
	
	public boolean UpdateUserImage(String phone, byte[] userImage);
	
	public byte[] readImage(String phone);
}
