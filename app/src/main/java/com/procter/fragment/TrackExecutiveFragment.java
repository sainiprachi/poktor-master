package com.procter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.adapter.NewAssignDeliveryAdapter;
import com.procter.databinding.ToolbarBindingBinding;
import com.procter.databinding.TrackExecutiveBinding;
import com.procter.utils.CusDialogProg;
import com.procter.viewmodel.OrderViewModel;

public class TrackExecutiveFragment extends BaseFragment {
    private TrackExecutiveBinding trackExecutiveBinding;
    private CusDialogProg cusDialogProg;
    private OrderViewModel viewModel;
    private NewAssignDeliveryAdapter newAssignDeliveryAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.track_executive;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        trackExecutiveBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        viewModel.getError().observe(this, s -> {

        });
        ToolbarBindingBinding toolbarBindingBinding = DataBindingUtil.findBinding(trackExecutiveBinding.toolBar.getRoot());
        assert toolbarBindingBinding != null;
        toolbarBindingBinding.txtHeader.setText(getString(R.string.track_executive));
        toolbarBindingBinding.ivImgBack.setVisibility(View.VISIBLE);
        toolbarBindingBinding.ivImgBack.setOnClickListener(v -> {
            ((MainActivity)mContext).cardTabs.setVisibility(View.VISIBLE);
            backPress(R.id.frame);
        });

        cusDialogProg = new CusDialogProg(mContext);
        if (isNetworkAvailable()){
            getAssignList();
        }else {
            MyCustomMessage.getInstance(mContext).showCustomAlert("",getString(R.string.check_internet));
        }
        return trackExecutiveBinding.getRoot();
    }

    private void getAssignList() {
        viewModel.getmIsLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                cusDialogProg.show();
            } else {
                cusDialogProg.dismiss();
            }
        });

        viewModel.getAssiignList().observe(this, deliveryAssignList ->
        {
            trackExecutiveBinding.recyclerAssign.setLayoutManager(new LinearLayoutManager(mContext));
            newAssignDeliveryAdapter = new NewAssignDeliveryAdapter(mContext, deliveryAssignList, position -> { });
            trackExecutiveBinding.recyclerAssign.setAdapter(newAssignDeliveryAdapter);
        });


    }

}
