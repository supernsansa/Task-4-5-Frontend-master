package asesix.sussex.propertylocations.locationdetails.locationlist.model;

public class FavouriteLocationPOJO {
    String id;
    String location_name;
    double longitude;
    double latitude;
    String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public  FavouriteLocationPOJO(String strId, String strLocationName, double strLatitude, double strLongitude)
    {
        this.id=strId;
        this.location_name=strLocationName;
        this.latitude=strLatitude;
        this.longitude=strLongitude;

    }






}
