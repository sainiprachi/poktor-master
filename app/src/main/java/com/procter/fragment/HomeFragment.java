package com.procter.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.activity.SplashActivity;
import com.procter.adapter.OpenBidsAdapter;
import com.procter.interfaces.SocketDataParser;
import com.procter.model.HomeModel;
import com.procter.model.OpenBidsModel;
import com.procter.model.UserInfo;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.session.Session;
import com.procter.utils.CusDialogProg;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static android.os.Looper.getMainLooper;

public class HomeFragment extends BaseFragment implements View.OnClickListener, RetrofitResponseInterface, SocketDataParser, OpenBidsAdapter.onClickListener {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.tvOutCount)
    TextView tvOutCount;
    @BindView(R.id.cvOut)
    CardView cvOut;
    @BindView(R.id.cvAssignCount)
    TextView tvAssignCount;
    @BindView(R.id.cvAssign)
    CardView cvAssign;
    @BindView(R.id.cvExpiryCount)
    TextView tvExpiryCount;
    @BindView(R.id.cvExpiry)
    CardView cvExpiry;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.ratingCount)
    TextView ratingCount;
    BottomSheetBehavior behavior;
    private List<HomeModel.DataBean.WatchListBean> arrylist;
    private List<HomeModel.DataBean.WatchListBean> watchListBeanArrayList;
    private RetrofitController retrofitController;
    private CusDialogProg cusDialogProg;
    private Session session;
    private LinearLayout llLocation;
    private LinearLayout llWatchList;
    private TextView txtPharmacyNAme, txtAddress, txtOpenBidsCount,
            txtWonBidsCount, txtPendingBids, txtTotalEarning, txtNotificationCount, txtNoData;
    private ImageView ivPharmacy;
    private RecyclerView recylerBids;
    private ArrayList<OpenBidsModel.ResponseBean.DataBean> openBidsModelArrayList;
    private Handler handler = new Handler(Looper.myLooper());
    private OpenBidsAdapter openBidsAdapter;
    private Paint p = new Paint();

    @Override
    protected int getLayoutResource() {
        return R.layout.home_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }


    private void initView(View view) {

        LinearLayout llAllBids = view.findViewById(R.id.llAllBids);
        LinearLayout llOpenBids = view.findViewById(R.id.llOpenBids);
        LinearLayout llPendingBids = view.findViewById(R.id.llPendingBids);
        LinearLayout wonBids = view.findViewById(R.id.wonBids);
        LinearLayout llEarnings = view.findViewById(R.id.llEarnings);
        FrameLayout rlTrack = view.findViewById(R.id.rlTrack);
        TextView txtLogout = view.findViewById(R.id.txtLogout);
        txtPharmacyNAme = view.findViewById(R.id.txtPharmacyNAme);
        txtOpenBidsCount = view.findViewById(R.id.txtOpenBidsCount);
        txtWonBidsCount = view.findViewById(R.id.txtWonBidsCount);
        txtPendingBids = view.findViewById(R.id.txtPendingBids);
        txtTotalEarning = view.findViewById(R.id.txtTotalEarning);
        txtNotificationCount = view.findViewById(R.id.txtNotificationCount);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtNoData = view.findViewById(R.id.txtNoData);
        llLocation = view.findViewById(R.id.llLocation);
        ivPharmacy = view.findViewById(R.id.ivPharmacy);
        recylerBids = view.findViewById(R.id.recylerBids);
        CoordinatorLayout coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        LinearLayout bottomSheet = view.findViewById(R.id.bottomSheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        enableSwipe(recylerBids);
        llWatchList = view.findViewById(R.id.llWatchList);
        txtLogout.setOnClickListener(this);
        TextView txtTime = view.findViewById(R.id.txtTime);
        openBidsModelArrayList = new ArrayList<>();
        rlTrack.setOnClickListener(this);
        llAllBids.setOnClickListener(this);
        llOpenBids.setOnClickListener(this);
        llPendingBids.setOnClickListener(this);
        wonBids.setOnClickListener(this);
        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Width", "" + width);
        Log.e("height", "" + height);
        int bottomSheetHeight = (int) Math.ceil(height/3.2);
        behavior.setPeekHeight(bottomSheetHeight);
        session = new Session(mContext);
        behavior.setHideable(false);


        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                /*if (i == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }*/
                switch (i) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        /*if (!applicationPreference.hasIntroducedBottomSheet()) {
                            hasBottomSheetExtended = true;
                        }*/
                        bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        behavior.setPeekHeight(bottomSheetHeight);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                       /* if (!applicationPreference.hasIntroducedBottomSheet()) {
                            hasBottomSheetExtended = true;
                        }*/
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        cusDialogProg = new CusDialogProg(mContext);
        retrofitController = new RetrofitController(getActivity(), this);

        if (watchListBeanArrayList == null || arrylist == null) {
            watchListBeanArrayList = new ArrayList<>();
            arrylist = new ArrayList<>();
        }


        recylerBids.setLayoutManager(new LinearLayoutManager(getActivity()));
        llEarnings.setOnClickListener(this);


        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtTime.setText(new SimpleDateFormat("hh:mm:a EEEE, MMMM dd", Locale.US).format(new Date()));

                someHandler.postDelayed(this, 1000);
            }
        }, 10);


        txtPharmacyNAme.setOnClickListener(v -> {

            replaceFragment(R.id.frame, new MyProfileFragment(), "MyProfileFragment");

        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNetworkAvailable()) {
            getHomeData();
        } else {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.check_internet));
        }
    }


    public void delete(String id, String medicineOrderId) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this Bid ?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        addtoIgnore(id);
                        dialog.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (openBidsAdapter != null) {
                            openBidsAdapter.notifyDataSetChanged();
                        }

                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void enableSwipe(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    final OpenBidsModel.ResponseBean.DataBean deletedModel = openBidsModelArrayList.get(position);
                    final int deletedPosition = position;

                    addtoWatchList(openBidsModelArrayList.get(position).getId());
                    // showing snack bar with Undo option

//                    Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), " removed from Recyclerview!", Snackbar.LENGTH_LONG);
//                    snackbar.setAction("UNDO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // undo is selected, restore the deleted item
//                            openBidsAdapter.restoreItem(deletedModel, deletedPosition);
//                        }
//                    });
//                    snackbar.setActionTextColor(Color.YELLOW);
//                    snackbar.show();
                } else {
                    final OpenBidsModel.ResponseBean.DataBean deletedModel = openBidsModelArrayList.get(position);
                    final int deletedPosition = position;


                    delete(openBidsModelArrayList.get(position).getId(), openBidsModelArrayList.get(position).getMedicine_order_id());

                    // showing snack bar with Undo option
//                    Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), " removed from Recyclerview!", Snackbar.LENGTH_LONG);
//                    snackbar.setAction("UNDO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            // undo is selected, restore the deleted item
//                            openBidsAdapter.restoreItem(deletedModel, deletedPosition);
//                        }
//                    });
//                    snackbar.setActionTextColor(Color.YELLOW);
//                    snackbar.show();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#dbdbdb"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);

                    } else {
                        p.setColor(Color.parseColor("#dbdbdb"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.watchlist);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAllBids:
            case R.id.llOpenBids:
                BidsFragment.status = "";
                ((MainActivity) Objects.requireNonNull(getActivity())).addFragmentback(R.id.frame, new BidsFragment(), "BidsFragment");
                break;

            case R.id.llPendingBids:
                ((MainActivity) Objects.requireNonNull(getActivity())).addFragmentback(R.id.frame, BidsFragment.newInstance("pending"), "BidsFragment");
                break;
            case R.id.wonBids:
                ((MainActivity) Objects.requireNonNull(getActivity())).addFragmentback(R.id.frame, BidsFragment.newInstance("won"), "BidsFragment");
                break;

            case R.id.txtLogout:
                Session session = new Session(mContext);
                session.logout(mContext);
                startActivity(new Intent(mContext, SplashActivity.class));
                break;

            case R.id.llEarnings:
                replaceFragment(R.id.frame, new EarningsFragment(), "EarningsFragment");
                break;

            case R.id.rlTrack:
                ((MainActivity) mContext).cardTabs.setVisibility(View.GONE);
                replaceFragment(R.id.frame, new TrackExecutiveFragment(), "TrackExecutiveFragment");
                break;
        }
    }


    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();

        switch (flag) {

            case 3:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Gson gson = new Gson();
                    if (status.equals("true")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        HomeModel.DataBean dataBean = gson.fromJson(jsonObjectData.toString(), HomeModel.DataBean.class);
                        setData(dataBean);
                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getAllBidsList();
                break;
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

                recyclerViewCheck();
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String satus = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (satus.equals("true")) {
                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerViewCheck();
                break;
        }
    }

    public void recyclerViewCheck() {

        getAllBidsList();

    }


    /*Set dashboard data*/

    private void setData(HomeModel.DataBean data) {
        handler.post(() -> {
            txtPharmacyNAme.setText(data.getProfile().getPharmacy_name());
            txtTotalEarning.setText(MessageFormat.format("{0}", data.getTotal_earnings()));
            txtNotificationCount.setText(MessageFormat.format("{0}", data.getNotification_count()));
            tvOutCount.setText(MessageFormat.format("{0}", data.getDelivering_order_count()));
            tvAssignCount.setText(MessageFormat.format("{0}", data.getUnassigned_order_count()));
            tvExpiryCount.setText(MessageFormat.format("{0}", data.getExpiring_bids_count()));
            ratingBar.setRating(Float.parseFloat(data.getRating()));
            session.setStrVal(Session.FIRST_DISCOUNT,data.getSettings().getFirst_discount());
            session.setStrVal(Session.BID_MIN_VALUE,data.getSettings().getBid_minimum_value());
            session.setStrVal(Session.BID_PERCENTAGE,data.getSettings().getBid_percentage());
            ratingCount.setText(data.getRating());
            if (data.getProfile().getAddress() != null && !data.getProfile().getAddress().isEmpty()) {
                String str = data.getProfile().getAddress();
                List<String> address = Arrays.asList(str.split(","));
                if (address.size() > 5) {

                    txtAddress.setText(address.get(address.size() - 4).concat(",").concat(address.get(address.size() - 3)));
                } else {
                    txtAddress.setText(data.getProfile().getAddress());
                }
            }
            arrylist = data.getWatch_list();
            watchListBeanArrayList.addAll(arrylist);
            String image = "http://52.200.8.36/poktor/" + data.getProfile().getImage();
            Picasso.get().load(image).placeholder(R.drawable.user_placeholder).into(((MainActivity) mContext).ivUserImage);
            Picasso.get().load(image).placeholder(R.drawable.pharmacylogo).into(ivPharmacy);
            ((MainActivity) mContext).txtUserName.setText(data.getProfile().getName());
            if (data.getProfile().getAddress() != null || !TextUtils.isEmpty(data.getProfile().getAddress())) {
                ((MainActivity) mContext).txtAddressNew.setVisibility(View.VISIBLE);
                ((MainActivity) mContext).txtAddressNew.setText(data.getProfile().getAddress());
                txtAddress.setVisibility(View.VISIBLE);
            } else {
                llLocation.setVisibility(View.GONE);
                txtAddress.setVisibility(View.GONE);
                ((MainActivity) mContext).txtAddressNew.setVisibility(View.GONE);
            }


            llWatchList.setOnClickListener(v -> {
                ((MainActivity) mContext).cardTabs.setVisibility(View.GONE);
                replaceFragment(R.id.frame, new WatchListFragment(), "WatchListFragment");

            });


            ((MainActivity) mContext).txtMobilenu.setText(data.getProfile().getPhone());

        });


    }

    @Override
    public void onErrorResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            MyCustomMessage.getInstance(mContext).showCustomAlert("", message);

            recyclerViewCheck();
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

    private void getHomeData() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        UserInfo.DataBean.ProfileBean userinfo = session.getUserInfo();
        myResponse = apiService.getHomeData(userinfo.getAuth_key());
        retrofitController.callRetrofitApi(myResponse, 3);
    }

    private void getBidList() {
        try {
            JSONObject connection = new JSONObject();
            connection.put("case_value", "204");
            connection.put("user_id", session.getUserInfo().getUser_id());
            ((MainActivity) getActivity()).sendData_from_MainActivity_without_progress(connection.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseResponse(String response) {
        cusDialogProg.dismiss();
        Log.e(TAG, "parseResponse: " + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (jsonObject.getString("key")) {
                case "order_accepted":
                    break;
                case "open_bids":
                    openBidsModelArrayList.clear();
                    JSONObject jsonBids = jsonObject.getJSONObject("response");
                    String status = jsonBids.getString("status");
                    String message = jsonBids.getString("message");
                    Gson gson = new Gson();
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonBids.getJSONArray("data");
                        if (jsonArray.toString().equals("[]")) {
                            txtNoData.setVisibility(View.VISIBLE);
                        } else {
                            txtNoData.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                                OpenBidsModel.ResponseBean.DataBean dataBean = gson.fromJson(jsonObjectData.toString(), OpenBidsModel.ResponseBean.DataBean.class);
                                openBidsModelArrayList.add(dataBean);

                            }
                        }

                        setOpenBidsAdapter();

                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                    }
                    break;
                case "bids":

                    JSONObject allBids = jsonObject.getJSONObject("response");
                    String statusAllBids = allBids.getString("status");
                    String messageAllBids = allBids.getString("message");

                    if (statusAllBids.equals("true")) {

                        JSONObject data = allBids.getJSONObject("data");
                        JSONArray openBids = data.getJSONArray("open_bids");
                        JSONArray closeBids = data.getJSONArray("closed_bids");
                        JSONArray pending_bids = data.getJSONArray("pending_bids");
                        if (openBids.length() == 0) {
                            txtOpenBidsCount.setText("0");
                        } else {
                            txtOpenBidsCount.setText(String.valueOf(openBids.length()));
                        }

                        if (closeBids.length() == 0) {
                            txtWonBidsCount.setText("0");
                        } else {
                            txtWonBidsCount.setText(String.valueOf(closeBids.length()));
                        }


                        if (pending_bids.length() == 0) {
                            txtPendingBids.setText("0");
                        } else {
                            txtPendingBids.setText(String.valueOf(pending_bids.length()));
                        }


                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", messageAllBids);
                    }
                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOpenBidsAdapter() {
        openBidsAdapter = new OpenBidsAdapter(openBidsModelArrayList, this);
        recylerBids.setAdapter(openBidsAdapter);
        openBidsAdapter.notifyDataSetChanged();
        getBidList();
    }

    public void getAllBidsList() {
        try {
            JSONObject connection = new JSONObject();
            connection.put("case_value", "205");
            connection.put("user_id", session.getUserInfo().getUser_id());
            ((MainActivity) Objects.requireNonNull(getActivity())).sendData_from_MainActivity_without_progress(connection.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getwatchlistClick(int position) {
        addtoWatchList(openBidsModelArrayList.get(position).getId());
    }

    @Override
    public void ondeleteClick(int position) {
        addtoIgnore(openBidsModelArrayList.get(position).getId());
    }

    @Override
    public void onItemClick(OpenBidsModel.ResponseBean.DataBean data) {

        ((MainActivity) mContext).cardTabs.setVisibility(View.GONE);

        replaceFragment(R.id.frame, OrderDetailsFragment.newInstance(data.getId(), ""), OrderDetailsFragment.class.getSimpleName());
    }


    private void addtoWatchList(String orderId) {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.addToWatchList(orderId);
        retrofitController.callRetrofitApi(myResponse, 1);
    }

    private void addtoIgnore(String orderId) {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.addtoIgnore(orderId);
        retrofitController.callRetrofitApi(myResponse, 2);
    }
}
