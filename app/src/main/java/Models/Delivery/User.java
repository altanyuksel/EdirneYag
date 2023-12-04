package Models.Delivery;

import com.google.gson.annotations.SerializedName;

public class User {
    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public void set_userType(String _userType) {
        this._userType = _userType;
    }

    @SerializedName("UserName")
    private String _userName;

    @SerializedName("Password")
    private String _password;

    @SerializedName("UserType")
    private String _userType;

    public String get_userName(){
        return _userName;
    }

    public String get_password(){
        return _password;
    }

    public String get_userType(){
        return _userType;
    }

}