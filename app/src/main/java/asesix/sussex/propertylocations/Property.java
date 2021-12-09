package asesix.sussex.propertylocations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;

/**
 * TODO: Add save/unsave button to label
 */

public class Property implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;
    private double soldPrice;

    public Property(LatLng position, String title, String snippet, int soldPrice) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.soldPrice = soldPrice;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }

    public double getSoldPrice() { return soldPrice; }

}
