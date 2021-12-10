package asesix.sussex.propertyheatmap;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import asesix.sussex.R;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.dashboard.DashboardActivity;
import asesix.sussex.databinding.ActivityMapsBinding;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavLocationResponsePoJo;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavouriteLocationPOJO;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.averagePriceDates;
import asesix.sussex.propertylocations.locationdetails.locationlist.view.LocationListActivity;
import asesix.sussex.user.passwordupdate.view.ChangePasswordActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    CustomSharedPreferences customSharedPreferences;
    private GoogleMap mMap;
    Fragment fragment ;

    private ActivityMapsBinding binding;
    private HeatmapTileProvider mProvider;
    private LatLng london;
    private OutcodeDBAccess outcodeDBAccess;
    private PostcodeDBAccess postcodeDBAccess;
    private FloatingActionButton fbSave;
    private TextView crimeReport;
    //Coordinates of viewable area
    private double northeast_lat;
    private double northeast_long;
    private double southwest_lat;
    private double southwest_long;
    //If first launch
    private boolean first_launch = true;
    //Heatmap variables
    private TileOverlay heatmap;
    private int[] colors = {
            Color.rgb(0, 255, 248),
            Color.rgb(56, 235, 169),
            Color.rgb(123, 207, 79),
            Color.rgb(175, 172, 0),
            Color.rgb(221, 122, 0),
            Color.rgb(255, 0, 0)
    };
    private float[] startPoints = {
            0.0f, 0.1f, 0.2f, 0.4f, 0.6f, 1f
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        crimeReport =(TextView)findViewById(R.id.crimeReport);
        outcodeDBAccess = OutcodeDBAccess.getInstance((getApplicationContext()));
        postcodeDBAccess = PostcodeDBAccess.getInstance(getApplicationContext());
        customSharedPreferences=new CustomSharedPreferences(MapsActivity.this);
        fbSave=(FloatingActionButton)findViewById(R.id.fB_save_location);
        fbSave.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent showMapintent=new Intent(MapsActivity.this, DashboardActivity.class);
                startActivity(showMapintent);
            }

        });


        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(6000, TimeUnit.SECONDS)
                .readTimeout(6000, TimeUnit.SECONDS)
                . writeTimeout(6000, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Move camera to london
        london = new LatLng(51.5072, 0.1276);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(london,12.0f));

        //Enable user location tracking
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.err.println("Location services not enabled");
        }
        else {
            mMap.setMyLocationEnabled(true);
        }


        if(getIntent().getExtras() !=null) {
           String locationId = getIntent().getStringExtra("id");
            Log.e("LOCation Id" ,locationId);
            getLocationDetails(locationId);




        }


        //When the camera moves, find the visible area and populate with markers/heatmap datapoints
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                System.out.println(cameraPosition.zoom);
                if(cameraPosition.zoom > 16.0) {
                    System.err.println("Change to full postcodes here");
                    heatmapGen(false);
                } else {
                    System.err.println("Use outcodes here");
                    crimeReport.setVisibility(View.INVISIBLE);
                    heatmapGen(true);
                }

                System.out.println(String.valueOf(northeast_lat) + " " + String.valueOf(northeast_long));
            }
        });

        //Get coordinates of tapped point on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                System.out.println("Map clicked [" + point.latitude + " / " + point.longitude + "]");
                CameraPosition cameraPosition = mMap.getCameraPosition();
                if(cameraPosition.zoom > 16) {
                    getLocationRequest(point.latitude,point.longitude,false);
                }
                else {
                    getLocationRequest(point.latitude, point.longitude,true);
                }
            }
        });
    }

    //Fetches viewable area and sets coordinates
    public void getViewableArea() {
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        LatLng northeast = bounds.northeast;
        LatLng southwest = bounds.southwest;
        //Get individual lats and longs and set accordingly
        northeast_lat = northeast.latitude;
        northeast_long = northeast.longitude;
        southwest_lat = southwest.latitude;
        southwest_long = southwest.longitude;
    }

    //Takes boolean parameter. true if outcodes, false if postcodes
    public void heatmapGen(boolean mode) {

        if(first_launch == false) {
            heatmap.remove();
        }
        else {
            first_launch = false;
        }

        //Get all codes in viewable area
        getViewableArea();

        //Fetch outcode or postcode data
        ArrayList<Postcode> codes = new ArrayList<Postcode>();
        //If we're looking for outcodes
        if (mode == true) {
            outcodeDBAccess.open();
            codes = outcodeDBAccess.getVisibleOutcodes(northeast_lat,northeast_long,southwest_lat,southwest_long);
        }
        //If we're looking for postcodes
        else {
            getCrimes();
            postcodeDBAccess.open();
            codes = postcodeDBAccess.getVisiblePostcodes(northeast_lat,northeast_long,southwest_lat,southwest_long);
        }

        final ArrayList<Postcode> finalCodes = codes;

        if(codes.size() > 0) {
            getPostcodeRequest(codes);
        }
        else {
            System.out.println("No visible datapoints");
        }

    }

    //This method retrieves sales prices and num properties values for a given list of postcodes/outcodes
    public void getPostcodeRequest(ArrayList<Postcode> postcodes) {

        ArrayList<String> codeStrings = new ArrayList<String>();
        ArrayList<WeightedLatLng> codeWeightedLatLngs = new ArrayList<WeightedLatLng>();
        codeWeightedLatLngs.add(new WeightedLatLng(new LatLng(0.0, 0.0),0.0));

        //Get arraylist of postcodes strings
        for (Postcode p : postcodes) {
            codeStrings.add(p.getCode());
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("outcodes",new JSONArray(codeStrings));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Send request for prices
        AndroidNetworking.post(ApiConstants.BASE_URL + "/property")
                .addJSONObjectBody(requestBody)
                .setPriority(Priority.HIGH)

                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //Read JSON response and set variables accordingly
                            for(int index = 0; index < response.length(); index++) {
                                //Get values
                                JSONObject obj = response.getJSONObject(index);
                                System.out.println(obj.getString("postcode"));
                                Double price = obj.getDouble("averageValue");
                                System.out.println(String.valueOf(price));
                                int numProperties = obj.getInt("numberOfProperties");
                                System.out.println(String.valueOf(numProperties));
                                //Set values for relevant postcode
                                postcodes.get(index).setPrice(price);
                                Log.e("Price" , String.valueOf(numProperties));

                                postcodes.get(index).setNumProperties(numProperties);
                                LatLng coords = new LatLng(postcodes.get(index).getLatitude(),postcodes.get(index).getLongitude());
                                codeWeightedLatLngs.add(new WeightedLatLng(coords,price));
                            }
                            //Set heatmap layer
                            mProvider = new HeatmapTileProvider.Builder().weightedData( codeWeightedLatLngs ).build();
                            mProvider.setRadius( 50 );
                            mProvider.setGradient(new Gradient(colors, startPoints));
                            heatmap = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.printStackTrace();
                        System.err.println(error.getErrorCode());
                        System.err.println(error.getErrorBody());
                        System.err.println(error.getErrorDetail());
                    }
                });
    }

    //This method retrieves the sales price and number of properties for a given location and displays it in an alert dialog
    public void getIndivDataRequest(String code) {
        //Send request for prices
        AndroidNetworking.get((ApiConstants.BASE_URL + "/property/" + code))
                .setPriority(Priority.HIGH)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        ProgressBar progressBar = new ProgressBar(MapsActivity.this, null, android.R.attr.progressBarStyleSmall);

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String price = "Average Value: " + response.getString("averageValue");
                            String avgPrice= (response.getString("averageValue"));
                            System.out.println(price);
                            String numberOfProperties = "Number of Properties: " + String.valueOf(response.getInt("numberOfProperties"));
                            System.out.println(numberOfProperties);
                            //Create alert dialog with postcode/outcode info
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                            builder.setMessage(price + "\n" + numberOfProperties)
                                    .setTitle(code)
                                     .setNegativeButton("Save this property",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    saveLocation(customSharedPreferences.getStringValue("userId"),"",code,
                                            avgPrice);


                                }
                            });


                            AlertDialog dialog = builder.create();
                            dialog.show();







                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.printStackTrace();
                        System.err.println(error.getErrorCode());
                        System.err.println(error.getErrorBody());
                        System.err.println(error.getErrorDetail());
                    }
                });
    }

    //This method consults postcodes.io to get a postcode/outcode for a given set of coordinates
    public void getLocationRequest(double lat, double lon, boolean mode) {
        //Send request for prices
        AndroidNetworking.get("https://api.postcodes.io/postcodes?lon=" + String.valueOf(lon) +
                "&lat=" + String.valueOf(lat))
                .setPriority(Priority.HIGH)

                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = "Invalid Location";
                        try {
                            JSONArray result = response.getJSONArray("result");
                            JSONObject codeObject = result.getJSONObject(0);
                            //Get outcode
                            if(mode == true) {
                                code = codeObject.getString("outcode");
                            }
                            //Get postcode
                            else {
                                code = codeObject.getString("postcode");
                            }
                            getIndivDataRequest(code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.printStackTrace();
                        System.err.println(error.getErrorCode());
                        System.err.println(error.getErrorBody());
                        System.err.println(error.getErrorDetail());
                    }
                });
    }

    //This method gets the crimes committed for a set of coordinates (Street-Level Only)
    public void getCrimes() {

        String[] bounds = new String[4];
        bounds[0] = String.valueOf(northeast_lat) + "," + String.valueOf(northeast_long);
        bounds[1] = String.valueOf(northeast_lat) + "," + String.valueOf(southwest_long);
        bounds[2] = String.valueOf(southwest_lat) + "," + String.valueOf(southwest_long);
        bounds[3] = String.valueOf(southwest_lat) + "," + String.valueOf(northeast_long);

        String poly = bounds[0] + ":" + bounds[1] + ":" + bounds[2] + ":" + bounds[3];

        //Send request for crimes
        AndroidNetworking.get("https://data.police.uk/api/crimes-street/all-crime?poly=" + poly)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Read JSON response and set variable accordingly
                        String numberOfCrimes = String.valueOf(response.length());
                        System.out.println("Crimes Reported: " + numberOfCrimes);
                        crimeReport.setText("Crimes Reported: " + numberOfCrimes);
                        crimeReport.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.printStackTrace();
                        System.err.println(error.getErrorCode());
                        System.err.println(error.getErrorBody());
                        System.err.println(error.getErrorDetail());
                    }
                });
    }







  public  void getLocationDetails(String locationId) {


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
        Call<FavLocationResponsePoJo> call = retrofitAPI.getLocationById("Bearer " + customSharedPreferences.getStringValue("token") ,locationId);

        // on below line we are executing our method.
        call.enqueue(new Callback<FavLocationResponsePoJo> () {
            @Override
            public void onResponse(@NotNull Call<FavLocationResponsePoJo> call, @NotNull Response<FavLocationResponsePoJo>response) {

                Log.e("response code" , String.valueOf(response.code()));

                if(response.code()==200) {


                    for (int i = 0; i < response.body().getAveragePriceDates().size(); i++) {

                        ArrayList<averagePriceDates>averagePriceDates=new ArrayList<>();

                        FavLocationResponsePoJo favouriteLocationPOJO = new FavLocationResponsePoJo(response.body().getId(),

                                response.body().getLocation_name(), response.body().getPostcode(),response.body().getUser(),

                                averagePriceDates);



                        List<averagePriceDates>priceDates=new ArrayList<>();
                String price = "Average Value: " + response.body().getAveragePriceDates().get(i).getAveragePrice();

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage(price )
                        .setTitle(response.body().getPostcode())
                        .setNegativeButton("Update this location",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                upDateLocation(locationId,customSharedPreferences.getStringValue("userId"),
                                       "", response.body().getPostcode(),price);



                            }
                        });


                AlertDialog dialog = builder.create();
                dialog.show();
//              Address address = addresses.get(0);
////                    // Use the address as needed
////                    String coordinates = String.valueOf(address.getLatitude()) + " " + String.valueOf(address.getLongitude());
////



              //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(),address.getLongitude()),12.0f));


//            } catch (IOException e) {
//                // handle exception
//            }



//

                        Log.e("resp" , String.valueOf(response.body().getPostcode()));


                    }


                }
//
            }

            @Override
            public void onFailure(@NotNull Call<FavLocationResponsePoJo>call, Throwable t) {

                Log.e("Fail" ,""+ t.getMessage().toString());

            }
        });
    }




    public  void upDateLocation(String locationId,String userId, String locationName, String postCode, String avgPrice) {

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

        FavouriteLocationPOJO favLocationResponsePoJo=new FavouriteLocationPOJO(userId,locationName, postCode,avgPrice);


        // calling a method to create a post and passing our modal class.
        Call<FavouriteLocationPOJO> call = retrofitAPI.updateLocationById("Bearer " + customSharedPreferences.getStringValue("token"),locationId,favLocationResponsePoJo);
        Log.e("Insdie" , String.valueOf(call.request()));

        // on below line we are executing our method.
        call.enqueue(new Callback<FavouriteLocationPOJO>() {
            @Override
            public void onResponse(@NotNull Call<FavouriteLocationPOJO> call, @NotNull Response<FavouriteLocationPOJO> response) {
                Log.e("Insdie  update" , String.valueOf(response.code()));



                if(response.code()==200) {

                    Log.e("response code map" , String.valueOf(response.code()));

                    Intent showMapintent=new Intent(MapsActivity.this, LocationListActivity.class);
                    startActivity(showMapintent);
                    finish();

                }
//
            }

            @Override
            public void onFailure(@NotNull Call<FavouriteLocationPOJO> call, Throwable t) {

                Log.e("Fail" ,""+ t.getMessage().toString());

            }
        });
    }






    public  void saveLocation(String userId, String locationName, String postCode, String avgPrice) {

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
        FavouriteLocationPOJO saveLocationPoJo = new FavouriteLocationPOJO(userId,locationName,postCode,avgPrice);

        // calling a method to create a post and passing our modal class.
        Call<FavouriteLocationPOJO> call = retrofitAPI.addUserLocation("Bearer " + customSharedPreferences.getStringValue("token") ,saveLocationPoJo);
        Log.e("Insdie" , String.valueOf(call.request()));

        // on below line we are executing our method.
        call.enqueue(new Callback<FavouriteLocationPOJO>() {
            @Override
            public void onResponse(@NotNull Call<FavouriteLocationPOJO> call, @NotNull Response<FavouriteLocationPOJO> response) {
                Log.e("Insdie" , String.valueOf(response.code()));


                if(response.code()==200) {

                    Log.e("response code map" , String.valueOf(response.code()));

                    Intent showMapintent=new Intent(MapsActivity.this, LocationListActivity.class);
                    startActivity(showMapintent);
                    finish();


                }
//
            }

            @Override
            public void onFailure(@NotNull Call<FavouriteLocationPOJO> call, Throwable t) {

                Log.e("Fail" ,""+ t.getMessage().toString());

            }
        });
    }







}