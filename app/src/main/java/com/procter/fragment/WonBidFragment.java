package com.procter.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.adapter.AssignDeliveryAdapter;
import com.procter.adapter.ItemAdapter;
import com.procter.adapter.NewAssignDeliveryAdapter;
import com.procter.utils.CusDialogProg;
import com.procter.viewmodel.OrderViewModel;

import java.util.Objects;

public class WonBidFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    OrderViewModel viewModel;
    private CusDialogProg cusDialogProg;
    private NewAssignDeliveryAdapter newAssignDeliveryAdapter;
    private String driveriD="";
    @Override
    protected int getLayoutResource() {
        return R.layout.won_bids_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        RecyclerView recyclerItem = view.findViewById(R.id.recyclerItem);
        recyclerItem.setLayoutManager(new LinearLayoutManager(mContext));
        RelativeLayout rlAssignDelivery = view.findViewById(R.id.rlAssignDelivery);
        rlAssignDelivery.setOnClickListener(this);
        recyclerItem.setAdapter(new ItemAdapter());
        getAssignList();
    }

    private void showAssignDelivery() {
        Dialog bottomSheetDialog = new Dialog(mContext);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.dialog_assign_delivery);
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recyclerAssign);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(new AssignDeliveryAdapter());
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlAssignDelivery) {
            showAssignDelivery();
        }
    }

    private void getAssignList(){

        Dialog bottomSheetDialog = new Dialog(mContext);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.dialog_assign_delivery);
        recyclerView = bottomSheetDialog.findViewById(R.id.recyclerAssign);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        viewModel.getAssiignList().observe(this, deliveryAssign -> viewModel.getmIsLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                cusDialogProg.show();
            } else {
                cusDialogProg.dismiss();
            }

            newAssignDeliveryAdapter = new NewAssignDeliveryAdapter(mContext, deliveryAssign, position ->
                    driveriD=deliveryAssign.get(position).getId());
            recyclerView.setAdapter(newAssignDeliveryAdapter);
        }));



    }
}
