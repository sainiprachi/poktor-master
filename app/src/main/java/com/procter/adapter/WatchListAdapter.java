package com.procter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.databinding.WatchListAdapterBinding;
import com.procter.model.WatchListData;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder> {
    private List<WatchListData>watchListDataList;
    private Context context;
    private onClickListener onClickListener;

    public WatchListAdapter(Context context, List<WatchListData>watchListDataList,onClickListener onClickListener){
      this.watchListDataList=watchListDataList;
      this.context=context;
      this.onClickListener =onClickListener;

    }
    public interface onClickListener{

        void ondeleteClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        WatchListAdapterBinding itemBinding = WatchListAdapterBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(itemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WatchListData watchListData=watchListDataList.get(position);
        holder.binding.txtPatientName.setText(watchListData.getPatientName());
        holder.binding.txtOrderId.setText(String.format("OrderID: %s", watchListData.getMedicineOrderId()));
        holder.binding.totalMrp.setText(String.format("%s", watchListData.getTotalSellingPrice()));
        if (TextUtils.isEmpty(watchListData.getPharmacyId())){
          holder.binding.txtPharmacyPrice.setText(String.format("%s", watchListData.getTotalSellingPrice()));
        }else {
            holder.binding.txtPharmacyPrice.setText(String.format("%s", watchListData.getTotalPharmacyPrice()));
        }

        holder.binding.ll.setOnClickListener(v -> {
            onClickListener.ondeleteClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return watchListDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WatchListAdapterBinding binding;
        public ViewHolder(WatchListAdapterBinding watchListAdapterBinding){
            super(watchListAdapterBinding.getRoot());
            this.binding = watchListAdapterBinding;

        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
