package cn.edu.zzti.soft.entity;

/**
 * Created by WYA on 2018/5/23.
 * 登录,用于解析后台传来的json数据
 */

public class loginResponseJson {
    public Integer code;
    public String msg;

    public class data{
        public userDetailResponseJson userDetail;
        public tokenResponseJson token;

        public userDetailResponseJson getUserDetail() {
            return userDetail;
        }

        public void setUserDetail(userDetailResponseJson userDetail) {
            this.userDetail = userDetail;
        }

        public tokenResponseJson getToken() {
            return token;
        }

        public void setToken(tokenResponseJson token) {
            this.token = token;
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
