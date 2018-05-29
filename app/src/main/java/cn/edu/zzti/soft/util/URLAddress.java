package cn.edu.zzti.soft.util;

/**
 * Created by WYA on 2018/5/23.
 */

public class URLAddress {


    //所有的链接地址都写这里
    static String  sPreURL = "http://39.106.46.201:40861/park/app/";  //前缀
    static String  registe   = "auth/registe";
    static String  login = "auth/login";
    static String  checkPhone =	"check/phone";  //验证手机号是否注册
    static String  userDetail="user/detail"; //查看用户信息

    //订单支付相关
    static String create =  "order/create" ;  //创建订单
    static String charge =  "order/charge";   //支付
    static String orderId = "order/find/by/orderId";  //根据订单ID获得订单信息服务

    public static String getOrderCreate() {
        return sPreURL+create;
    }

    public static String getOrderCharge() {
        return sPreURL+charge;
    }
    public static String getOrderFindByOrderId() {
        return sPreURL+orderId;
    }

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
