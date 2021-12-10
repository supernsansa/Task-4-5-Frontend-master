package asesix.sussex.network;

import java.util.List;

import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavLocationResponsePoJo;
import asesix.sussex.propertylocations.locationdetails.locationlist.model.FavouriteLocationPOJO;
import asesix.sussex.user.passwordupdate.model.ChangePasswordPoJo;
import asesix.sussex.userauthentication.login.model.LoginPoJo;
import asesix.sussex.userauthentication.registration.model.RegistrationPoJo;
import asesix.sussex.user.profile.model.GetUserProfilePoJo;
import asesix.sussex.user.profile.model.UpdateProfilePoJo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import asesix.sussex.common.util.AppConstants.ApiConstants;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {

    //
    @POST(ApiConstants.Sign_Up)
    //on below line we are creating a method to post our data.
    Call<RegistrationPoJo> createRegistrationPost(@Body RegistrationPoJo registrationPoJo);

    @POST(ApiConstants.Sign_In)
        //on below line we are creating a method to post our data.
    Call<LoginPoJo> createLogInPost(@Body LoginPoJo loginPoJo);

    @GET(ApiConstants.GET_USER)
    Call <GetUserProfilePoJo> getUserDetails(@Header("Authorization") String auth, @Query("id") String id);

    @PUT(ApiConstants.UPDATE_PROFILE)
        //on below line we are creating a method to post our data.
    Call<UpdateProfilePoJo> upDateProfilePost(@Header("Authorization")String auth,@Body UpdateProfilePoJo updateProfilePoJo);
    @PUT(ApiConstants.CHANGE_PASSWORD)
        //on below line we are creating a method to post our data.
    Call<ChangePasswordPoJo> changePasswordPost(@Header("Authorization")String auth,@Body ChangePasswordPoJo changePasswordPoJo);



    @POST(ApiConstants.Add_User_Location)
    Call<FavouriteLocationPOJO> addUserLocation(@Header("Authorization") String auth, @Body() FavouriteLocationPOJO saveLocationPoJo);




    @GET(ApiConstants.GET_User_LocationById)
    Call<List<FavLocationResponsePoJo>> getUserFavourLocations(@Header("Authorization") String auth, @Path("id") String id);


    @GET(ApiConstants.GET_LocationById)
    Call<FavLocationResponsePoJo> getLocationById(@Header("Authorization") String auth, @Path("id") String id);


    @DELETE(ApiConstants.DELETE_User_LocationById)
    Call <ResponseBody> deleteUserLocationById(@Header("Authorization") String auth, @Path("id") String id);

    @PUT(ApiConstants.UPDATE_LocationById)
        //on below line we are creating a method to post our data.
    Call<FavouriteLocationPOJO> updateLocationById(@Header("Authorization")String auth,@Path("id") String id,@Body() FavouriteLocationPOJO favLocationResponsePoJo);


}
