package cn.edu.zzti.soft.entity;

/**
 * Created by WYA on 2018/5/23.
 * 查看用户信息
 */

public class seeUserDetailResponseJson {
    public Integer code;
    public String msg;

    public class data{
        public userDetailResponseJson userDetail;
    }

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
}
