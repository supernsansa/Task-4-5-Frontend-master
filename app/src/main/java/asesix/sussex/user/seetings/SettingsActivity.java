package asesix.sussex.user.seetings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import asesix.sussex.R;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavLocationResponsePoJo;
import asesix.sussex.propertylocations.locationdetails.locationlist.view.FavouriteLocationsItemAdapter;

public class SettingsActivity  extends AppCompatActivity {

        View view;
        RecyclerView recyclerView;
        FavouriteLocationsItemAdapter favouriteLocationsItemAdapter;
        List<FavLocationResponsePoJo> favouriteLocationsList;
        private DrawerLayout drawer;
        private FrameLayout frames;
        private TextView tvNoData;
        private ProgressBar loadingPB;
        private RelativeLayout iVBack;


        CustomSharedPreferences customSharedPreferences;
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_settings_activity);


        }
    }

