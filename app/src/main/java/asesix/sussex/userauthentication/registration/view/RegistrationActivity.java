package asesix.sussex.userauthentication.registration.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import asesix.sussex.R;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.userauthentication.login.view.LogInActivity;
import asesix.sussex.userauthentication.registration.model.RegistrationPoJo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {


    EditText eTFirstName,eTLastName,eTEmail,eTPassword,eTConfirmPassword;
    Button btRegister;
    private ProgressBar loadingPB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Custom Toolbar");
        }
        initView();

    }


    void initView()
    {

        eTFirstName=(EditText) findViewById(R.id.eT_FirstName);
        eTLastName=(EditText)findViewById(R.id.eT_LastName);
        eTEmail=(EditText)findViewById(R.id.eT_EmailId);
        eTPassword=(EditText)findViewById(R.id.eT_Password);
        eTConfirmPassword=(EditText)findViewById(R.id.eT_ConfirmPassword);
        btRegister=(Button)findViewById(R.id.bT_Register);
        btRegister.setTextColor(Color.parseColor("#FFFFFF"));
        loadingPB = findViewById(R.id.idLoadingPB);
        btRegister.setOnClickListener(v -> {

            // calling a method to post the data and passing our name and job.

            if (registrationValidation()) {
                if(ApiConstants.isInternetConnected(getApplicationContext())) {

                    postRegistrationData(loadingPB, eTFirstName.getText().toString(), eTLastName.getText().toString(), eTEmail.getText().toString(),
                            eTPassword.getText().toString(), "");
                }
                else
                {
                    ApiConstants.showToast(RegistrationActivity.this,ApiConstants.noNetworkAvailable);

                }

            }

        });


    }
    @Override
    public void onClick(View v) {

      //  case v.and

    }


    public void ShowHidePass(View view){

        ImageView iVShowHidePassword=(ImageView)view.findViewById(R.id.iV_showPassword);
        ImageView iVShowHideConfirmPassword=(ImageView)view.findViewById(R.id.iV_confirmPassword);

        if(view.getId()==R.id.iV_showPassword){
            if(eTPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

                iVShowHidePassword.setImageResource(R.drawable.ic_show);

                //Show Password
                eTPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                iVShowHidePassword.setImageResource(R.drawable.ic_hide);

                //Hide Password
                eTPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }



        if(view.getId()==R.id.iV_confirmPassword){

            if(eTConfirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

                iVShowHideConfirmPassword.setImageResource(R.drawable.ic_show);

                //Show Password
                eTConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                iVShowHideConfirmPassword.setImageResource(R.drawable.ic_hide);

                //Hide Password
                eTConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }




    public void postRegistrationData(ProgressBar loadingPB, String strFirstName, String strLastName, String strEmailId, String strPassword, String strId) {

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

        // passing data from our text fields to our modal class.
        RegistrationPoJo registrationPoJo = new RegistrationPoJo(strFirstName, strLastName,strEmailId,strPassword,"");

        // calling a method to create a post and passing our modal class.
        Call<RegistrationPoJo> call = retrofitAPI.createRegistrationPost(registrationPoJo);

        // on below line we are executing our method.
        call.enqueue(new Callback<RegistrationPoJo>() {
            @Override
            public void onResponse(Call<RegistrationPoJo> call, Response<RegistrationPoJo> response) {
                // this method is called when we get response from our api.
                // Toast.makeText(RegistrationController.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                if(response.code()==200)
                {
                    Intent startLoginActivity=new Intent(RegistrationActivity.this, LogInActivity.class);
                    startActivity(startLoginActivity);
                    finish();;
                }
                else
                {
                    ApiConstants.showToast(RegistrationActivity.this,ApiConstants.serverError);
                }

            }

            @Override
            public void onFailure(@NotNull Call<RegistrationPoJo> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.getMessage());
                ApiConstants.showToast(RegistrationActivity.this,ApiConstants.serverError);

                loadingPB.setVisibility(View.GONE);
            }
        });
    }













    private boolean registrationValidation() {
        try {
            String tempEmail = eTEmail.getText().toString().trim();
            String tempUsername = eTFirstName.getText().toString().trim();
            String tempPassword= eTPassword.getText().toString().trim();
            String tempConfirmPassword = eTConfirmPassword.getText().toString().trim();
            if (tempUsername.isEmpty()) {
                eTFirstName.requestFocus();
                eTFirstName.setError("Please Enter Your Name");
                return false;
            }

            else if (tempEmail.isEmpty()) {
                eTEmail.requestFocus();
                eTEmail.setError("Please Enter Your Email Id");
                return false;

            }
            else if (ApiConstants.isValidEmail(tempEmail.toLowerCase())) {
                eTEmail.requestFocus();
                eTEmail.setError("Please Enter valid Email Id");
                return false;

            }else if (eTPassword.getText().toString().isEmpty()) {
                eTPassword.requestFocus();
                eTPassword.setError("Please Enter Your Password");
                return false;
            } else if (tempPassword.length() <= 3) {
                eTPassword.requestFocus();
                eTPassword.setError("Password should be more than 4 characters");
                return false;
            } else if (tempConfirmPassword.isEmpty()) {
                eTConfirmPassword.requestFocus();
                eTConfirmPassword.setError("Please Reenter Your Password");
                return false;
            } else if (!tempPassword.equals(tempConfirmPassword)) {
                eTConfirmPassword.requestFocus();
                eTConfirmPassword.setError("Password Does not match");
                return false;
            } else {
                eTFirstName.setError(null);
                eTPassword.setError(null);
                eTConfirmPassword.setError(null);
                eTEmail.setError(null);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }




}


