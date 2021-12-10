package asesix.sussex.propertyheatmap;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class PostcodeAssetHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "postcode_latlongs.db";
    private static final int DATABASE_VERSION = 1;

    public PostcodeAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
