package asesix.sussex.propertylocations.locationdetails.locationlist.view;

import java.util.List;

import asesix.sussex.R;
import asesix.sussex.common.util.sharedprefernce.CustomSharedPreferences;
import asesix.sussex.network.RetrofitAPI;
import asesix.sussex.propertylocations.MapsActivity;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavouriteLocationPOJO;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class FavouriteLocationsItemAdapter extends RecyclerView.Adapter<FavouriteLocationsItemAdapter.ViewHolder>{
    private List<FavouriteLocationPOJO> favouriteLocationPOJOList;
    ClickListener listener;
    CustomSharedPreferences customSharedPreferences;
    Context mContext;

    // RecyclerView recyclerView;
    public FavouriteLocationsItemAdapter(List<FavouriteLocationPOJO> favouriteLocationPOJOList,Context mContext) {
        this.favouriteLocationPOJOList = favouriteLocationPOJOList;
        this.mContext=mContext;


    }
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        Log.e("onCreateViewHolder", "OK");

        View listItem= layoutInflater.inflate(R.layout.favouritelocations_row_adapter, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FavouriteLocationPOJO favouriteLocationPOJO = favouriteLocationPOJOList.get(position);
       // Log.e("DTAAA", myListData.getLocation_name());
       // notifyItemRemoved( position);

        holder.tV_LocationName.setText(String.valueOf(favouriteLocationPOJO.getLongitude()));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MapsActivity.class);
            Log.e("clicked","clike not moving");
            intent.putExtra("lat", String.valueOf(favouriteLocationPOJO.getLatitude()));
            intent.putExtra("long", String.valueOf(favouriteLocationPOJO.getLongitude()));
            v.getContext().startActivity(intent);
        });



        holder.fB_DeleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // listener.onClick(v,favouriteLocationPOJO,position);
                Log.e("favouriteLocationPOJO", favouriteLocationPOJO.getId());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = ((Activity) v.getContext()).getLayoutInflater();
                View alertView = inflater.inflate(R.layout.delete_data_dialog, null);
                alertDialog.setView(alertView);

                // notifyItemRemoved( position);
                notifyDataSetChanged();
                final AlertDialog show = alertDialog.show();

                Button alertButton = (Button) alertView.findViewById(R.id.bt_delete_location);
                alertButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ApiConstants.isInternetConnected(mContext)) {
                            postData(favouriteLocationPOJO.getId(), position);
                            updateData(position, favouriteLocationPOJOList);
                            // notifyItemRemoved( position);
                            notifyDataSetChanged();
                        }
                        else
                        {
                            ApiConstants.showToast(mContext,ApiConstants.noNetworkAvailable);
                        }

                        //notify();
                        show.dismiss();
                    }
                });

            }
        });

    }
    public void updateData(int position, List<FavouriteLocationPOJO> updateFavouriteLocationList) {
        this.favouriteLocationPOJOList = updateFavouriteLocationList;
        notifyItemRemoved( position);
    }

    @Override
    public int getItemCount() {
        Log.e("getItemCount", String.valueOf(favouriteLocationPOJOList.size()));

        return favouriteLocationPOJOList.size();
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.listener = clickListener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tV_LocationName;
        public FloatingActionButton fB_DeleteLocation;
        public CardView mVLocationDetailsCard;

        public ViewHolder(View itemView) {

            super(itemView);
            Log.e("itemView", "OK");

            this.tV_LocationName = (TextView) itemView.findViewById(R.id.tV_locationName);
            this.fB_DeleteLocation=(FloatingActionButton)itemView.findViewById(R.id.fB_delete_location);
            this.mVLocationDetailsCard=(CardView)itemView.findViewById(R.id.cV_locationDetailsCard);
        }
    }


    public  void postData(String locationId,int position) {

        customSharedPreferences=new CustomSharedPreferences(mContext);

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

        // calling a method to create a post and passing our modal class.
        Call<ResponseBody> call = retrofitAPI.deleteUserLocationById("Bearer " + customSharedPreferences.getStringValue("token"),locationId);

        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {




                if(favouriteLocationPOJOList.size()>0) {
                    favouriteLocationPOJOList.remove(favouriteLocationPOJOList.remove(position));
                    notifyDataSetChanged();
                }

                if(response.code()!=200)
                {
                    ApiConstants.showToast(mContext,ApiConstants.serverError);
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.getMessage());
                Log.e("Fail" ,""+ t.getMessage().toString());

            }
        });
    }
}