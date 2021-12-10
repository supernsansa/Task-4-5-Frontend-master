package asesix.sussex.user.passwordupdate.model;

public class ChangePasswordPoJo {

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    private String oldPassword;
    private String newPassword;

    public ChangePasswordPoJo(String oldPassword,String newPassword)
    {
        this.oldPassword=oldPassword;
        this.newPassword=newPassword;
    }
}
