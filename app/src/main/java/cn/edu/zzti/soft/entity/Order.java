package cn.edu.zzti.soft.entity;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zzti.soft.activity.MyInfoActivity;
import cn.edu.zzti.soft.util.OkHttpUtil;
import cn.edu.zzti.soft.util.URLAddress;
import okhttp3.Response;

/**
 * Created by laz on 2018-05-24.
 */

public class Order {
    String openid="0";
    String orderId="0";   //订单id 由 Web服务端产生
    String realToken; //  header中的数据(必传)header 用户后全局存在
    String type="park";     //订单类型(必传)
    double amount;        // 订单总金额(元)(必传)
    String channel= "alipay";  //订单支付方式，默认阿里支付宝
    String orderCertificate;   //订单凭证
    String deviceId="000000";      //设备号(必传)
    String parkingSdate="0000-01-01 00:00:00";//停车开始时间 (yyyy-MM-dd HH:mm:ss)(必传)
    String parkingEdate="0000-01-01 01:00:00";//停车结束时间 (yyyy-MM-dd HH:mm:ss)(必传)


    public Order(String openid, String realToken, String type, double amount, String deviceId, String parkingSdate, String parkingEdate) {
        this.openid = openid;
        this.realToken = realToken;
        this.type = type;
        this.amount = amount;
        this.deviceId = deviceId;
        this.parkingSdate = parkingSdate;
        this.parkingEdate = parkingEdate;
    }
    public Order(String realToken, double amount) {
        this.realToken = realToken;
        this.amount = amount;
    }

    public String getOrderInfo() {
        httpCreateOrder(); //提交服务器产生订单id；
        String charge =  httpCreateCharge(); //获得支付凭证
        return charge;
    }
    //访问网络服务创建服务端订单，同时获取订单号
    private void httpCreateOrder() {
        String url = URLAddress.getOrderCreate();
        Map<String, String> mapHeader = new HashMap<String, String>();
        mapHeader.put("realToken", MyInfoActivity.realToken);
        Map<String, String> map = new HashMap<String, String>();
        map.put("amount", String.valueOf(this.amount));
        map.put("realToken", MyInfoActivity.realToken);

        Response response = OkHttpUtil.getWithHeaderSync(url, map, mapHeader);
        String s = null;
        try {
            s = response.body().string();
            JSONObject josnObject = new JSONObject(s);
            orderId = josnObject.getString("data");
            Log.i("orderId:", orderId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            orderId = "";
        }
    }

    //访问网络服务获得支付凭证
    private String httpCreateCharge() {
        String url = URLAddress.getOrderCharge();
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderId", orderId );
        map.put("channel", channel );

        Response response = OkHttpUtil.postSync( url, map );
        String s = null;
        try {
            s = response.body().string();
            JSONObject josnObject = new JSONObject( s );
            orderCertificate = josnObject.getString("data").toString();
            Log.i("orderCertificate",orderCertificate);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            orderCertificate = "";
        }
       //下面解析JSON，处理
//                {
//                    "code": 200,
//                    "msg": "成功",
//                    "data": "charset=utf-8&biz_content=%7B%22body%22%3A%22%E5%81%9C%E8%BD%A6%E8%B4%B9%E7%94%A8%22%2C%22out_trade_no%22%3A%22ID15271559238334289%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%81%9C%E8%BD%A6%E8%B4%B9%E7%94%A8%22%2C%22total_amount%22%3A%2212.0%22%7D&method=alipay.trade.app.pay&format=json&notify_url=http%3A%2F%2Fjgrm.net%2Fnotice%2Fweb%2Falipay%2Fwebhooks&app_id=2017110609768045&sign_type=RSA2&version=1.0&timestamp=2018-05-24+18%3A40%3A48&sign=Gu35rFeOp7BKRJNAC%2F6ZJnE0EboBdcCAbRFdkMLkomrZE56fvhuqFHpvXCNVvaRhWO8ISMG602SRpjM9DVoCydLWdI6C5m5dGdeBJPtwt7wYyMzoMz5mIoQOv6RLlt5%2B6m7GGZGHMsM9d5rTevu90J3yjwNtW86%2BDWG%2BsIwai%2Bies6yDo3YOL9G3f%2BZrI2uA41jisMI78Nkm%2Fm%2BJAnUPvC%2FqMmOmd7LdCPaP5Pp5gaIrACSZn0S%2FZ%2FaEugId0gvEPQ7ZTrjhR6%2FZCmOyaC6J6%2FVIcYPrIujWhDyiStPcHuK8EheXNQSuIRw0QbQB3sUns6LA1so%2BMG6y7Lghzwk5wA%3D%3D"
//                }
        return orderCertificate;
    }
}
