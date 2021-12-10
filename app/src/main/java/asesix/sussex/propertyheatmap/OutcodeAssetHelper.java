package asesix.sussex.propertyheatmap;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class OutcodeAssetHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "outcode_latlongs.db";
    private static final int DATABASE_VERSION = 1;

    public OutcodeAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
