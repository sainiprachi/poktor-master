package com.procter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procter.model.CMSResponse;
import com.procter.model.earnings.EarningsData;
import com.procter.model.earnings.EarningsResponse;
import com.procter.model.faq.FaqResponse;
import com.procter.retrofit.ApiClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonViewModel extends ViewModel {

    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    public MutableLiveData<String> msg = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    public MutableLiveData<String> aboutUsData = new MutableLiveData<>();
    public MutableLiveData<String> privacyPolicyData = new MutableLiveData<>();
    public MutableLiveData<String> termsAndConditions = new MutableLiveData<>();
    public MutableLiveData<FaqResponse> faqs = new MutableLiveData<>();
    private MutableLiveData<ResponseBody> notificationListData = new MutableLiveData<>();
    private MutableLiveData<EarningsData> earningsData = new MutableLiveData<>();

    public LiveData<String> getAboutUsData(String type) {
        ApiClient.getApiService().getCMSData(type).enqueue(new Callback<CMSResponse>() {
            @Override
            public void onResponse(Call<CMSResponse> call, Response<CMSResponse> response) {
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        aboutUsData.setValue(response.body().getData().getDescription());
                    }
                } else {
                    error.setValue("");
                }
            }

            @Override
            public void onFailure(Call<CMSResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return aboutUsData;
    }

    public LiveData<String> getPrivacyPolicyData(String type) {
        ApiClient.getApiService().getCMSData(type).enqueue(new Callback<CMSResponse>() {
            @Override
            public void onResponse(Call<CMSResponse> call, Response<CMSResponse> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null)
                        privacyPolicyData.setValue(response.body().getData().getDescription());
                } else {
                    error.setValue("");
                }
            }

            @Override
            public void onFailure(Call<CMSResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return privacyPolicyData;
    }

    public LiveData<String> getTermsAndConditionsData(String type) {
        ApiClient.getApiService().getCMSData(type).enqueue(new Callback<CMSResponse>() {
            @Override
            public void onResponse(Call<CMSResponse> call, Response<CMSResponse> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null)
                        termsAndConditions.setValue(response.body().getData().getDescription());
                } else {
                    error.setValue("");
                }
            }

            @Override
            public void onFailure(Call<CMSResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return termsAndConditions;
    }

    public LiveData<FaqResponse> getFAQsData(String type) {
        ApiClient.getApiService().getFaq().enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null)
                        faqs.setValue(response.body());
                } else {
                    error.setValue("");
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return faqs;
    }

    /*public LiveData<ResponseBody> getNotificationListDataData(String type) {
        ApiClient.getApiService().getAboutUsData(type).enqueue(new Callback<CMSResponse>() {
            @Override
            public void onResponse(Call<CMSResponse> call, Response<CMSResponse> response) {
                if (response.body() != null) {

                } else {
                    error.setValue("");
                }
            }

            @Override
            public void onFailure(Call<CMSResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return notificationListData;
    }*/

    public LiveData<EarningsData> getEarningsData() {
        ApiClient.getApiService().getEarningsData().enqueue(new Callback<EarningsResponse>() {
            @Override
            public void onResponse(Call<EarningsResponse> call, Response<EarningsResponse> response) {
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        earningsData.setValue(response.body().getData());
                    } else {
                        error.setValue(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<EarningsResponse> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
        return earningsData;
    }
}
