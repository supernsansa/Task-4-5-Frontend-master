package asesix.sussex.propertylocations.locationdetails.locationlist.model;

import java.util.ArrayList;

public class FavLocationResponsePoJo {

    String id;
    String location_name;
    String postcode;
    ArrayList<averagePriceDates>averagePriceDates;
    user user;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String latitude;
    String longitude;

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

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public ArrayList<asesix.sussex.propertylocations.locationdetails.locationlist.model.averagePriceDates> getAveragePriceDates() {
        return averagePriceDates;
    }

    public void setAveragePriceDates(ArrayList<asesix.sussex.propertylocations.locationdetails.locationlist.model.averagePriceDates> averagePriceDates) {
        this.averagePriceDates = averagePriceDates;
    }

    public asesix.sussex.propertylocations.locationdetails.locationlist.model.user getUser() {
        return user;
    }

    public void setUser(asesix.sussex.propertylocations.locationdetails.locationlist.model.user user) {
        this.user = user;
    }

    public FavLocationResponsePoJo(String id, String location_name, String postcode, user user, ArrayList<asesix.sussex.propertylocations.locationdetails.locationlist.model.averagePriceDates> averagePriceDates)
    {
        this.id=id;
        this.location_name=location_name;
        this.postcode=postcode;
        this.user=user;
        this.averagePriceDates=averagePriceDates;

    }
}

