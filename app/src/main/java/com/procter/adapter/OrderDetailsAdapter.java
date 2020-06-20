package com.procter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.databinding.ItemMedicineOrderDetailsBinding;
import com.procter.model.order.OrderItemsItem;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter {

    private List<OrderItemsItem> orderItemsItemList;

    public OrderDetailsAdapter(Context context, List<OrderItemsItem> orderItemsItemList) {
        Context context1 = context;
        this.orderItemsItemList = orderItemsItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMedicineOrderDetailsBinding itemBinding = ItemMedicineOrderDetailsBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.setData(orderItemsItemList.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return orderItemsItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ItemMedicineOrderDetailsBinding binding;

        MyViewHolder(ItemMedicineOrderDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(OrderItemsItem orderItemsItem, int position) {
            binding.setOrderItem(orderItemsItem);
            binding.setSlNo(String.valueOf(position+1));
            binding.executePendingBindings();
        }
    }
    public void assignDelivery(){

    }
}
