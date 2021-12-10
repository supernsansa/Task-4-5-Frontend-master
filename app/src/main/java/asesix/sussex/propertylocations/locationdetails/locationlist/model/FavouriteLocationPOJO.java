package asesix.sussex.propertylocations.locationdetails.locationlist.model;

import java.util.ArrayList;

public class FavouriteLocationPOJO {
    String userId;
    String latitude;
    String longitude;
    String postcode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public FavouriteLocationPOJO(String userId, String latitude, String longitude, String postcode, String averagePrice)
    {
         this.userId=userId;
         this.longitude=longitude;
         this.latitude=latitude;
         this.postcode=postcode;
         this.averagePrice=averagePrice;
    }


    private String averagePrice;






}

