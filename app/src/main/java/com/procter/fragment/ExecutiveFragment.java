package com.procter.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.adapter.DeliveryExecutiveAdapter;
import com.procter.model.DeliveryExecutiveModel;
import com.procter.model.UserInfo;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.session.Session;
import com.procter.utils.CusDialogProg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ExecutiveFragment extends BaseFragment implements RetrofitResponseInterface, View.OnClickListener, DeliveryExecutiveAdapter.onClickListener {
    private CusDialogProg cusDialogProg;
    private RetrofitController retrofitController;
    private TextView txtNoExec;
    private ArrayList<DeliveryExecutiveModel.DataBean> deliveryList = new ArrayList<>();
    private DeliveryExecutiveAdapter deliveryExecutiveAdapter;
    private Dialog bottomSheetDialog;

    @Override
    protected int getLayoutResource() {
        return R.layout.executive_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    /*Initialise View*/

    private void initView(View view) {
        cusDialogProg = new CusDialogProg(mContext);
        retrofitController = new RetrofitController(getContext(), ExecutiveFragment.this);
        getDeliveries();
        RecyclerView rcycler = view.findViewById(R.id.rcycler);
        TextView txtHeader=view.findViewById(R.id.txtHeader);
        txtHeader.setText("Executives");
        rcycler.setLayoutManager(new LinearLayoutManager(mContext));
        deliveryExecutiveAdapter = new DeliveryExecutiveAdapter(mContext, deliveryList, this);
        rcycler.setAdapter(deliveryExecutiveAdapter);
        txtNoExec = view.findViewById(R.id.txtNoExec);
        CardView cardAddExecutive = view.findViewById(R.id.cardAddExecutive);
        cardAddExecutive.setOnClickListener(this);

    }


    /*Get Deliveryexec*/

    private void getDeliveries() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        Session session = new Session(mContext);
        UserInfo.DataBean.ProfileBean ProfileBean = session.getUserInfo();
        myResponse = apiService.getDelivery(ProfileBean.getAuth_key());
        retrofitController.callRetrofitApi(myResponse, 1);
    }

    /*delete Deliveryexec*/

    private void deleteDelivery(String driverId) {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        Session session = new Session(mContext);
        UserInfo.DataBean.ProfileBean ProfileBean = session.getUserInfo();
        myResponse = apiService.deleteExecutives(driverId);
        retrofitController.callRetrofitApi(myResponse, 2);
    }


    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        deliveryList.clear();
        switch (flag) {
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Gson gson = new Gson();
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.toString().equals("[]")) {
                            txtNoExec.setVisibility(View.VISIBLE);
                        } else {
                            txtNoExec.setVisibility(View.GONE);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                                DeliveryExecutiveModel.DataBean deliveryExecutiveMode = gson.fromJson(jsonObjectData.toString(), DeliveryExecutiveModel.DataBean.class);

                                deliveryList.add(deliveryExecutiveMode);
                            }

                            deliveryList.get(0).setSelect(true);

                            deliveryExecutiveAdapter.notifyDataSetChanged();
                        }

                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("true")) {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                        getDeliveries();

                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


        }

    }

    @Override
    public void onErrorResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
            // MyCustomMessage.getInstance(mContext).snackbar(rlParent, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardAddExecutive:
                ((MainActivity) mContext).cardTabs.setVisibility(View.GONE);
                replaceFragment(R.id.frame, new AddExecutiveFragment(),"AddExecutiveFragment");
                break;
        }
    }

    @Override
    public void onDeleteClick(int position) {
        deleteDelivery(position);

    }

    @Override
    public void onCallClick(int position) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+"91"+deliveryList.get(position).getPhone()));//change the number
        startActivity(callIntent);

    }

    @Override
    public void onEditClick(int position) {
        ((MainActivity) mContext).cardTabs.setVisibility(View.GONE);

        String driverId=deliveryList.get(position).getId();
     replaceFragment(R.id.frame, new EditExecutiveFragment(driverId),"UpdateExecutive");
    }

    @Override
    public void onLocationClick(int position) {
        ((MainActivity)mContext).cardTabs.setVisibility(View.GONE);
        String driverId=deliveryList.get(position).getId();
      replaceFragment(R.id.frame,new TrackExecutiveDataFragment(driverId),"TrackExecutiveFragment");
    }

    private void deleteDelivery(final int position) {

        bottomSheetDialog = new Dialog(mContext);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.bottom_edit_confirm_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(bottomSheetDialog.getWindow()).getAttributes());
        CardView rlDelete = bottomSheetDialog.findViewById(R.id.rlDelete);
        CardView rlCancel = bottomSheetDialog.findViewById(R.id.rlCancel);
        rlDelete.setOnClickListener(v -> {
            String driverID = deliveryList.get(position).getId();
            deleteDelivery(driverID);
            bottomSheetDialog.dismiss();
        });

        rlCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawableResource(R.drawable.shape_dialog_view);
        lp.windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setAttributes(lp);
        bottomSheetDialog.show();

    }

}
