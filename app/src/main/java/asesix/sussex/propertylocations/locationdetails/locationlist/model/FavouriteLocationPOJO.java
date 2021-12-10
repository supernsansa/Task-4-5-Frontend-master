package asesix.sussex.propertylocations.locationdetails.locationlist.model;

import java.util.ArrayList;

public class FavouriteLocationPOJO {
    String userId;
    String location_name;
    String postcode;
    public FavouriteLocationPOJO(String userId, String location_name, String postcode, String averagePrice)
    {
         this.userId=userId;
         this.location_name=location_name;
         this.postcode=postcode;
         this.averagePrice=averagePrice;
    }

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

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    private String averagePrice;






}

