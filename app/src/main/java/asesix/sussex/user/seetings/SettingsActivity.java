package asesix.sussex.user.seetings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import asesix.sussex.R;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.dashboard.DashboardActivity;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavLocationResponsePoJo;
import asesix.sussex.propertylocations.locationdetails.locationlist.view.FavouriteLocationsItemAdapter;
import asesix.sussex.user.passwordupdate.view.ChangePasswordActivity;
import asesix.sussex.user.profile.view.UserProfileActivity;
import asesix.sussex.userauthentication.login.view.LogInActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity  extends AppCompatActivity {


CardView cvShowProfile,cvChangePassword,cvLogout,cvDelete;

        CustomSharedPreferences customSharedPreferences;
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_settings_activity);

            cvChangePassword=(CardView)findViewById(R.id.showChangePasswordCard);
            cvShowProfile=(CardView)findViewById(R.id.showUserProfileCard);
            cvLogout=(CardView)findViewById(R.id.cvUserLogout);
            cvDelete=(CardView)findViewById(R.id.cvDeleteUser);
            customSharedPreferences=new CustomSharedPreferences(SettingsActivity.this);

            cvChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent showMapintent=new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                    startActivity(showMapintent);
                    finish();

                }
            });

            cvShowProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent showMapintent = new Intent(SettingsActivity.this, UserProfileActivity.class);
                    startActivity(showMapintent);
                    finish();
                }
                //finish();
            });


            cvLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent logOutIntent = new Intent(SettingsActivity.this, LogInActivity.class);
                    logOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    logOutIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    customSharedPreferences.setIsLogin(false);
                    startActivity(logOutIntent);
                }
            });

            cvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    LayoutInflater inflater = ((Activity) v.getContext()).getLayoutInflater();
                    View alertView = inflater.inflate(R.layout.delete_data_dialog, null);
                    alertDialog.setView(alertView);

                    // notifyItemRemoved( position);
                    final AlertDialog show = alertDialog.show();

                    Button alertButton = (Button) alertView.findViewById(R.id.bt_delete_location);
                    Button alertCancelButton = (Button) alertView.findViewById(R.id.bt_cancel_location);
                    TextView tvMsg=(TextView)alertView.findViewById(R.id.tV_delete);
                    tvMsg.setText("Are you sure you want to delete your account");

                    alertCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            show.dismiss();
                        }
                    });
                    alertButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ApiConstants.isInternetConnected(SettingsActivity.this)) {
                                deleteUserAccount(customSharedPreferences.getStringValue("userId"));
                                // notifyItemRemoved( position);
                            }
                            else
                            {
                                ApiConstants.showToast(SettingsActivity.this,ApiConstants.noNetworkAvailable);
                            }

                            //notify();
                            show.dismiss();
                        }
                    });
                }
            });




        }






    public  void deleteUserAccount(String userId) {

        customSharedPreferences=new CustomSharedPreferences(SettingsActivity.this);

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        // passing data from our text fields to our modal class.

        // calling a method to create a post and passing our modal class.
        Call<ResponseBody> call = retrofitAPI.deleteUserAccount("Bearer " + customSharedPreferences.getStringValue("token"),userId);

        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {




                Log.e("response" , String.valueOf(response.code()));

                if(response.code()!=200)
                {
                    ApiConstants.showToast(SettingsActivity.this,ApiConstants.serverError);
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.getMessage());
                Log.e("Fail" ,""+ t.getMessage().toString());

            }
        });
    }
}


