package com.procter.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.procter.R;

public class ItemAdapter extends CommonRecyclerAdapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonViewHolder(viewGroup, R.layout.item_adapter) {
            @Override
            public void onItemSelected(int position) {


            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder  viewHolder, int i) {
        View view=viewHolder.getView();
        LinearLayout llHeader=view.findViewById(R.id.llHeader);
        if (i==0){
        llHeader.setVisibility(View.VISIBLE);
        }else {
            llHeader.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
