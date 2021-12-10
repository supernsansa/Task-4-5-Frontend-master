package asesix.sussex.user.profile.model;

public class GetUserProfilePoJo {

   private String firstname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String lastname;
    private String email;

    public GetUserProfilePoJo(String id,String firstname,String lastname,String email)
    {
        this.id=id;
        this.firstname=firstname;
        this.lastname=lastname;
        this.email=email;
    }

}
