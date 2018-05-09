package cn.edu.zzti.soft.entity;

import java.io.Serializable;

public class Person implements Serializable{
	
	private int id;
	private String name;
	private String phone;
	private String password;
	private float money;
	private byte[] userImage;
	public Person() {
		
	}
	public Person(int id, String name, String phone, String password, float money) {
		
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.money = money;
	}
	
	
	public byte[] getUserImage() {
		return userImage;
	}
	public void setUserImage(byte[] userImage) {
		this.userImage = userImage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	
	
	

}
