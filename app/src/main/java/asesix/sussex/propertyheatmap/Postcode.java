package asesix.sussex.propertyheatmap;

public class Postcode {

    private String code;
    private double latitude;
    private double longitude;
    private double price;
    private int numProperties;

    public Postcode(String code, double latitude, double longitude) {
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;
        price = 0;
        numProperties = 0;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public int getNumProperties() { return numProperties; }

    public void setNumProperties(int numProperties) { this.numProperties = numProperties; }
}
