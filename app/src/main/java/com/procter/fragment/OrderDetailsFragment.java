package com.procter.fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.adapter.NewAssignDeliveryAdapter;
import com.procter.adapter.OrderDetailsAdapter;
import com.procter.databinding.FragmentOrderDetailsBinding;
import com.procter.databinding.ToolbarBindingBinding;
import com.procter.interfaces.SocketDataParser;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.session.Session;
import com.procter.utils.CusDialogProg;
import com.procter.utils.Utils;
import com.procter.viewmodel.OrderViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailsFragment extends BaseFragment implements SocketDataParser, RetrofitResponseInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentOrderDetailsBinding detailsBinding;
    OrderDetailsAdapter orderDetailsAdapter;
    NewAssignDeliveryAdapter newAssignDeliveryAdapter;
    CusDialogProg cusDialogProg;
    private OrderViewModel viewModel;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private Dialog bottomSheetDialog;
    private String mParam2 = "";
    private RetrofitController retrofitController;
    private String driverId = "";
    private OnFragmentInteractionListener mListener;
    private String driveriD = "";

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param medicine_order_id
     * @return A new instance of fragment OrderDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    protected static OrderDetailsFragment newInstance(String medicine_order_id, String delivertstatus) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, medicine_order_id);
        args.putString(ARG_PARAM2, delivertstatus);

        fragment.setArguments(args);
        return fragment;
    }

    protected static OrderDetailsFragment newInstance(String medicine_order_id) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, medicine_order_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            if (getArguments().getString(ARG_PARAM2) != null)
                mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailsBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(OrderViewModel.class);
        ToolbarBindingBinding toolbarBindingBinding = DataBindingUtil.findBinding(detailsBinding.toolBar.getRoot());
        assert toolbarBindingBinding != null;
        toolbarBindingBinding.txtHeader.setText("Order ID:" + mParam1);
        toolbarBindingBinding.ivImgBack.setVisibility(View.VISIBLE);
        toolbarBindingBinding.ivImgBack.setOnClickListener(v -> {
            ((MainActivity) mContext).cardTabs.setVisibility(View.VISIBLE);
            backPress(R.id.frame);
        });
        cusDialogProg = new CusDialogProg(mContext);
        retrofitController = new RetrofitController(mContext, OrderDetailsFragment.this);
        detailsBinding.setFragment(this);


        return detailsBinding.getRoot();
    }

    private void addtoWatchList(String orderId) {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.addToWatchList(orderId);
        retrofitController.callRetrofitApi(myResponse, 1);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailsBinding.setViewModel(viewModel);
        viewModel.getmIsLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                cusDialogProg.show();
            } else {
                cusDialogProg.dismiss();
            }
        });

        viewModel.getError().observe(this, s -> {

            if (s != null && s.length() > 0) {
                MyCustomMessage.getInstance(mContext).showCustomAlert("", s);
            }

        });
        getDetails();


        viewModel.getMin().observe(this, s -> detailsBinding.setMin(s));

        viewModel.sec.observe(this, s -> detailsBinding.setSec(s));
        if (mParam2 != null && !mParam2.isEmpty()) {
            switch (mParam2) {
                case "1":
                    detailsBinding.cardStatus.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
                    detailsBinding.txtDeliveryAssign.setText("Delivery Assigned");
                    break;

                case "3":
                    detailsBinding.cardStatus.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.darkpurple));
                    detailsBinding.txtDeliveryAssign.setText(mContext.getString(R.string.delivered));
                /*llMinAmt.setVisibility(View.VISIBLE);
                txtPrice.setText(MessageFormat.format("₹{0}", orderModel.getTotal_amount()));*/
                    break;

                case "2":
                    detailsBinding.cardStatus.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.light_red));
                    detailsBinding.txtDeliveryAssign.setText(mContext.getString(R.string.out));
                    break;

                case "0":
                    detailsBinding.cardStatus.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.light_red));
                    detailsBinding.txtDeliveryAssign.setText(R.string.assign);
                    break;

            }
        }

        detailsBinding.ibBid.setOnClickListener(v -> {
            bid(mParam1, detailsBinding.edtAssignDelivery.getText().toString());
        });

        detailsBinding.edtAssignDelivery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    bid(mParam1, detailsBinding.edtAssignDelivery.getText().toString());

                }
                return false;
            }
        });

        detailsBinding.edtAssignDelivery.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                detailsBinding.edtAssignDelivery.setHint("");
            // else
            //detailsBinding.edtAssignDelivery.setHint("");
        });


    }

    private void getDetails() {
        Session session = Session.getInstance(getActivity());
        viewModel.getmIsLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                cusDialogProg.show();
            } else {
                cusDialogProg.dismiss();
            }
        });
        viewModel.getOrderDetails(mParam1).observe(this, orderDetails -> {
            cusDialogProg.dismiss();
            float price = 0;
            detailsBinding.setOrderDetails(orderDetails);
            floatingTime(Utils.floatingTimeInMillSeconds(Utils.getCurrentDateTime(), Utils.convertDatetoddMMyyy(orderDetails.getExpire_time())));
            for (int i = 0; i < orderDetails.getOrderItems().size(); i++) {
                price += Float.parseFloat(orderDetails.getOrderItems().get(i).getPrice());
                detailsBinding.tvTotalPrice.setText(MessageFormat.format("{0}", price));
            }
            orderDetailsAdapter = new OrderDetailsAdapter(OrderDetailsFragment.this.getContext(), orderDetails.getOrderItems());
            detailsBinding.setAdapter(orderDetailsAdapter);
            if (orderDetails.getPharmacyId() == null) {




                int value = (int) Math.ceil(Float.parseFloat(orderDetails.getTotalSellingPrice()) * Integer.parseInt(session.getStrVal(Session.FIRST_DISCOUNT,"0")) / 100);

                int firstValue = (int) (Float.parseFloat(orderDetails.getTotalSellingPrice())-value);

                int bidValue = (int) Math.ceil(firstValue) * Integer.parseInt(session.getStrVal(Session.BID_PERCENTAGE,"0")) / 100;

                if (bidValue > Integer.parseInt(session.getStrVal(Session.BID_MIN_VALUE,"0"))) {
                    detailsBinding.edtAssignDelivery.setHint(Math.ceil(firstValue - Integer.parseInt(session.getStrVal(Session.BID_MIN_VALUE,"0"))) + " Or Less");
                } else {
                    detailsBinding.edtAssignDelivery.setHint(Math.ceil(firstValue - bidValue) + " Or Less");
                }

                detailsBinding.tvFinalPrice.setText(String.valueOf(firstValue));
                detailsBinding.tvCurrentPrice.setText(String.valueOf(firstValue));

            } else {
                int value = (int) Math.ceil(Float.parseFloat(orderDetails.getTotalPharmacyPrice()) * Integer.parseInt(session.getStrVal(Session.BID_PERCENTAGE,"0")) / 100);

                if (value > Integer.parseInt(session.getStrVal(Session.BID_MIN_VALUE,"0"))) {
                    detailsBinding.edtAssignDelivery.setHint(Math.ceil(Float.parseFloat(orderDetails.getTotalPharmacyPrice()) - Integer.parseInt(session.getStrVal(Session.BID_MIN_VALUE,"0"))) + " Or Less");
                } else {
                    detailsBinding.edtAssignDelivery.setHint(Math.ceil(Float.parseFloat(orderDetails.getTotalPharmacyPrice()) - value) + " Or Less");
                }
                detailsBinding.tvFinalPrice.setText(orderDetails.getTotalPharmacyPrice());
                detailsBinding.tvCurrentPrice.setText(orderDetails.getTotalPharmacyPrice());

            }



            detailsBinding.tvDateTime.setText(Utils.convertDatetoddMMyyy(orderDetails.getCreatedAt()));

            detailsBinding.cvWatch.setOnClickListener(v -> {

                addtoWatchList(orderDetails.getId());

            });
        });


    }

    public void floatingTime(long mill) {


        new CountDownTimer(mill, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                detailsBinding.tvMin.setText((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)) + ""));

                detailsBinding.tvSec.setText((TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + "");

            }

            @Override
            public void onFinish() {
                detailsBinding.tvMin.setText("00");
                detailsBinding.tvSec.setText("00");
                //tvRemaining.setText("");
                if (detailsBinding.tvMin.getText().toString().matches("00")&& detailsBinding.tvSec.getText().toString().matches("00")) {
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                }

            }
        }.start();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_order_details;
    }

    @Override
    public void parseResponse(String response) {

    }

    private void bid(String orderId, String bidAmount) {
        cusDialogProg.show();
        viewModel.bidPrice(orderId, bidAmount, mContext).observe(this, bidResponseData -> {
            cusDialogProg.dismiss();
            detailsBinding.edtAssignDelivery.getText().clear();
            detailsBinding.edtAssignDelivery.clearFocus();
            getDetails();

        });
    }

    public void enterAmount() {

        // detailsBinding.edtAssignDelivery.requestFocus();
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(detailsBinding.edtAssignDelivery, InputMethodManager.SHOW_IMPLICIT);
    }

    public void showAssignDelivery() {
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

                } else {
                    MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Select Driver");
                }
            });
            newAssignDeliveryAdapter = new NewAssignDeliveryAdapter(mContext, deliveryAssign, position -> {
                driveriD = deliveryAssign.get(position).getId();


            });
            recyclerView.setAdapter(newAssignDeliveryAdapter);
        });

        Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawableResource(R.drawable.shape_dialog_view);

        lp.windowAnimations = R.style.DialogAnimation;

        bottomSheetDialog.getWindow().setAttributes(lp);
        bottomSheetDialog.show();


    }

    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        switch (flag) {
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String satus = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (satus.equals("true")) {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
