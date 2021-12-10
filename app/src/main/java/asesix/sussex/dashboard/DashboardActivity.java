package asesix.sussex.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.jetbrains.annotations.NotNull;

import asesix.sussex.R;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.propertyheatmap.MapsActivity;
import asesix.sussex.propertylocations.locationdetails.locationlist.view.LocationListActivity;
import asesix.sussex.user.passwordupdate.view.ChangePasswordActivity;
import asesix.sussex.user.profile.view.UserProfileActivity;
import asesix.sussex.userauthentication.login.model.LoginPoJo;
import asesix.sussex.userauthentication.login.view.LogInActivity;
import asesix.sussex.userauthentication.registration.view.RegistrationActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardActivity extends AppCompatActivity {


    CardView cvShowMap,cvSowLocation,cvShowProfile,cvChangePassword,cvLogout;

    CustomSharedPreferences customSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout_activity);


        customSharedPreferences=new CustomSharedPreferences(this);

        initView();
        cvShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMapintent=new Intent(DashboardActivity.this, MapsActivity.class);
                startActivity(showMapintent);

            }
        });


        cvSowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMapintent=new Intent(DashboardActivity.this, LocationListActivity.class);
                startActivity(showMapintent);
                finish();

            }
        });


        cvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMapintent=new Intent(DashboardActivity.this, ChangePasswordActivity.class);
                startActivity(showMapintent);
                //finish();

            }
        });

        cvShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent showMapintent = new Intent(DashboardActivity.this, UserProfileActivity.class);
                startActivity(showMapintent);
            }
            //finish();
        });


        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(DashboardActivity.this, LogInActivity.class);
                logOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                logOutIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                customSharedPreferences.setIsLogin(false);
                startActivity(logOutIntent);
            }
        });

    }





    void initView() {
        cvShowMap=(CardView)findViewById(R.id.showMapCard);
        cvChangePassword=(CardView)findViewById(R.id.showChangePasswordCard);
        cvShowProfile=(CardView)findViewById(R.id.showUserProfileCard);
        cvSowLocation=(CardView)findViewById(R.id.showLocationsCard);
        cvLogout=(CardView)findViewById(R.id.showLogoutCard);

    }



}



