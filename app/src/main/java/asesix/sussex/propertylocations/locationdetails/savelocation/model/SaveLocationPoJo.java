package asesix.sussex.propertylocations.locationdetails.savelocation.model;

public class SaveLocationPoJo {


      String userId;
      String location_name;
      double longitude;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    double latitude;
       String colour;

      public SaveLocationPoJo(String strUserId,double dbLongitude,double dbLatitude,String location_name,String colour)
      {
          this.userId=strUserId;
          this.location_name=location_name;
          this.longitude=dbLongitude;
          this.latitude=dbLatitude;
          this.colour=colour;

      }
}
