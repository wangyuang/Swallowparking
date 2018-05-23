package cn.edu.zzti.soft.util;

/**
 * Created by WYA on 2018/5/23.
 */

public class URLAddress {


    //所有的链接地址都写这里
    static String  sPreURL = "http://39.106.46.201:40861/park/app/";  //前缀
    static String  registe   = "auth/registeResponseJson";
    static String  login = "auth/loginResponseJson";
    static String  checkPhone =	"check/phone";  //验证手机号是否注册
    static String  userDetail="user/detail"; //查看用户信息
    public static String getCheckPhone()
    {
        return sPreURL+checkPhone;
    }

    public static String getRegisteURL()
    {
        return sPreURL+registe;
    }

    public static String getLoginURL()
    {
        return sPreURL+login;
    }

    public static String getuserDetail(){
        return sPreURL+userDetail;
    }
}
