package asesix.sussex.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import asesix.sussex.R;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.dashboard.DashboardActivity;
import asesix.sussex.userauthentication.login.view.LogInActivity;

public class SplashScreenActivity extends AppCompatActivity {
    CustomSharedPreferences customSharedPreferences;

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        customSharedPreferences=new CustomSharedPreferences(getApplicationContext());


        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(customSharedPreferences.isLogin()) {
                    Intent intent=new Intent(SplashScreenActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    SplashScreenActivity.this.finish();
                }
                else
                {
                    Intent intent=new Intent(SplashScreenActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },3000);

    }
}