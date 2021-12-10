package asesix.sussex.user.profile.model;

public class UpdateProfilePoJo {

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    private String firstname;
    private String lastname;

    public UpdateProfilePoJo(String firstname,String lastname)
    {
        this.firstname=firstname;
        this.lastname=lastname;
    }
}
