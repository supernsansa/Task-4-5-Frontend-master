package asesix.sussex.user.passwordupdate.view;

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
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import asesix.sussex.R;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.dashboard.DashboardActivity;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.user.passwordupdate.model.ChangePasswordPoJo;
import asesix.sussex.userauthentication.login.view.LogInActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends AppCompatActivity {
    View view;

    private EditText etOldPassword;
    private EditText etNewPassword;
    private ProgressBar loadingPB;
    private Button bTChangePassword;
    ImageView iVShowHideOldPassword;
    ImageView iVShowHidNewPassword;
    RelativeLayout iVBack;

    CustomSharedPreferences customSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_fragment);



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChangePasswordActivity.this);
        etNewPassword=(EditText)findViewById(R.id.eT_newPassword);
        etOldPassword=(EditText)findViewById(R.id.eT_old_password) ;
        bTChangePassword=(Button)findViewById(R.id.bT_change_password);

        loadingPB =(ProgressBar) findViewById(R.id.idLoadingPB);
         iVShowHideOldPassword=(ImageView)findViewById(R.id.iV_showPassword);
         iVShowHidNewPassword=(ImageView)findViewById(R.id.iV_confirmPassword);
         iVBack=(RelativeLayout) findViewById(R.id.iv_back);


         iVBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.e("clicked","not working");
                 Intent redirectToDashBoard=new Intent(ChangePasswordActivity.this, DashboardActivity.class);
                 startActivity(redirectToDashBoard);
                 finish();
             }
         });

         iVShowHideOldPassword.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(etOldPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

                     iVShowHideOldPassword.setImageResource(R.drawable.ic_show);

                     //Show Password
                     etOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                 }
                 else{
                     iVShowHideOldPassword.setImageResource(R.drawable.ic_hide);

                     //Hide Password
                     etOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                 }
             }
         });


        iVShowHidNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNewPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

                    iVShowHidNewPassword.setImageResource(R.drawable.ic_show);

                    //Show Password
                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    iVShowHidNewPassword.setImageResource(R.drawable.ic_hide);

                    //Hide Password
                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });



        bTChangePassword.setOnClickListener(v -> {
            if(ApiConstants.isInternetConnected(ChangePasswordActivity.this)) {
                if(changePasswordValidation()) {
                    changePassword(etOldPassword.getText().toString(), etNewPassword.getText().toString());
                }
            }
            else
            {
                ApiConstants.showToast(ChangePasswordActivity.this,ApiConstants.noNetworkAvailable);
            }
        });
        // if(favouriteLocationsItemAdapter!=null) {

        // }

    }






    private boolean changePasswordValidation() {
        try {

            if (etOldPassword.getText().toString().isEmpty()) {
                etOldPassword.requestFocus();
                etOldPassword.setError("Please Enter Your Current Password");
                return false;
            }

            else if (etNewPassword.getText().toString().isEmpty()) {
                etNewPassword.requestFocus();
                etNewPassword.setError("Please Enter Your New Password");
                return false;
            } else {
                etNewPassword.setError(null);
                etOldPassword.setError(null);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }












    public  void changePassword(String strOldPassword,String strNewPassword) {
        customSharedPreferences=new CustomSharedPreferences(ChangePasswordActivity.this);

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

        ChangePasswordPoJo changePasswordPoJo=new ChangePasswordPoJo(strOldPassword,strNewPassword);


        // calling a method to create a post and passing our modal class.
        Call<ChangePasswordPoJo> call = retrofitAPI.changePasswordPost("Bearer " + customSharedPreferences.getStringValue("token") ,
                changePasswordPoJo );

//
        // on below line we are executing our method.
        call.enqueue(new Callback<ChangePasswordPoJo>() {
            @Override
            public void onResponse(@NotNull Call<ChangePasswordPoJo> call, @NotNull Response<ChangePasswordPoJo> response) {
                // this method is called when we get response from our api.
                // Toast.makeText(RegistrationController.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);
                Log.e("REsponse code" , String.valueOf(response.code()));

                ApiConstants.showToast(ChangePasswordActivity.this,"Password updated!!");
                Intent logOutIntent = new Intent(ChangePasswordActivity.this, LogInActivity.class);
                logOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                logOutIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                customSharedPreferences.setIsLogin(false);
                startActivity(logOutIntent);

                if(response.code()==200) {


                }
                else
                {
                    ApiConstants.showToast(ChangePasswordActivity.this,response.message());

                }




            }

            @Override
            public void onFailure(@NotNull Call<ChangePasswordPoJo> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.getMessage());
                Log.e("REsponse code" , String.valueOf(t.getMessage()));

                ApiConstants.showToast(ChangePasswordActivity.this,t.getMessage());

                loadingPB.setVisibility(View.GONE);
            }
        });
    }
}


