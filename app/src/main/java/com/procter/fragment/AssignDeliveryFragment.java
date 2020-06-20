package com.procter.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.adapter.NewAssignDeliveryAdapter;
import com.procter.adapter.OrderDetailsAdapter;
import com.procter.databinding.AssignDeliveryFragmentBinding;
import com.procter.databinding.ToolbarBindingBinding;
import com.procter.interfaces.SocketDataParser;
import com.procter.utils.CusDialogProg;
import com.procter.viewmodel.OrderViewModel;

import java.text.MessageFormat;
import java.util.Objects;

public class AssignDeliveryFragment extends BaseFragment implements SocketDataParser {
    private static final String ARG_PARAM1 = "ARG_PARAM1";
    private static final String ARG_PARAM2 = "ARG_PARAM2";
    private AssignDeliveryFragmentBinding detailsBinding;
    private OrderViewModel viewModel;
    private CusDialogProg cusDialogProg;
    private String mParam1,mParam2;
    private NewAssignDeliveryAdapter newAssignDeliveryAdapter;
    private OrderDetailsAdapter orderDetailsAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.assign_delivery_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailsBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(OrderViewModel.class);
        ToolbarBindingBinding toolbarBindingBinding = DataBindingUtil.findBinding(detailsBinding.toolBar.getRoot());
        assert toolbarBindingBinding != null;

        cusDialogProg = new CusDialogProg(mContext);
        detailsBinding.setFragment(this);
        return detailsBinding.getRoot();
    }
    // TODO: Rename and change types and number of parameters
    protected static AssignDeliveryFragment newInstance(String medicine_order_id,String delivertstatus) {
        AssignDeliveryFragment fragment = new AssignDeliveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, medicine_order_id);
        args.putString(ARG_PARAM2, delivertstatus);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void parseResponse(String response) {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
       /* case "1":
        cardStatus.setCardBackgroundColor(ContextCompat.getColor(context,R.color.green));
        txtDeliveryAssign.setText("Delivery Assigned");
        break;

        case"3":
        cardStatus.setCardBackgroundColor(ContextCompat.getColor(context,R.color.darkpurple));
        txtDeliveryAssign.setText(context.getString(R.string.delivered));
        llMinAmt.setVisibility(View.VISIBLE);
        txtPrice.setText(MessageFormat.format("₹{0}", orderModel.getTotal_amount()));
        break;

        case"2":
        cardStatus.setCardBackgroundColor(ContextCompat.getColor(context,R.color.light_red));
        txtDeliveryAssign.setText(context.getString(R.string.out));
        break;

        case "0":
        cardStatus.setCardBackgroundColor(ContextCompat.getColor(context,R.color.light_red));
        txtDeliveryAssign.setText("Assign For Delivery");
        break;*/

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailsBinding.setViewModel(viewModel);
        viewModel.getError().observe(this, s -> {
            cusDialogProg.dismiss();
            if (s != null && s.length() > 0)
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        });
        getDetails();

        viewModel.getMin().observe(this, s -> detailsBinding.setMin(s));
        ToolbarBindingBinding toolbarBindingBinding = DataBindingUtil.findBinding(detailsBinding.toolBar.getRoot());
        assert toolbarBindingBinding != null;
        toolbarBindingBinding.txtHeader.setText(MessageFormat.format("Order ID:{0}", mParam1));


        viewModel.sec.observe(this, s -> detailsBinding.setSec(s));
        assert mParam2 != null;
        switch (mParam2) {
            case "0":
                detailsBinding.edtAssignDelivery.setText("Assign Delivery");
                break;
            case "2":
                detailsBinding.edtAssignDelivery.setText("Out For Delivery");
                break;
            case "3":
                detailsBinding.edtAssignDelivery.setText("Delivered");
                break;

            case "1":
                detailsBinding.edtAssignDelivery.setText("Delivery Assigned");
                break;



        }

        detailsBinding.rlAssignDelivery.setOnClickListener(v -> {
           if (mParam2.equals("0")){
               showAssignDelivery();
           }
        });



    }


    private void getDetails() {
        cusDialogProg.show();
        viewModel.getOrderDetails(mParam1).observe(this, orderDetails -> {
            cusDialogProg.dismiss();
            float price = 0;
            detailsBinding.setOrderDetails(orderDetails);
            detailsBinding.txtContactNu.setText(MessageFormat.format("Contact:{0}", orderDetails.getPatientPhone()));
            detailsBinding.tvOrderId.setText(MessageFormat.format("Order ID:{0}", orderDetails.getMedicineOrderId()));
            detailsBinding.tvAddress.setText(MessageFormat.format("Address:{0}", orderDetails.getPatientAddress()));
            orderDetailsAdapter = new OrderDetailsAdapter(AssignDeliveryFragment.this.getContext(), orderDetails.getOrderItems());
            detailsBinding.setAdapter(orderDetailsAdapter);
            for (int i = 0; i < orderDetails.getOrderItems().size(); i++) {
                 price+=Float.parseFloat(orderDetails.getOrderItems().get(i).getPrice());
                detailsBinding.tvTotalPrice.setText(MessageFormat.format("{0}", price));
            }


        });

    }

    private String driverId = "";

    private void showAssignDelivery() {
        Dialog bottomSheetDialog = new Dialog(mContext);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.dialog_assign_delivery);
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recyclerAssign);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(bottomSheetDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        viewModel.getAssiignList().observe(this, deliveryAssign -> {
            viewModel.getmIsLoading().observe(this, aBoolean -> {
                if (aBoolean) {
                    cusDialogProg.show();
                } else {
                    cusDialogProg.dismiss();
                }
            });
            Button btnConfirm = bottomSheetDialog.findViewById(R.id.btnConfirm);
            btnConfirm.setOnClickListener(v -> {
                if (!driverId.isEmpty()) {
                    viewModel.assignDelivery(mParam1,driverId);
                    replaceFragment(R.id.frame,new AssignDeliveryFragment(),"AllOrderFragment");

                } else {
                    MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Select Driver");
                }
            });
            newAssignDeliveryAdapter = new NewAssignDeliveryAdapter(mContext, deliveryAssign, position -> {
                driverId = deliveryAssign.get(position).getId();


            });
            recyclerView.setAdapter(newAssignDeliveryAdapter);
        });

        Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawableResource(R.drawable.shape_dialog_view);

        lp.windowAnimations = R.style.DialogAnimation;

        bottomSheetDialog.getWindow().setAttributes(lp);
        bottomSheetDialog.show();


    }

}
