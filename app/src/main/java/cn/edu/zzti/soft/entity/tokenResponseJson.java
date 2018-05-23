package cn.edu.zzti.soft.entity;

/**
 * Created by WYA on 2018/5/23.
 * tokenResponseJson,在login的json返回数据的data对象中
 */

public class tokenResponseJson {
    public String tempToken;
    public String realToken;

    public String getTempToken() {
        return tempToken;
    }

    public void setTempToken(String tempToken) {
        this.tempToken = tempToken;
    }

    public String getRealToken() {
        return realToken;
    }

    public void setRealToken(String realToken) {
        this.realToken = realToken;
    }
}
