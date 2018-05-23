package cn.edu.zzti.soft.entity;

/**
 * Created by WYA on 2018/5/23.
 */

public class registeRequest {
    public String phone;
    public String password;
    public String nick;
    public String email;

    public registeRequest(String phone) {
        this.phone = phone;
    }

    public registeRequest(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public registeRequest(String phone, String password, String nick, String email) {
        this.phone = phone;
        this.password = password;
        this.nick = nick;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
