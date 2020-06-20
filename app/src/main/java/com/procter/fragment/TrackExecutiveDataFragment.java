package com.procter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.databinding.ToolbarBindingBinding;
import com.procter.databinding.TrackExecutiveDateBinding;
import com.procter.model.DriverDetailModel;
import com.procter.model.UserInfo;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.session.Session;
import com.procter.utils.CusDialogProg;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.procter.retrofit.ApiClient.BASE_URL;

public class TrackExecutiveDataFragment extends BaseFragment implements RetrofitResponseInterface {
    private TrackExecutiveDateBinding trackExecutiveDateBinding;
    private RetrofitController retrofitController;
    private CusDialogProg cusDialogProg;
    private String driverId="";
    @Override
    protected int getLayoutResource() {
        return R.layout.track_executive_date;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        trackExecutiveDateBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
       // viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(OrderViewModel.class);
        ToolbarBindingBinding toolbarBindingBinding = DataBindingUtil.findBinding(trackExecutiveDateBinding.toolBar.getRoot());
        assert toolbarBindingBinding != null;
        toolbarBindingBinding.txtHeader.setText("");
        toolbarBindingBinding.ivImgBack.setVisibility(View.VISIBLE);
        toolbarBindingBinding.ivImgBack.setOnClickListener(v -> {
            ((MainActivity)mContext).cardTabs.setVisibility(View.VISIBLE);
            backPress(R.id.frame);
        });
        cusDialogProg = new CusDialogProg(mContext);
        retrofitController=new RetrofitController(getContext(),TrackExecutiveDataFragment.this);
        trackExecutiveDateBinding.setFragment(this);
        getDriverDetail();

        return trackExecutiveDateBinding.getRoot();
    }
    TrackExecutiveDataFragment(String driverId) {
        this.driverId = driverId;
    }




    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        switch (flag) {
            case 1:
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        DriverDetailModel.DataBean dataBean = gson.fromJson(data.toString(), DriverDetailModel.DataBean.class);
                        setData(dataBean);

                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    private void setData(DriverDetailModel.DataBean dataBean) {
        trackExecutiveDateBinding.tvName.setText(dataBean.getName());
        trackExecutiveDateBinding.tvOrderId.setText("Order ID :"+dataBean.getId());
        trackExecutiveDateBinding.tvOrderIdNew.setText("Order ID :"+dataBean.getId());
        trackExecutiveDateBinding.txtAddress.setText("Address:"+dataBean.getAddress());
        trackExecutiveDateBinding.txtContact.setText("Contact:"+dataBean.getPhone());
        String image = BASE_URL + dataBean.getImage();
        Picasso.get().load(image).placeholder(R.drawable.user_placeholder).into(trackExecutiveDateBinding.ivProfile);

    }


    @Override
    public void onErrorResponse(String response, int flag) {

    }
    private void getDriverDetail() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        Session session = new Session(mContext);
        UserInfo.DataBean.ProfileBean profileBean = session.getUserInfo();
        myResponse = apiService.getExecutive(profileBean.getAuth_key(), driverId);
        retrofitController.callRetrofitApi(myResponse, 1);
    }


    @Override
    public void onFailureResponse(String response, int flag) {

    }
}
