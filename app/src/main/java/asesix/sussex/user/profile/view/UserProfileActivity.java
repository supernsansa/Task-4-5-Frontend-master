package asesix.sussex.user.profile.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import asesix.sussex.R;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.dashboard.DashboardActivity;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.propertyheatmap.MapsActivity;
import asesix.sussex.user.passwordupdate.view.ChangePasswordActivity;
import asesix.sussex.user.profile.model.GetUserProfilePoJo;
import asesix.sussex.user.profile.model.UpdateProfilePoJo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileActivity extends AppCompatActivity {
    View view;

    private TextView tvEmail;
    private EditText eTFirstName;
    private EditText eTLastName;
    private ProgressBar loadingPB;
    private Button bTUpdateProfile;
    private RelativeLayout iVBack;


    CustomSharedPreferences customSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_fragment);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserProfileActivity.this);
        tvEmail=(TextView)findViewById(R.id.tv_EmailId);
        eTFirstName=(EditText)findViewById(R.id.eT_FirstName);
        eTLastName=(EditText)findViewById(R.id.eT_LastName) ;
        bTUpdateProfile=(Button)findViewById(R.id.bT_update_profile);


        loadingPB =(ProgressBar) findViewById(R.id.idLoadingPB);



        iVBack=(RelativeLayout) findViewById(R.id.iv_back);


        iVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("clicked","not working");
                Intent redirectToDashBoard=new Intent(UserProfileActivity.this, DashboardActivity.class);
                startActivity(redirectToDashBoard);
                finish();
            }
        });


        if(ApiConstants.isInternetConnected(UserProfileActivity.this)) {
            getUserProfile();
        }
        else
        {
            ApiConstants.showToast(UserProfileActivity.this,ApiConstants.noNetworkAvailable);
        }

        bTUpdateProfile.setOnClickListener(v -> {
            if(ApiConstants.isInternetConnected(UserProfileActivity.this)) {
                updateUserProfile(eTFirstName.getText().toString(),eTLastName.getText().toString());
            }
            else
            {
                ApiConstants.showToast(UserProfileActivity.this,ApiConstants.noNetworkAvailable);
            }
        });
        // if(favouriteLocationsItemAdapter!=null) {

        // }

    }



    public  void getUserProfile() {
        customSharedPreferences=new CustomSharedPreferences(UserProfileActivity.this);

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);

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


        GetUserProfilePoJo getUserProfilePoJo=new GetUserProfilePoJo(customSharedPreferences.getStringValue("userId") ,""
        ,"","");

        // calling a method to create a post and passing our modal class.
        Call<GetUserProfilePoJo> call = retrofitAPI.getUserDetails("Bearer " + customSharedPreferences.getStringValue("token") ,
                customSharedPreferences.getStringValue("userId"));

        Log.e("retrofit", String.valueOf(call.request()));


//
        // on below line we are executing our method.
        call.enqueue(new Callback<GetUserProfilePoJo>() {
            @Override
            public void onResponse(@NotNull Call<GetUserProfilePoJo> call, @NotNull Response<GetUserProfilePoJo> response) {
                // this method is called when we get response from our api.
                // Toast.makeText(RegistrationController.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);
                Log.e("response",response.message());
                ApiConstants.showToast(UserProfileActivity.this,response.message());

                if(response.code()==200) {
                   GetUserProfilePoJo getUserProfilePoJo=response.body();
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                    builder.setMessage("Profile updated!!")
                            .setTitle("Profile")
                            .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity


                                    tvEmail.setText(response.body().getEmail());
                                    eTFirstName.setText(response.body().getFirstname().toString());
                                    eTLastName.setText(response.body().getLastname());
//

                                }
                            });


                    AlertDialog dialog = builder.create();
                    dialog.show();






                }
                else
                {
                    ApiConstants.showToast(UserProfileActivity.this,response.message());

                }




            }

            @Override
            public void onFailure(@NotNull Call<GetUserProfilePoJo> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.getMessage());
                ApiConstants.showToast(UserProfileActivity.this,ApiConstants.serverError);
                Log.e("response",t.getMessage());

                loadingPB.setVisibility(View.GONE);
            }
        });
    }




    public  void updateUserProfile(String strFirstName,String strLastName) {
        customSharedPreferences=new CustomSharedPreferences(UserProfileActivity.this);

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);

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

        UpdateProfilePoJo updateProfilePoJo=new UpdateProfilePoJo(strFirstName,strLastName);


        // calling a method to create a post and passing our modal class.
        Call<UpdateProfilePoJo> call = retrofitAPI.upDateProfilePost("Bearer " + customSharedPreferences.getStringValue("token") ,
               updateProfilePoJo );

//
        // on below line we are executing our method.
        call.enqueue(new Callback<UpdateProfilePoJo>() {
            @Override
            public void onResponse(@NotNull Call<UpdateProfilePoJo> call, @NotNull Response<UpdateProfilePoJo> response) {
                // this method is called when we get response from our api.
                // Toast.makeText(RegistrationController.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                if(response.code()==200) {

                    ApiConstants.showToast(UserProfileActivity.this,"Profile updated!!");




                }
                else
                {
                    ApiConstants.showToast(UserProfileActivity.this,response.message());

                }




            }

            @Override
            public void onFailure(@NotNull Call<UpdateProfilePoJo> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.getMessage());
                ApiConstants.showToast(UserProfileActivity.this,ApiConstants.serverError);

                loadingPB.setVisibility(View.GONE);
            }
        });
    }
}

