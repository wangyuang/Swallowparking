package cn.edu.zzti.soft.entity;

/**
 * Created by WYA on 2018/5/27.
 * 订单支付响应回来的Json数据
 */

public class orderChargeResponseJson {
    public Integer code;
    public String msg;
    public String data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
