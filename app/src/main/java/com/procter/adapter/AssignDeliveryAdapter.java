package com.procter.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procter.R;

public class AssignDeliveryAdapter extends CommonRecyclerAdapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonViewHolder(viewGroup, R.layout.assign_delivery_adapter) {
            @Override
            public void onItemSelected(int position) {

            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder viewHolder, int i) {
        View view=viewHolder.getView();
        TextView txtAll=view.findViewById(R.id.txtAll);
        TextView txtPendingDelivery=view.findViewById(R.id.txtPendingDelivery);
        TextView txtDeliveredOrders=view.findViewById(R.id.txtDeliveredOrders);
        View v=view.findViewById(R.id.view);
        if (i==0){
            txtAll.setVisibility(View.VISIBLE);
            txtPendingDelivery.setVisibility(View.VISIBLE);
            txtDeliveredOrders.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);
        }else {
            txtAll.setVisibility(View.GONE);
            txtPendingDelivery.setVisibility(View.GONE);
            txtDeliveredOrders.setVisibility(View.GONE);
            v.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
