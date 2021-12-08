package asesix.sussex.common.util.sharedprefernce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.internal.Util;

public class CustomSharedPreferences {


    public static Context _context;
    int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "propertyPrices";
    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public CustomSharedPreferences(Context context) {
        // TODO Auto-generated constructor stub
        prefs = getMyPreferences(context);
        _context = context;
        prefs = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }


    private static SharedPreferences getMyPreferences(Context context) {
        return context.getSharedPreferences("propertyPrices",
                Context.MODE_PRIVATE);
    }



    int defaultChildId;

    public boolean isLogin() {
        return prefs.getBoolean("isLogin", false);
    }

    public void setIsLogin(boolean isLogin) {

        Log.d("Login Status", "" + isLogin);
        editor.putBoolean("isLogin", isLogin).commit();
    }

    boolean isLogin;


    public String getStringValue(String key) {
        return prefs.getString(key,"");
    }

    public void setStringValue(String key,String strValue) {
        editor.putString(key, strValue).commit();
    }

    String storedValue;






}
