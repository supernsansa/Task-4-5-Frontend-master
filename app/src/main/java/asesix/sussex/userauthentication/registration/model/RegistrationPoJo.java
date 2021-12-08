package asesix.sussex.userauthentication.registration.model;

public class RegistrationPoJo {

    private String firstname;
    private String lastname;
    private  String email;
    private String  password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RegistrationPoJo(String strFirstName, String strLastName, String strEmailId,
                            String strPassword, String strId) {
        this.firstname = strFirstName;
       this.lastname = strLastName;
        this.email=strEmailId;
        this.password=strPassword;
        this.id=strId;
    }

}
