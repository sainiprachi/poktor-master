package com.procter.adapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procter.R;
import com.procter.model.HomeModel;

import java.util.List;

public class BidsAdapter extends CommonRecyclerAdapter {
    private List<HomeModel.DataBean.WatchListBean>watchListBeanList;

    public BidsAdapter(List<HomeModel.DataBean.WatchListBean> watchListBean){
        this.watchListBeanList=watchListBean;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonViewHolder(viewGroup, R.layout.bids_adapter) {
            @Override
            public void onItemSelected(int position) {

            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder viewHolder, int i) {
        View view=viewHolder.getView();
        HomeModel.DataBean.WatchListBean watchListBean=watchListBeanList.get(i);
        if(i %2 == 1)
        {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F1F1F1"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

        TextView txtPatientName=view.findViewById(R.id.txtPatientName);
        TextView txtBids=view.findViewById(R.id.txtBids);
        TextView txtTotalPrice=view.findViewById(R.id.txtTotalPrice);
        TextView txtOrderId=view.findViewById(R.id.txtOrderId);
        txtPatientName.setText(watchListBean.getPatient_name());
        txtOrderId.setText(watchListBean.getMedicine_order_id());
        txtTotalPrice.setText("₹ "+watchListBean.getTotal_amount());
        txtBids.setText("₹ "+watchListBean.getTotal_pharmacy_price());

    }

    @Override
    public int getItemCount() {
        return watchListBeanList.size();
    }
}
