package asesix.sussex.userauthentication.login.model;

public class LoginPoJo {


    String token;
    String password;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String email;
    String userID;

   public LoginPoJo(String email,String password ,String token,  String userID)
    {

        this.email=email;
        this.password=password;
        this.token=token;
        this.userID=userID;
    }
}
