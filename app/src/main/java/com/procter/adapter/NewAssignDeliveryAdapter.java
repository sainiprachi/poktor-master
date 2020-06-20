package com.procter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.databinding.AssignDeliveryAdapterBinding;
import com.procter.model.order.DeliveryAssign;

import java.util.List;

public class NewAssignDeliveryAdapter  extends RecyclerView.Adapter<NewAssignDeliveryAdapter.ViewHolder> {
    private Context context;
    private List<DeliveryAssign>deliveryAssignList;
    private onClickDelivery onClickDelivery;

    public NewAssignDeliveryAdapter(Context context,List<DeliveryAssign>deliveryAssignList,onClickDelivery onClickDelivery){
        this.deliveryAssignList=deliveryAssignList;
        this.context=context;
        this.onClickDelivery=onClickDelivery;

    }

    public interface onClickDelivery{
        void onClick(int position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AssignDeliveryAdapterBinding itemBinding = AssignDeliveryAdapterBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(itemBinding);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeliveryAssign deliveryAssign=deliveryAssignList.get(position);
        if (deliveryAssign.isSelect()){
            onClickDelivery.onClick(position);
            holder.binding.rlParent.setBackgroundResource(R.drawable.shape_card_shadow_view);
        }else {
            holder.binding.rlParent.setBackgroundResource(0);
        }
        holder.setData(deliveryAssign);



    }

    @Override
    public int getItemCount() {
        return deliveryAssignList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        AssignDeliveryAdapterBinding binding;
        public ViewHolder(AssignDeliveryAdapterBinding assignDeliveryAdapterBinding){
            super(assignDeliveryAdapterBinding.getRoot());
            this.binding = assignDeliveryAdapterBinding;

        }



        public void setData(DeliveryAssign deliveryAssign) {
            binding.setDeliveryassign(deliveryAssign);
            binding.executePendingBindings();
            binding.rlParent.setOnClickListener(v -> {
                for (int j = 0; j < deliveryAssignList.size(); j++) {
                    deliveryAssignList.get(j).setSelect(false);
                }
                deliveryAssign.setSelect(true);
                notifyDataSetChanged();
            });

        }
    }
}
