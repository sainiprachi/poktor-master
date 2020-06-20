package com.procter.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.adapter.WatchListAdapter;
import com.procter.databinding.ToolbarBindingBinding;
import com.procter.databinding.WatchListFragmentBinding;
import com.procter.model.WatchListData;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.utils.CusDialogProg;
import com.procter.viewmodel.OrderViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class WatchListFragment extends BaseFragment implements RetrofitResponseInterface, WatchListAdapter.onClickListener {
   private OrderViewModel viewModel;
   private WatchListFragmentBinding watchListFragmentBinding;
   private WatchListAdapter watchListAdapter;
   private CusDialogProg cusDialogProg;

    private RetrofitController retrofitController;
    List<WatchListData> watchListData;
    private Paint p = new Paint();

    @Override
    protected int getLayoutResource() {
        return R.layout.watch_list_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        watchListFragmentBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        viewModel.getError().observe(this, s -> {

        });
        cusDialogProg = new CusDialogProg(mContext);
        ToolbarBindingBinding toolbarBindingBinding = DataBindingUtil.findBinding(watchListFragmentBinding.toolBar.getRoot());
        assert toolbarBindingBinding != null;
        toolbarBindingBinding.txtHeader.setText("WatchList");
        toolbarBindingBinding.ivImgBack.setVisibility(View.VISIBLE);
        toolbarBindingBinding.ivImgBack.setOnClickListener(v -> {
            ((MainActivity)mContext).cardTabs.setVisibility(View.VISIBLE);
            backPress(R.id.frame);
        });
        getDetails();
        retrofitController=new RetrofitController(mContext,WatchListFragment.this);
        return watchListFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getDetails() {
        viewModel.getmIsLoading().observe(this, aBoolean -> {
            if (aBoolean){
                cusDialogProg.show();
            }else {
                cusDialogProg.dismiss();
            }
        });
        viewModel.getwatchList().observe(this, watchListDataList -> {
            if (watchListDataList.size()==0){
                watchListFragmentBinding.txtWatchList.setVisibility(View.VISIBLE);

            }   else {
                watchListFragmentBinding.txtWatchList.setVisibility(View.GONE);
            }
            this.watchListData =watchListDataList;

            watchListAdapter = new WatchListAdapter(WatchListFragment.this.getContext(), watchListDataList,this);
            watchListFragmentBinding.recyclerOpenBids.setLayoutManager(new LinearLayoutManager(mContext));
            watchListFragmentBinding.recyclerOpenBids.setAdapter(watchListAdapter);
            watchListAdapter.notifyDataSetChanged();
        });

        enableSwipe(watchListFragmentBinding.recyclerOpenBids);


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
//
                if (direction == ItemTouchHelper.LEFT) {
                }
                else  {
                    delete(watchListData.get(position).getId(),"");

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

                    }else {
                        return;
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
    private void addtoIgnore(String orderId) {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.removeFromWatch(orderId);
        retrofitController.callRetrofitApi(myResponse, 3);
    }

    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject=new JSONObject(response);
            String satus=jsonObject.getString("status");
            String message=jsonObject.getString("message");
            if (satus.equals("true")){
                MyCustomMessage.getInstance(mContext).showCustomAlert("",message);
            }else {
                MyCustomMessage.getInstance(mContext).showCustomAlert("",message);
            }
            getDetails();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
            getDetails();
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
    public void ondeleteClick(int position) {
        ((MainActivity)mContext).cardTabs.setVisibility(View.GONE);
        replaceFragment(R.id.frame, OrderDetailsFragment.newInstance(watchListData.get(position).getId()), OrderDetailsFragment.class.getSimpleName());

    }
}
