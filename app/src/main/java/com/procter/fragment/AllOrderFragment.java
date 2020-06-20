package com.procter.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.adapter.AllOrderAdapter;
import com.procter.model.OrdersModel;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.utils.CusDialogProg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class AllOrderFragment extends BaseFragment implements AllOrderAdapter.onClick, RetrofitResponseInterface, View.OnClickListener {
    private CusDialogProg cusDialogProg;
    private RetrofitController retrofitController;
    private ArrayList<OrdersModel.DataBean> orderModelArrayList;
    private AllOrderAdapter allOrderAdapter;
    private TextView txtCountOrder;


    protected static AllOrderFragment newInstance(String medicine_order_id) {
        AllOrderFragment fragment = new AllOrderFragment();
        Bundle args = new Bundle();
        args.putString("medicine_order_id", medicine_order_id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.all_order_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        if (cusDialogProg == null) {
            cusDialogProg = new CusDialogProg(mContext);

        }

        if (retrofitController == null) {
            retrofitController = new RetrofitController(mContext, AllOrderFragment.this);
        }

        if (orderModelArrayList == null) {
            orderModelArrayList = new ArrayList<>();
        }

        TextView txtHeader = view.findViewById(R.id.txtHeader);
        ImageView ivImgBack = view.findViewById(R.id.ivImgBack);
        ivImgBack.setOnClickListener(this);
        txtHeader.setText(getString(R.string.order_history));
        RecyclerView recyclerOrder = view.findViewById(R.id.recyclerOrder);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(mContext));
        allOrderAdapter = new AllOrderAdapter(this, orderModelArrayList, mContext);
        recyclerOrder.setAdapter(allOrderAdapter);
        txtCountOrder = view.findViewById(R.id.txtCountOrder);

        if (isNetworkAvailable()) {
            getOrderList();
        } else {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.check_internet));
        }
    }

    @Override
    public void onclick(int position) {
        showBidPopup();
    }

    @Override
    public void onCallClick(int position) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "91" + orderModelArrayList.get(position).getPatient_phone()));//change the number
        startActivity(callIntent);
    }

    @Override
    public void onDataClick(int position) {
        String medicineOrderId = orderModelArrayList.get(position).getId();
        replaceFragment(R.id.frame, AssignDeliveryFragment.newInstance(medicineOrderId,orderModelArrayList.get(position).getDelivery_status()),"AssignDeliveryFragment");

        /*if (orderModelArrayList.get(position).getDelivery_status().equals("0")){
            replaceFragment(R.id.frame, AssignDeliveryFragment.newInstance(medicineOrderId,orderModelArrayList.get(position).getDelivery_status()),"AssignDeliveryFragment");
        }else {
            replaceFragment(R.id.frame, OrderDetailsFragment.newInstance(medicineOrderId,orderModelArrayList.get(position).getDelivery_status()), "OrderDetailsFragment");

        }*/
    }

    private void showBidPopup() {
        Dialog bottomSheetDialog = new Dialog(mContext);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.bid_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(bottomSheetDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawableResource(R.drawable.shape_dialog_view);
        lp.windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setAttributes(lp);
        bottomSheetDialog.show();
    }

    private void getOrderList() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.getOrderList();
        retrofitController.callRetrofitApi(myResponse, 1);
    }


    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        orderModelArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            Gson gson = new Gson();
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            if (status.equals("true")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectObjectData = jsonArray.getJSONObject(i);
                    OrdersModel.DataBean dataBean = gson.fromJson(jsonObjectObjectData.toString(), OrdersModel.DataBean.class);
                    orderModelArrayList.add(dataBean);
                }

                allOrderAdapter.notifyDataSetChanged();
                txtCountOrder.setText(MessageFormat.format("All Orders (Total {0} )", orderModelArrayList.size()));
            } else {
                MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (getArguments()!=null){
            if (getArguments().getString("medicine_order_id")!=null){
                if (!getArguments().getString("medicine_order_id").isEmpty()){

                    for (int i=0;i<orderModelArrayList.size();i++){
                        if (getArguments().getString("medicine_order_id").matches(orderModelArrayList.get(i).getMedicine_order_id())){
                            replaceFragment(R.id.frame, AssignDeliveryFragment.newInstance(orderModelArrayList.get(i).getId(),orderModelArrayList.get(i).getDelivery_status()),"AssignDeliveryFragment");
                            this.setArguments(null);
                            return;
                        }
                    }



                }
            }
        }


    }

    @Override
    public void onErrorResponse(String response, int flag) {

    }

    @Override
    public void onFailureResponse(String response, int flag) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImgBack:
                backPress(R.id.frame);
                break;
        }
    }
}
