package cn.edu.zzti.soft.entity;

/**
 * Created by WYA on 2018/5/23.
 * 检查手机号,用于解析后台传来的json数据
 */

public class checkPhoneResponseJson {
    public Integer code;
    public String msg;

    public class data{
        public Boolean exists;

        public Boolean getExists() {
            return exists;
        }

        public void setExists(Boolean exists) {
            this.exists = exists;
        }
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
