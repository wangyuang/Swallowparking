package cn.edu.zzti.soft.entity;

/**
 * Created by WYA on 2018/5/27.
 * 根据订单id查询订单
 */

public class orderFindByorderId {
    public Integer code;
    public String msg;
    public orderFindByorderIdData data;

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

    public orderFindByorderIdData getData() {
        return data;
    }

    public void setData(orderFindByorderIdData data) {
        this.data = data;
    }
}
