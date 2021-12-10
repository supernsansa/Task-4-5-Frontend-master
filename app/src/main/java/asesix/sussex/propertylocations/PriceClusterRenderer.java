package asesix.sussex.propertylocations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;

import java.util.ArrayList;

/**
 * TODO: make clusters smaller
 * TODO: make individual marker colour change depending on price (maybe)
 */
public class PriceClusterRenderer extends DefaultClusterRenderer<Property> {

    private Context context;

    PriceClusterRenderer(Context context, GoogleMap map, ClusterManager<Property> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected String getClusterText(int bucket) {
        return "£" + String.valueOf(bucket);
    }

    @Override
    protected int getColor(int clusterSize) {
        return Color.DKGRAY;
    }

    //Calculate average price of properties in cluster
    @Override
    protected int getBucket(Cluster<Property> cluster) {
        int averagePrice = 0;
        Property[] properties = cluster.getItems().toArray(new Property[0]);

        for(Property p : properties) {
            averagePrice = (int) (averagePrice + p.getSoldPrice());
        }
        averagePrice = averagePrice/(cluster.getSize());

        return averagePrice;
    }
}