package cn.edu.zzti.soft.serviceImpi;

public interface GxcwUserService {
	
	
   public boolean LoginService(String phone, String password);
   
   public boolean RegisterService(String phone, String password);
   
   public boolean UpdatePhoneService(String phone);

}
