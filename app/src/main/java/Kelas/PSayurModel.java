package Kelas;

/**
 * Created by Glory on 19/09/2018.
 */

public class PSayurModel {
    public String uid;
    public String displayName;
    public String token;
    public String last_login;
    public String check;
    public String phone;
    public String latlon;
    public String status;

    public PSayurModel(String uid, String displayName, String token, String last_login, String check, String phone,
                       String latlon,String status) {
        this.uid = uid;
        this.displayName = displayName;
        this.token = token;
        this.last_login = last_login;
        this.check = check;
        this.phone = phone;
        this.latlon = latlon;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatlon() {
        return latlon;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }
}
