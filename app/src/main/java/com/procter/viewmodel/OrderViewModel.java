package com.procter.viewmodel;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.procter.Singleton.MyCustomMessage;
import com.procter.model.WarchListResponse;
import com.procter.model.WatchListData;
import com.procter.model.bid.BidResponseData;
import com.procter.model.order.DeliveryAssign;
import com.procter.model.order.DeliveryAssignResponse;
import com.procter.model.order.OrderDetails;
import com.procter.model.order.OrderDetailsResponse;
import com.procter.retrofit.ApiClient;
import com.procter.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {
    private static final String TAG = "OrderViewModel";
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private MutableLiveData<String> msg = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<String> min = new MutableLiveData<>();
    public MutableLiveData<String> sec = new MutableLiveData<>();
    private MutableLiveData<List<DeliveryAssign>> assignDetail = new MutableLiveData<>();
    private MutableLiveData<BidResponseData> bidPriceData = new MutableLiveData<>();

    public MutableLiveData<Boolean> getmIsLoading() {
        return mIsLoading;
    }

    public MutableLiveData<String> getMin() {
        return min;
    }

    public void  setMin() {
        min.setValue(null);
    }

    public void setSec(){
        sec.setValue(null);
    }

    public LiveData<OrderDetails> getOrderDetails(String orderId) {
        mIsLoading.setValue(true);
        MutableLiveData<OrderDetails> orderDetails = new MutableLiveData<>();
        ApiClient.getApiService().getOrderDetails(orderId).enqueue(new Callback<OrderDetailsResponse>() {
            @Override
            public void onResponse(Call<OrderDetailsResponse> call, Response<OrderDetailsResponse> response) {
                mIsLoading.setValue(false);
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        orderDetails.setValue(response.body().getData());

                        //calculateTime(response.body().getData());
                    } else {
                        error.setValue(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return orderDetails;
    }



    private void calculateTime(OrderDetails orderDetails) {
        try {
            Date createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(orderDetails.getExpire_time());
            if (createdAt.before(new Date())) {
                if ((30 * 60000) - getTimeDifference(new Date(), createdAt) > 0)
                    showTimer((30 * 60000) - getTimeDifference(new Date(), createdAt));
                else {
                    min.setValue("00");
                    sec.setValue("00");
                }
            } else {
                min.setValue("00");
                sec.setValue("00");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private long getTimeDifference(Date startTime, Date endTime) {
        return startTime.getTime() - endTime.getTime();
    }

    private void showTimer(long duration) {
        Log.d(TAG, "showTimer: " + duration);
        new CountDownTimer(duration, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long remainingSecs = millisUntilFinished / 1000;
                min.postValue(String.format(Locale.getDefault(), "%02d", remainingSecs / 60));
                sec.postValue(String.format(Locale.getDefault(), "%02d", (remainingSecs % 60)));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }


    public LiveData<List<DeliveryAssign>> getAssiignList() {
        mIsLoading.setValue(true);
        ApiClient.getApiService().getAssignDelivery().enqueue(new Callback<DeliveryAssignResponse>() {
            @Override
            public void onResponse(Call<DeliveryAssignResponse> call, Response<DeliveryAssignResponse> response) {
                mIsLoading.setValue(false);
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        assignDetail.setValue(response.body().getData());
                    } else {
                        error.setValue(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryAssignResponse> call, Throwable t) {
                mIsLoading.setValue(false);
            }
        });
        return assignDetail;
    }

    public void assignDelivery(String orderId, String driverId) {
        mIsLoading.setValue(true);
        ApiClient.getApiService().assignOrder(orderId, driverId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mIsLoading.setValue(false);
                assert response.body() != null;
                if (!response.body().toString().isEmpty()) {
                    msg.setValue(response.toString());
                } else {
                    error.setValue(response.body().toString());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }

    public LiveData<List<WatchListData>> getwatchList() {
        mIsLoading.setValue(true);
        MutableLiveData<List<WatchListData>> watListData = new MutableLiveData<>();
        ApiClient.getApiService().getWatchList().enqueue(new Callback<WarchListResponse>() {
            @Override
            public void onResponse(Call<WarchListResponse> call, Response<WarchListResponse> response) {
                mIsLoading.setValue(false);
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        watListData.setValue(response.body().getData());
                    } else {
                        error.setValue(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<WarchListResponse> call, Throwable t) {
                mIsLoading.setValue(false);
            }
        });
        return watListData;
    }

    public LiveData<BidResponseData> bidPrice(String orderId, String price, Context context) {

        ApiClient.getApiService().bidPrice(orderId, price).enqueue(new Callback<BidResponseData>() {
            @Override
            public void onResponse(Call<BidResponseData> call, Response<BidResponseData> response) {
                try {

                    assert response.errorBody() != null;
                    if (response.body() != null) {

                        if (response.body().isStatus()) {
                            String errorBody = response.body().getMessage();
                            bidPriceData.setValue(response.body());
                            MyCustomMessage.getInstance(context).showCustomAlert("" ,errorBody);
                            //BidResponseData bidResponseData = new Gson().fromJson(errorBody, BidResponseData.class);

                           // MyCustomMessage.getInstance(context).showCustomAlert(errorBody ,"");
                        }
                    } else {
                        mIsLoading.setValue(false);
                        String errorBody = response.errorBody().string();
                        BidResponseData bidResponseData = new Gson().fromJson(errorBody, BidResponseData.class);
                        error.setValue(bidResponseData.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BidResponseData> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return bidPriceData;
    }

    public LiveData<String> getError() {
        mIsLoading.setValue(true);
        error.setValue(null);
        return error;
    }
}
