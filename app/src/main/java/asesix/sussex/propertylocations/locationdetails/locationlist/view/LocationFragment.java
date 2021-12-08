package asesix.sussex.propertylocations.locationdetails.locationlist.view;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import asesix.sussex.R;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavouriteLocationPOJO;
import asesix.sussex.userauthentication.login.view.LogInActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationFragment  extends Fragment {
    View view;
    RecyclerView recyclerView;
    FavouriteLocationsItemAdapter favouriteLocationsItemAdapter;
    List<FavouriteLocationPOJO> favouriteLocationsList;
    private DrawerLayout drawer;
    private FrameLayout frames;
    private TextView tvNoData;
    private ProgressBar loadingPB;


    CustomSharedPreferences customSharedPreferences;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favouritelocation_list_activity, container, false);


        recyclerView = (RecyclerView)view.findViewById(R.id.rV_favourite_location_list);
      if(ApiConstants.isInternetConnected(getContext())) {
          getLocationList();
      }
      else
      {
          ApiConstants.showToast(getContext(),ApiConstants.noNetworkAvailable);
      }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        tvNoData=(TextView)view.findViewById(R.id.tv_noDataAvailable);
        loadingPB = view.findViewById(R.id.idLoadingPB);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        favouriteLocationsItemAdapter = new FavouriteLocationsItemAdapter(favouriteLocationsList,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(favouriteLocationsList.size()>0) {
            favouriteLocationsList.remove(favouriteLocationsList.size() - 1);
            favouriteLocationsItemAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), String.valueOf(favouriteLocationsList.size()), Toast.LENGTH_LONG).show();
        }
        // if(favouriteLocationsItemAdapter!=null) {

        // }
        return view;

    }







    public  void getLocationList() {
        favouriteLocationsList = new ArrayList<>();
        customSharedPreferences=new CustomSharedPreferences(getContext());

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
        Call<List<FavouriteLocationPOJO>> call = retrofitAPI.getUserFavourLocations("Bearer " + customSharedPreferences.getStringValue("token") ,
                customSharedPreferences.getStringValue("userId"));

//
        // on below line we are executing our method.
        call.enqueue(new Callback<List<FavouriteLocationPOJO>>() {
            @Override
            public void onResponse(@NotNull Call<List<FavouriteLocationPOJO>> call, @NotNull Response<List<FavouriteLocationPOJO>> response) {
                // this method is called when we get response from our api.
                // Toast.makeText(RegistrationController.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                if(response.code()==200) {

                    if(response.body()==null)
                    {
                        tvNoData.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < Objects.requireNonNull(response.body()).size(); i++) {
                        FavouriteLocationPOJO favouriteLocationPOJO = new FavouriteLocationPOJO(response.body().get(i).getId(),

                                response.body().get(i).getLocation_name(), response.body().get(i).getLatitude(), response.body().get(i).getLongitude());


                        favouriteLocationsList.add(favouriteLocationPOJO);

                    }


                    favouriteLocationsItemAdapter = new FavouriteLocationsItemAdapter(
                            favouriteLocationsList, getContext());
                    favouriteLocationsItemAdapter.notifyDataSetChanged();


                    favouriteLocationsItemAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(favouriteLocationsItemAdapter);
                }
                else
                {
                    ApiConstants.showToast(getContext(),ApiConstants.serverError);

                }




            }

            @Override
            public void onFailure(@NotNull Call<List<FavouriteLocationPOJO>> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.getMessage());
                ApiConstants.showToast(getActivity(),ApiConstants.serverError);

                loadingPB.setVisibility(View.GONE);
            }
        });
    }
    }

