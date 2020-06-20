package com.procter.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.adapter.ClosedBidsAdapter;
import com.procter.adapter.OpenBidsAdapter;
import com.procter.adapter.PendingBidsAdapter;
import com.procter.interfaces.SocketDataParser;
import com.procter.model.OpenBidsModel;
import com.procter.model.closed.ClosedItem;
import com.procter.model.pending.PendingItem;
import com.procter.model.pending.Response;
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
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class BidsFragment extends BaseFragment implements View.OnClickListener, SocketDataParser, OpenBidsAdapter.onClickListener, RetrofitResponseInterface, PendingBidsAdapter.Click, ClosedBidsAdapter.onClick {


    private static final String TAG = "BidsFragment";
    static String status = "";
    private CardView llOpenBids, llPendingBids, llClosedBids;
    private TextView txtPendingBids, txtClosedBids,
            txtClosedCount, txtOpenBids, txtOpenCount, txtPendingCount, txtNoBids;
    private RelativeLayout rlClosedBids, rlOpenBids, rlPendingBids;
    private RecyclerView recyclerOpenBids;
    private RecyclerView recyclerPendingBids;
    private RecyclerView recyclerClosedBids;
    private ArrayList<OpenBidsModel.ResponseBean.DataBean> openBidsModelArrayList;
    private List<PendingItem> pendingItemList = new ArrayList<>();
    private List<ClosedItem> closedItemList = new ArrayList<>();
    private Session session;
    private OpenBidsAdapter openBidsAdapter;
    private PendingBidsAdapter pendingBidsAdapter;
    private ClosedBidsAdapter closedBidsAdapter;
    private RetrofitController retrofitController;
    private CusDialogProg cusDialogProg;
    private Paint p = new Paint();

    public static BidsFragment newInstance(String status) {
        BidsFragment bidsFragment = new BidsFragment();
        BidsFragment.status = status;
        return bidsFragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.bids_fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        session = Session.getInstance(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    private void initView(View view) {
        llOpenBids = view.findViewById(R.id.llOpenBids);
        txtOpenBids = view.findViewById(R.id.txtOpenBids);
        txtPendingCount = view.findViewById(R.id.txtPendingCount);
        llPendingBids = view.findViewById(R.id.llPendingBids);
        txtPendingBids = view.findViewById(R.id.txtPendingBids);
        txtClosedBids = view.findViewById(R.id.txtClosedBids);
        rlClosedBids = view.findViewById(R.id.rlClosedBids);
        llClosedBids = view.findViewById(R.id.llClosedBids);
        rlOpenBids = view.findViewById(R.id.rlOpenBids);
        rlPendingBids = view.findViewById(R.id.rlPendingBids);
        txtClosedCount = view.findViewById(R.id.txtClosedCount);
        txtNoBids = view.findViewById(R.id.txtNoBids);
        txtOpenCount = view.findViewById(R.id.txtOpenCount);
        recyclerOpenBids = view.findViewById(R.id.recyclerOpenBids);
        recyclerPendingBids = view.findViewById(R.id.recyclerPendingBids);
        recyclerClosedBids = view.findViewById(R.id.recyclerClosedBids);
        TextView txtHeader = view.findViewById(R.id.txtHeader);
        txtHeader.setText(getString(R.string.bids));
        llOpenBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.pink));
        rlOpenBids.setBackgroundResource(R.drawable.shape_circle_white);
        txtOpenBids.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        txtOpenCount.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));

        retrofitController = new RetrofitController(mContext, BidsFragment.this);
        cusDialogProg = new CusDialogProg(mContext);

        llClosedBids.setOnClickListener(this);
        llOpenBids.setOnClickListener(this);
        llPendingBids.setOnClickListener(this);
        openBidsModelArrayList = new ArrayList<>();
        getOpenBidList();
        getBidList();
        recyclerOpenBids.setLayoutManager(new LinearLayoutManager(mContext));


        if (!status.isEmpty()) {
            if (status.matches("pending"))
                onClick(llPendingBids);
            else if (status.matches("won"))
                onClick(llClosedBids);
        }

        enableSwipe(recyclerOpenBids);
        enableSwipePending(recyclerPendingBids);
        enableSwipeClosed(recyclerClosedBids);

    }


    public void setOpenBidsAdapter() {
        openBidsAdapter = new OpenBidsAdapter(openBidsModelArrayList, this);
        recyclerOpenBids.setAdapter(openBidsAdapter);
        openBidsAdapter.notifyDataSetChanged();
    }

    public void setClosedBidAdapter() {
        closedBidsAdapter = new ClosedBidsAdapter(getContext(), closedItemList, session.getUserInfo().getUser_id(), this);
        recyclerClosedBids.setAdapter(closedBidsAdapter);
        closedBidsAdapter.notifyDataSetChanged();
    }

    public void setPendingBidsAdapter() {
        pendingBidsAdapter = new PendingBidsAdapter(getContext(), pendingItemList, session.getUserInfo().getUser_id(), this);
        recyclerPendingBids.setAdapter(pendingBidsAdapter);
        pendingBidsAdapter.notifyDataSetChanged();
    }


    public void delete(String id,String medicineOrderId){
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this bid ?")

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
                        dialog.dismiss();
                            openBidsAdapter.notifyDataSetChanged();
                            if (closedBidsAdapter!=null)
                            closedBidsAdapter.notifyDataSetChanged();


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



                    delete(openBidsModelArrayList.get(position).getId(),openBidsModelArrayList.get(position).getMedicine_order_id());

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


    private void enableSwipeClosed(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
//
                if (direction == ItemTouchHelper.LEFT) {
                } else {


                    delete(closedItemList.get(position).getId(),closedItemList.get(position).getMedicineOrderId());

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
                        return;
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void enableSwipePending(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
//
                if (direction == ItemTouchHelper.LEFT) {

                    addtoWatchList(pendingItemList.get(position).getId());
                } else {


                }
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.LEFT;
                int swipeFlags = ItemTouchHelper.LEFT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX >= 0) {
                        return;

                    } else {
                        p.setColor(Color.parseColor("#dbdbdb"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.watchlist);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llClosedBids:
                llClosedBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.pink));
                rlClosedBids.setBackgroundResource(R.drawable.shape_circle_white);
                txtClosedBids.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                txtClosedCount.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));

                llOpenBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                rlOpenBids.setBackgroundResource(R.drawable.shape_circle_red);
                txtOpenBids.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));
                txtOpenCount.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                llPendingBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                rlPendingBids.setBackgroundResource(R.drawable.shape_circle_red);
                txtPendingBids.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));
                txtPendingCount.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                recyclerPendingBids.setVisibility(View.GONE);
                recyclerOpenBids.setVisibility(View.GONE);
                recyclerClosedBids.setVisibility(View.VISIBLE);
                status="won";
                getClosedBidsList();


                break;

            case R.id.llOpenBids:
                llOpenBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.pink));
                rlOpenBids.setBackgroundResource(R.drawable.shape_circle_white);
                txtOpenBids.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                txtOpenCount.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));

                llClosedBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                rlClosedBids.setBackgroundResource(R.drawable.shape_circle_red);
                txtClosedBids.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));
                txtClosedCount.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                llPendingBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                rlPendingBids.setBackgroundResource(R.drawable.shape_circle_red);
                txtPendingBids.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));
                txtPendingCount.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                recyclerPendingBids.setVisibility(View.GONE);
                recyclerOpenBids.setVisibility(View.VISIBLE);
                recyclerClosedBids.setVisibility(View.GONE);
                status="";
                getOpenBidList();


                break;

            case R.id.llPendingBids:

                llPendingBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.pink));
                rlPendingBids.setBackgroundResource(R.drawable.shape_circle_white);
                txtPendingBids.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                txtPendingCount.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));

                llClosedBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                rlClosedBids.setBackgroundResource(R.drawable.shape_circle_red);
                txtClosedBids.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));
                txtClosedCount.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                llOpenBids.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                rlOpenBids.setBackgroundResource(R.drawable.shape_circle_red);
                txtOpenBids.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));
                txtOpenCount.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                recyclerPendingBids.setVisibility(View.VISIBLE);
                recyclerOpenBids.setVisibility(View.GONE);
                recyclerClosedBids.setVisibility(View.GONE);
                status="pending";
                getPendingBidsList();

                break;
        }
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

    private void getOpenBidList() {


        cusDialogProg.show();


        try {
            JSONObject connection = new JSONObject();
            connection.put("case_value", "205");
            connection.put("user_id", session.getUserInfo().getUser_id());
            ((MainActivity) getActivity()).sendData_from_MainActivity_without_progress(connection.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPendingBidsList() {
        cusDialogProg.show();
        try {
            JSONObject connection = new JSONObject();
            connection.put("case_value", "206");
            connection.put("user_id", session.getUserInfo().getUser_id());
            ((MainActivity) getActivity()).sendData_from_MainActivity_without_progress(connection.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getClosedBidsList() {
        cusDialogProg.show();
        try {
            JSONObject connection = new JSONObject();
            connection.put("case_value", "207");
            connection.put("user_id", session.getUserInfo().getUser_id());
            ((MainActivity) getActivity()).sendData_from_MainActivity_without_progress(connection.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void parseResponse(String response) {


        if (cusDialogProg.isShowing())
            cusDialogProg.dismiss();


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                Log.e(TAG, "parseResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    switch (jsonObject.getString("key")) {
                        case "order_accepted":
                            break;
                        case "open_bids":

                            // getBidList();
                            openBidsModelArrayList.clear();
                            JSONObject jsonBids = jsonObject.getJSONObject("response");
                            String status = jsonBids.getString("status");
                            String message = jsonBids.getString("message");
                            Gson gson = new Gson();
                            if (status.equals("true")) {
                                JSONArray jsonArray = jsonBids.getJSONArray("data");
                                if (jsonArray.toString().equals("[]")) {
                                    txtNoBids.setVisibility(View.VISIBLE);
                                } else {
                                    txtNoBids.setVisibility(View.GONE);
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
                        case "pending_bids":
                            // getBidList();
                            pendingItemList.clear();
                            JSONObject pendingResponse = jsonObject.getJSONObject("response");
                            Response response1 = new Gson().fromJson(pendingResponse.toString(), Response.class);
                            Log.d(TAG, "parseResponse: response-->" + response1.isStatus());
                            if (response1.isStatus()) {
                                if (response1.getData() != null && response1.getData().size() > 0) {
                                    txtNoBids.setVisibility(View.GONE);
                                    pendingItemList.addAll(response1.getData());
                                    txtPendingCount.setText(String.valueOf(pendingItemList.size()));
                                } else {
                                    txtNoBids.setVisibility(View.VISIBLE);
                                    txtPendingCount.setText("0");
                                }

                            } else {
                                MyCustomMessage.getInstance(mContext).showCustomAlert("", response1.getMessage());
                            }
                            setPendingBidsAdapter();

                            break;
                        case "closed_bids":
                            //getBidList();
                            closedItemList.clear();
                            JSONObject closedResponse = jsonObject.getJSONObject("response");
                            com.procter.model.closed.Response response2 = new Gson().fromJson(closedResponse.toString(), com.procter.model.closed.Response.class);
                            Log.d(TAG, "parseResponse: response-->" + response2.isStatus());
                            if (response2.isStatus()) {
                                if (response2.getData() != null && response2.getData().size() > 0) {
                                    txtNoBids.setVisibility(View.GONE);
                                    closedItemList.addAll(response2.getData());
                                    txtClosedCount.setText(String.valueOf(closedItemList.size()));
                                } else {

                                    txtClosedCount.setText("0");
                                    txtNoBids.setVisibility(View.VISIBLE);
                                }
                            } else {
                                MyCustomMessage.getInstance(mContext).showCustomAlert("", response2.getMessage());
                            }
                            setClosedBidAdapter();
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
                                    txtOpenCount.setText("0");
                                } else {
                                    txtOpenCount.setText(String.valueOf(openBids.length()));
                                }

                                if (closeBids.length() == 0) {
                                    txtClosedCount.setText("0");
                                } else {
                                    txtClosedCount.setText(String.valueOf(closeBids.length()));
                                }


                                if (pending_bids.length() == 0) {
                                    txtPendingCount.setText("0");
                                } else {
                                    txtPendingCount.setText(String.valueOf(pending_bids.length()));
                                }


                            } else {
                                MyCustomMessage.getInstance(mContext).showCustomAlert("", messageAllBids);
                            }
                            break;

                    }
                } catch (Exception e) {
                    cusDialogProg.dismiss();
                    Log.e(TAG, "Exception" + e.toString());
                    e.printStackTrace();
                }


            }
        });


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


        if (recyclerOpenBids.getVisibility() == View.VISIBLE) {
            getOpenBidList();
        } else if (recyclerPendingBids.getVisibility() == View.VISIBLE) {
            getPendingBidsList();
        } else if (recyclerClosedBids.getVisibility() == View.VISIBLE) {
            getClosedBidsList();
        }

        getBidList();
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


    @Override
    public void onChildClick(String medicineOrderId) {

        if (!medicineOrderId.isEmpty() || !medicineOrderId.matches("")) {
            replaceFragment(R.id.frame, OrderDetailsFragment.newInstance(medicineOrderId), OrderDetailsFragment.class.getSimpleName());
        } else {
            replaceFragment(R.id.frame, new AllOrderFragment(), "AllOrderFragment");
        }
        ((MainActivity) mContext).cardTabs.setVisibility(View.GONE);

    }


    @Override
    public void position(int position) {
        ((MainActivity)mContext).cardTabs.setVisibility(View.GONE);
        String medicineOrderId=closedItemList.get(position).getMedicineOrderId();
        replaceFragment(R.id.frame, AllOrderFragment.newInstance(medicineOrderId),"AllOrderFragment");

    }
}
