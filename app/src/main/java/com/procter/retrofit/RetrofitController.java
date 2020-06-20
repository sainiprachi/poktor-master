package com.procter.retrofit;

import android.content.Context;

import androidx.annotation.NonNull;

import com.procter.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class RetrofitController {
    private RetrofitResponseInterface responseInterface;
    private Context context;


    public RetrofitController(Context applicationContext, Object object) {
        this.responseInterface = (RetrofitResponseInterface) object;
        this.context=applicationContext;

    }

    public void callRetrofitApi(Call<ResponseBody> myResponse, final int flag) {



        myResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if (response.code() == 200) {
                    try {

                        assert response.body() != null;
                        String result = response.body().string();
                        responseInterface.onSuccessResponse(result, flag);
                    } catch (Exception e) {
                        e.printStackTrace();
                        responseInterface.onErrorResponse(e.toString(), flag);
                    }
                } else {
                    String errorResult;

                    try {
                        assert response.errorBody() != null;

                        errorResult = response.errorBody().string();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(errorResult);
                            String status=jsonObject.getString("code");
                            if (Integer.parseInt(status)==410000){
                                ((MainActivity)context).refreshToken(myResponse,flag);
                                responseInterface.onErrorResponse("", flag);
                            }else if (Integer.parseInt(status)==400005){
                                ((MainActivity)context).logout();
                                responseInterface.onErrorResponse("", flag);
                            }else {
                                responseInterface.onErrorResponse(errorResult, flag);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    } catch (IOException e) {

                        e.printStackTrace();
                        responseInterface.onErrorResponse(e.toString(), flag);
                    }



                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable otpResponse) {

                String message="";
                long errorCode =0;
                if (otpResponse instanceof HttpException) {

                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(((HttpException)otpResponse).response().errorBody().byteStream()));
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String finallyError = sb.toString();
                    try {
                        JSONObject jsonObject = new JSONObject(finallyError);
                        message= jsonObject.getString("message");
                        errorCode = Long.parseLong(jsonObject.getString("error"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                if (errorCode==410000){

                    ((MainActivity)context).refreshToken(myResponse, flag);

                }else if (errorCode==400005){

                    ((MainActivity)context).callLogout();

                }else {
                    responseInterface.onFailureResponse(otpResponse.toString(), flag);
                }

            }
        });
    }
}
