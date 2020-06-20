package com.procter.retrofit;

import com.procter.model.CMSResponse;
import com.procter.model.WarchListResponse;
import com.procter.model.bid.BidResponseData;
import com.procter.model.earnings.EarningsResponse;
import com.procter.model.faq.FaqResponse;
import com.procter.model.order.DeliveryAssignResponse;
import com.procter.model.order.OrderDetailsResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by ptbr-1167 on 4/12/18.
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("api/pharmacyApi/login")
    Call<ResponseBody> signIn(@Field("mobile") String first,
                              @Field("password") String password, @Field("fcm_id")
                                      String fcm_id, @Field("device_type") String device_type);


    @FormUrlEncoded
    @POST("api/pharmacyApi/sendOtp")
    Call<ResponseBody> forgetPass(@Field("mobile") String mobile);


    @FormUrlEncoded
    @POST("api/pharmacyApi/verifyOtp")
    Call<ResponseBody> verifyOtp(@Field("mobile") String mobile, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("api/pharmacyApi/resetPassword")
    Call<ResponseBody> resetPassword(@Field("otp_id") String otp_id, @Field("password") String password, @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @POST("api/pharmacyApi/generateOtp")
    Call<ResponseBody> generateOtp(@Field("mobile") String mobile);


    @Multipart
    @POST("api/pharmacyApi/register")
    Call<ResponseBody> register(@Part("name") RequestBody name,
                                @Part("email") RequestBody email,
                                @Part("pharmacy_name") RequestBody pharmacy_name,
                                @Part("registration_number") RequestBody registration_number,
                                @Part("password") RequestBody password,
                                @Part("confirm_password") RequestBody confirm_password,
                                @Part("device_id") RequestBody device_id,
                                @Part("device_type") RequestBody device_type,
                                @Part("fcm_id") RequestBody fcm_id,
                                @Part("otp_id") RequestBody otp_id,
                                @Part("address") RequestBody address,
                                @Part("latitude") RequestBody latittude,
                                @Part("longitude") RequestBody longitude,
                                @Part MultipartBody.Part lFileBody,
                                @Part MultipartBody.Part owner_id,
                                @Part MultipartBody.Part drug_license,
                                @Part MultipartBody.Part other_document
            , @Part MultipartBody.Part image);


    @GET("api/pharmacyApi/getExecutives")
    Call<ResponseBody> getDelivery(@Header("Authentication") String Authentication);

    @GET("api/pharmacyApi/getToken")
    Call<ResponseBody> getToken(@Query("refresh_token") String Authentication,
                                @Query("id") String id);


    @Multipart
    @POST("api/pharmacyApi/addExecutive")
    Call<ResponseBody> addExecutive(@Part("mobile") RequestBody mobile,
                                    @Part("name") RequestBody name,
                                    @Part("email") RequestBody email,
                                    @Part("city") RequestBody city,
                                    @Part("state") RequestBody state,
                                    @Part("pincode") RequestBody pincode,
                                    @Part("vehicle_number") RequestBody vehicle_number,
                                    @Part("address") RequestBody vehicle_name,
                                    @Part MultipartBody.Part driving_license,
                                    @Part MultipartBody.Part owner_id,
                                    @Part MultipartBody.Part other_document,
                                    @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("api/pharmacyApi/deleteDriver")
    Call<ResponseBody> deleteExecutives(@Field("driver_id") String driver_id);


    @POST("api/pharmacyApi/logout")
    Call<ResponseBody> logout(@Header("Authentication") String Authentication);

    @GET("api/pharmacyApi/getDashboard")
    Call<ResponseBody> getHomeData(@Header("Authentication") String Authentication);


    @GET("api/pharmacyApi/getExecutiveDetails")
    Call<ResponseBody> getExecutive(@Header("Authentication") String Authentication, @Query("driver_id") String driver_id);


    @Multipart
    @POST("api/pharmacyApi/updateExecutive")
    Call<ResponseBody> updateExecutive(@Part("mobile") RequestBody mobile,
                                       @Part("name") RequestBody name,
                                       @Part("email") RequestBody email,
                                       @Part("city") RequestBody city,
                                       @Part("state") RequestBody state,
                                       @Part("pincode") RequestBody pincode,
                                       @Part("vehicle_number") RequestBody vehicle_number,
                                       @Part("address") RequestBody vehicle_name,
                                       @Part("driver_id") RequestBody driver_id,
                                       @Part MultipartBody.Part driving_license,
                                       @Part MultipartBody.Part owner_id,
                                       @Part MultipartBody.Part other_document,
                                       @Part MultipartBody.Part image);


    @GET("api/pharmacyApi/getOrders")
    Call<ResponseBody> getOrderList();

    @GET("api/pharmacyApi/getOrderDetails")
    Call<OrderDetailsResponse> getOrderDetails(@Query("order_id") String orderId);


    @FormUrlEncoded
    @POST("api/pharmacyApi/addToWatch")
    Call<ResponseBody> addToWatchList(@Field("order_id") String order_id);


    @FormUrlEncoded
    @POST("api/pharmacyApi/addToIgnore")
    Call<ResponseBody> addtoIgnore(@Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("api/pharmacyApi/removeOrderWatch")
    Call<ResponseBody> removeFromWatch(@Field("order_id") String order_id);

    @GET("api/pharmacyApi/getExecutivesForAssignment")
    Call<DeliveryAssignResponse> getAssignDelivery();

    @FormUrlEncoded
    @POST("api/pharmacyApi/assignOrder")
    Call<ResponseBody> assignOrder(@Field("order_id") String order_id, @Field("driver_id") String driver_id);

    @GET("api/pharmacyApi/getWatchList")
    Call<WarchListResponse> getWatchList();

    @GET("api/pharmacyApi/getCms")
    Call<CMSResponse> getCMSData(@Query("type") String type);

    @GET("api/pharmacyApi/getFaq")
    Call<FaqResponse> getFaq();

    @GET("api/pharmacyApi/getCms")
    Call<CMSResponse> getPrivacyPolicyData(@Query("type") String type);

    @GET("api/pharmacyApi/getCms")
    Call<CMSResponse> getTermsAndConditionsData(@Query("type") String type);

    @GET("api/pharmacyApi/")
    Call<ResponseBody> getNotificationListData();

    @POST("api/pharmacyApi/bidPrice")
    @FormUrlEncoded
    Call<BidResponseData> bidPrice(@Field("order_id") String orderId, @Field("price") String price);

    @GET("api/pharmacyApi/getEarnings")
    Call<EarningsResponse> getEarningsData();

}
