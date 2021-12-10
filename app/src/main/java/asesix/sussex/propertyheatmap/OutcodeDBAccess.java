package asesix.sussex.propertyheatmap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class OutcodeDBAccess {
    private OutcodeAssetHelper outcodeAssetHelper;
    private SQLiteDatabase outcode_db;
    private static OutcodeDBAccess instance;
    Cursor c = null;

    private OutcodeDBAccess(Context context) {
        this.outcodeAssetHelper = new OutcodeAssetHelper(context);
    }

    //To return single instance of database
    public static OutcodeDBAccess getInstance(Context context) {
        if(instance==null) {
            instance = new OutcodeDBAccess(context);
        }
        return instance;
    }

    //To open the database
    public void open() {
        this.outcode_db = outcodeAssetHelper.getWritableDatabase();
    }

    //To close the database
    public void close() {
        if(outcode_db!=null) {
            outcode_db.close();
        }
    }

    //Method for querying an outcode from coordinates
    public String getOutcode(double latitude, double longitude) {
        c=outcode_db.rawQuery(("select postcode, latitude, longitude from outcode_latlongs where latitude = "
                + String.valueOf(latitude) +  " and longitude = " +
                String.valueOf(longitude)), new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()) {
            String outcode = c.getString(0) + " " +
                    String.valueOf(c.getDouble(1)) + " " +
                    String.valueOf(c.getDouble(2));
            buffer.append(" "+outcode);
        }
        return buffer.toString();
    }

    //Method for retrieving all outcodes within a range of coordinates
    public ArrayList<Postcode> getVisibleOutcodes(double northeast_lat, double northeast_long, double southwest_lat, double southwest_long) {
        ArrayList<Postcode> visibleOutcodes = new ArrayList<Postcode>();

        c=outcode_db.rawQuery( ("select * from outcode_latlongs where (latitude between " + String.valueOf(southwest_lat) +" and " + String.valueOf(northeast_lat) +
                ") and (longitude between " + String.valueOf(southwest_long) + " and " + String.valueOf(northeast_long) + ")" ),new String[]{});

        while(c.moveToNext()) {
            //Get row data
            String outcode = c.getString(1);
            // Debug - System.err.println(outcode);
            double latitude = c.getDouble(2);
            double longitude = c.getDouble(3);
            //Make Postcode object
            Postcode newOutcode = new Postcode(outcode,latitude,longitude);
            //Add to ArrayList
            visibleOutcodes.add(newOutcode);
        }

        c.close();
        outcode_db.close();

        return visibleOutcodes;
    }

    //Method for retrieving all outcodes from the database
    public ArrayList<Postcode> getAllOutcodes() {
        ArrayList<Postcode> allOutcodes = new ArrayList<Postcode>();

        c=outcode_db.rawQuery(("select * from outcode_latlongs"),new String[]{});

        //If result is not empty
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                //Get row data
                String outcode = c.getString(1);
                double latitude = c.getDouble(2);
                double longitude = c.getDouble(3);
                //Make Postcode object
                Postcode newOutcode = new Postcode(outcode, latitude, longitude);
                //Add to ArrayList
                allOutcodes.add(newOutcode);
            }
        }
        c.close();
        outcode_db.close();

        return allOutcodes;
    }
}