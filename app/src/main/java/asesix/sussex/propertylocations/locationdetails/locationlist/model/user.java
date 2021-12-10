package asesix.sussex.propertylocations.locationdetails.locationlist.model;

public class user
{
    public String getFristname() {
        return fristname;
    }

    public void setFristname(String fristname) {
        this.fristname = fristname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    private String fristname;
    private String lastname;
    public user(String fristname,String lastname)
    {
        this.fristname=fristname;
        this.lastname=lastname;
    }

}
