package asesix.sussex.propertyheatmap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostcodeDBAccess {

    private PostcodeAssetHelper postcodeAssetHelper;
    private SQLiteDatabase postcode_db;
    private static PostcodeDBAccess instance;
    Cursor c = null;

    private PostcodeDBAccess(Context context) {
        this.postcodeAssetHelper = new PostcodeAssetHelper(context);
    }

    //To return single instance of database
    public static PostcodeDBAccess getInstance(Context context) {
        if(instance==null) {
            instance = new PostcodeDBAccess(context);
        }
        return instance;
    }

    //To open the database
    public void open() {
        this.postcode_db = postcodeAssetHelper.getWritableDatabase();
    }

    //To close the database
    public void close() {
        if(postcode_db!=null) {
            postcode_db.close();
        }
    }

    //Method for retrieving all postcodes within a range of coordinates
    public ArrayList<Postcode> getVisiblePostcodes(double northeast_lat, double northeast_long, double southwest_lat, double southwest_long) {
        ArrayList<Postcode> visiblePostcodes = new ArrayList<Postcode>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        c=postcode_db.rawQuery( ("select * from postcode_latlongs where (latitude between " + String.valueOf(southwest_lat) +" and " + String.valueOf(northeast_lat) +
                ") and (longitude between " + String.valueOf(southwest_long) + " and " + String.valueOf(northeast_long) + ") limit 350" ),new String[]{});

        //If result is not empty
        if (c.getCount() > 0) {
            while(c.moveToNext()) {
                //Get row data
                String postcode = c.getString(1);
                // Debug - System.err.println(outcode);
                double latitude = c.getDouble(2);
                double longitude = c.getDouble(3);
                //Make Postcode object
                Postcode newPostcode = new Postcode(postcode,latitude,longitude);
                visiblePostcodes.add(newPostcode);
            }
        }
        c.close();
        postcode_db.close();

        return visiblePostcodes;
    }

    //Method for querying a postcode from coordinates
    public String getPostcode(double latitude, double longitude) {
        c=postcode_db.rawQuery(("select postcode from postcode_latlongs ORDER BY ABS(latitude - " +
                        latitude + ") AND ABS(longitude - " + longitude + ") LIMIT 1"),
                new String[]{});
        StringBuffer buffer = new StringBuffer();
        String postcode = "No Result";
        while(c.moveToNext()) {
            postcode = c.getString(0);
        }
        c.close();
        postcode_db.close();

        return postcode;
    }

}