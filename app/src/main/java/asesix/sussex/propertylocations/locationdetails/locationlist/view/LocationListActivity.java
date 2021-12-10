package asesix.sussex.propertylocations.locationdetails.locationlist.view;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import asesix.sussex.R;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.dashboard.DashboardActivity;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavLocationResponsePoJo;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.averagePriceDates;
import asesix.sussex.user.passwordupdate.view.ChangePasswordActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationListActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favouritelocation_list_activity);


        recyclerView = (RecyclerView)findViewById(R.id.rV_favourite_location_list);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LocationListActivity.this);
        tvNoData=(TextView)findViewById(R.id.tv_noDataAvailable);
        loadingPB =(ProgressBar) findViewById(R.id.idLoadingPB);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        favouriteLocationsItemAdapter = new FavouriteLocationsItemAdapter(favouriteLocationsList, LocationListActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(LocationListActivity.this));
        favouriteLocationsList = new ArrayList<>();
        tvNoData.setVisibility(View.GONE);

        iVBack=(RelativeLayout) findViewById(R.id.iv_back);


        iVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("clicked","not working");
                Intent redirectToDashBoard=new Intent(LocationListActivity.this, DashboardActivity.class);
                startActivity(redirectToDashBoard);
                finish();
            }
        });

        if(favouriteLocationsList.size()>0) {

            favouriteLocationsList.remove(favouriteLocationsList.size() - 1);
            favouriteLocationsItemAdapter.notifyDataSetChanged();

            Toast.makeText(LocationListActivity.this, String.valueOf(favouriteLocationsList.size()), Toast.LENGTH_LONG).show();
        }




        if(ApiConstants.isInternetConnected(LocationListActivity.this)) {
            getLocationList();
        }
        else
        {
            ApiConstants.showToast(LocationListActivity.this,ApiConstants.noNetworkAvailable);
        }
        // if(favouriteLocationsItemAdapter!=null) {

        // }

    }







    public  void getLocationList() {
        customSharedPreferences=new CustomSharedPreferences(LocationListActivity.this);

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


        // calling a method to create a post and passing our modal class.
        Call<List<FavLocationResponsePoJo>> call = retrofitAPI.getUserFavourLocations("Bearer " + customSharedPreferences.getStringValue("token") ,
                customSharedPreferences.getStringValue("userId"));

//
        // on below line we are executing our method.
        call.enqueue(new Callback<List<FavLocationResponsePoJo>>() {
            @Override
            public void onResponse(@NotNull Call<List<FavLocationResponsePoJo>> call, @NotNull Response<List<FavLocationResponsePoJo>> response) {
                // this method is called when we get response from our api.
                // Toast.makeText(RegistrationController.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                if(response.code()==200) {


                    for (int i = 0; i < response.body().size(); i++) {
                        Log.e("resp" , String.valueOf(response.body().get(i).getPostcode()));


                        ArrayList<averagePriceDates>averagePriceDates=new ArrayList<>();
                        averagePriceDates=response.body().get(i).getAveragePriceDates();

                        FavLocationResponsePoJo favouriteLocationPOJO = new FavLocationResponsePoJo(response.body().get(i).getId(),

                                response.body().get(i).getLocation_name(), response.body().get(i).getPostcode(),response.body().get(i).getUser(),

                               averagePriceDates);

//

                        favouriteLocationsList.add(favouriteLocationPOJO);
                        Log.e("resp" , String.valueOf(response.body().get(i).getId()));


                    }

                    if(favouriteLocationsList.size()==0)
                    {

                        Log.e("inside" ,"size 0");
                        tvNoData.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        tvNoData.setVisibility(View.GONE);

                    }

                    favouriteLocationsItemAdapter = new FavouriteLocationsItemAdapter(
                            favouriteLocationsList, LocationListActivity.this);
                    favouriteLocationsItemAdapter.notifyDataSetChanged();


                    favouriteLocationsItemAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(favouriteLocationsItemAdapter);
                }
                else
                {
                    ApiConstants.showToast(LocationListActivity.this,response.message());

                }




            }

            @Override
            public void onFailure(@NotNull Call<List<FavLocationResponsePoJo>> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.getMessage());
                ApiConstants.showToast(LocationListActivity.this,t.getMessage());
                Log.e("Fail" ,t.getMessage());

                loadingPB.setVisibility(View.GONE);
            }
        });
    }
    }

