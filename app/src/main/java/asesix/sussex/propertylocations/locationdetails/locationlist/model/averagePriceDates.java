package asesix.sussex.propertylocations.locationdetails.locationlist.model;

public class averagePriceDates
{
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    private  String averagePrice;
    public averagePriceDates(String date,String averagePrice)
    {
        this.date=date;
        this.averagePrice=averagePrice;
    }
}
