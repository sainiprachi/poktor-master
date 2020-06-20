package com.procter.retrofit;

public interface RetrofitResponseInterface {
    void onSuccessResponse(String response, int flag);
    void onErrorResponse(String response, int flag);
    void onFailureResponse(String response, int flag);
}
