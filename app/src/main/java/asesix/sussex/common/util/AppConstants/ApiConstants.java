package asesix.sussex.common.util.AppConstants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import org.intellij.lang.annotations.RegExp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiConstants {

    public static final String BASE_URL = "http://3.9.171.61:8080/";
    //static const String BASE_URL="https://covid-19.dataflowkit.com/";
    static  String SUB_URL="/address";
    public static final String  Sign_Up = "/auth/signup";
   public  static  final String Sign_In= "/auth/signin";
    public static final String GET_User_LocationById="/location/findByUserId/{id}";
    public static final String DELETE_User_LocationById="/location/delete/{id}";
    public   static final  String Add_User_Location="/location/add";




    public static final String serverError="Something went wrong!!!!";
    public static final String noNetworkAvailable="Please check your internet connection!!";


//    public static boolean isNetworkAvailable(Context mContext) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

    public static boolean isValidEmail(String RequestedEmail) {

        Matcher m = Pattern.compile(" \"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]\"\n" +
                " {8}r\"{0,253}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]\"\n" +
                " {8}r\"{0,253}[a-zA-Z0-9])?)*$\"").matcher(RequestedEmail);

        return m.matches();
    }


    public static void showToast(Context mContext, String strMessage) {
        final Toast toast = Toast.makeText(mContext, strMessage, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 2000);
    }

    public static boolean isInternetConnected(Context getApplicationContext) {
        boolean status = false;

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (cm.getActiveNetwork() != null && cm.getNetworkCapabilities(cm.getActiveNetwork()) != null) {
                    // connected to the internet
                    status = true;
                }

            } else {
                if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
                    // connected to the internet
                    status = true;
                }
            }
        }

        return status;
    }

}
