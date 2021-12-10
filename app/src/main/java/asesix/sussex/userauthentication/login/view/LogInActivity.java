package asesix.sussex.userauthentication.login.view;

import android.content.Context;
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

import org.jetbrains.annotations.NotNull;

import asesix.sussex.R;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.dashboard.DashboardActivity;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.userauthentication.login.model.LoginPoJo;
import asesix.sussex.userauthentication.registration.view.RegistrationActivity;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends AppCompatActivity  {


    EditText eTEmail,eTPassword;
    Button btLogin;
    TextView tvNewUser;
    private ProgressBar loadingPB;
    CustomSharedPreferences customSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Custom Toolbar");
        }
        customSharedPreferences=new CustomSharedPreferences(this);

        initView();

    }




    void initView()
    {


        eTEmail=(EditText)findViewById(R.id.eT_EmailId);
        eTPassword=(EditText)findViewById(R.id.eT_Password);
        btLogin=(Button)findViewById(R.id.bT_signIn);
        loadingPB = findViewById(R.id.idLoadingPB);
        tvNewUser=findViewById(R.id.tv_NewUser);

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationIntent=new Intent(LogInActivity.this, RegistrationActivity.class);
                startActivity(registrationIntent);
                finish();
            }
        });

        btLogin.setOnClickListener(v -> {


            if(loginValidation()) {
                if(ApiConstants.isInternetConnected(getApplicationContext())) {

                    postLoginData(loadingPB, eTEmail.getText().toString(),
                            eTPassword.getText().toString(), "", "");
                }
                else
                {

                    ApiConstants.showToast(LogInActivity.this,ApiConstants.noNetworkAvailable);

                }
            }


        });


    }




    public void ShowHidePass(View view){

        ImageView iVShowHidePassword=(ImageView)view.findViewById(R.id.iV_showPassword);
        if(view.getId()==R.id.iV_showPassword){
            if(eTPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

                iVShowHidePassword.setImageResource(R.drawable.ic_hide);

                //Show Password
                eTPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                iVShowHidePassword.setImageResource(R.drawable.ic_show);

                //Hide Password
                eTPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }




    }

    private boolean loginValidation() {
        try {
            String tempEmail = eTEmail.getText().toString().trim();
            String tempPassword= eTPassword.getText().toString().trim();

             if (tempEmail.isEmpty()) {
                eTEmail.requestFocus();
                eTEmail.setError("Please Enter Your Email Id");
                return false;

            }
            else if (ApiConstants.isValidEmail(tempEmail.toLowerCase())) {
                eTEmail.requestFocus();
                eTEmail.setError("Please Enter valid Email Id");
                return false;

            }


            else if (eTPassword.getText().toString().isEmpty()) {
                eTPassword.requestFocus();
                eTPassword.setError("Please Enter Your Password");
                return false;
            } else if (tempPassword.length() <= 3) {
                eTPassword.requestFocus();
                eTPassword.setError("Password should be more than 4 characters");
                return false;
            } else {
                eTPassword.setError(null);
                eTEmail.setError(null);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

























    public  void postLoginData(ProgressBar loadingPB, String strEmailId, String strPassword, String strToken, String strId) {

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);
        Log.e("response code" , "String.valueOf(response.code())");

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

        Log.e("response code" , String.valueOf(retrofit.baseUrl()));

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Log.e("response code" , String.valueOf(retrofit));

        // passing data from our text fields to our modal class.
        LoginPoJo loginPoJo = new LoginPoJo(strEmailId,strPassword,"","");

        // calling a method to create a post and passing our modal class.
        Call<LoginPoJo> call = retrofitAPI.createLogInPost(loginPoJo);
        Log.e("response code" , String.valueOf(call.request()));

        // on below line we are executing our method.
        call.enqueue(new Callback<LoginPoJo>() {
            @Override
            public void onResponse(Call<LoginPoJo> call, Response<LoginPoJo> response) {
                // this method is called when we get response from our api.
                // Toast.makeText(RegistrationController.this, "Data added to API", Toast.LENGTH_SHORT).show();
                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                Log.e("response code" , String.valueOf(response.code()));

                if(response.code()==200) {
                    customSharedPreferences.setIsLogin(true);
                    customSharedPreferences.setStringValue("email", response.body().getEmail());
                    customSharedPreferences.setStringValue("token", response.body().getToken());
                    Log.e("TOKEN" , customSharedPreferences.getStringValue("token"));

                    customSharedPreferences.setStringValue("userId",response.body().getUserID().trim());
                    Intent login = new Intent(LogInActivity.this, DashboardActivity.class);
                    startActivity(login);

                }
                else
                {
                    ApiConstants.showToast(LogInActivity.this,response.message());

                }


            }

            @Override
            public void onFailure(@NotNull Call<LoginPoJo> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.getMessage());
                ApiConstants.showToast(LogInActivity.this,ApiConstants.serverError);
                Log.e("fail code" , String.valueOf(t.getMessage()));

                loadingPB.setVisibility(View.GONE);

            }
        });
    }





}


