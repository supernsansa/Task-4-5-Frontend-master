package asesix.sussex.network;

import java.util.List;

import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavouriteLocationPOJO;
import asesix.sussex.propertylocations.locationdetails.savelocation.model.SaveLocationPoJo;
import asesix.sussex.userauthentication.login.model.LoginPoJo;
import asesix.sussex.userauthentication.registration.model.RegistrationPoJo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import retrofit2.http.Path;

public interface RetrofitAPI {

    // as we are making a post request to post a data
    // so we are annotating it with post
    // and along with that we are passing a parameter as users
    @POST(ApiConstants.Sign_Up)
    //on below line we are creating a method to post our data.
    Call<RegistrationPoJo> createRegistrationPost(@Body RegistrationPoJo registrationPoJo);

    @POST(ApiConstants.Sign_In)
        //on below line we are creating a method to post our data.
    Call<LoginPoJo> createLogInPost(@Body LoginPoJo loginPoJo);

    @POST(ApiConstants.Add_User_Location)
    Call<SaveLocationPoJo> addUserLocation(@Header("Authorization") String auth, @Body() SaveLocationPoJo saveLocationPoJo);




    @GET(ApiConstants.GET_User_LocationById)
    Call<List<FavouriteLocationPOJO>> getUserFavourLocations(@Header("Authorization") String auth, @Path("id") String id);

//
    @DELETE(ApiConstants.DELETE_User_LocationById)
    Call <ResponseBody> deleteUserLocationById(@Header("Authorization") String auth, @Path("id") String id);


}
