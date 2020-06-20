package com.procter.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.model.OrdersModel;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;

import static com.procter.retrofit.ApiClient.BASE_URL;

public class AllOrderAdapter extends CommonRecyclerAdapter {
    private onClick onClick;
    private ArrayList<OrdersModel.DataBean>orderModelList;
    private Context context;

    public AllOrderAdapter(onClick onClick, ArrayList<OrdersModel.DataBean>orderModelList, Context context){
        this.onClick=onClick;
        this.context=context;
        this.orderModelList=orderModelList;
    }

   public interface onClick{
        void onclick(int position);
        void onCallClick(int position);
        void onDataClick(int position);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonViewHolder(viewGroup, R.layout.all_order_adapter) {
            @Override
            public void onItemSelected(int position) {

            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        OrdersModel.DataBean orderModel=orderModelList.get(i);
        View view=viewHolder.getView();
        CardView cardStatus=view.findViewById(R.id.cardStatus);
        LinearLayout llCCall=view.findViewById(R.id.llCCall);
        LinearLayout llOrders=view.findViewById(R.id.llOrders);
        ImageView ivProfile=view.findViewById(R.id.ivProfile);
        TextView txtUserName=view.findViewById(R.id.txtUserName);
        TextView txtDeliveryAssign=view.findViewById(R.id.txtDeliveryAssign);
        TextView txtContact=view.findViewById(R.id.txtContact);
        TextView txtAddress=view.findViewById(R.id.txtAddress);
        LinearLayout llMinAmt=view.findViewById(R.id.llMinAmt);
        TextView txtUserId=view.findViewById(R.id.txtUserId);
        TextView txtPrice=view.findViewById(R.id.txtPrice);
        llOrders.setOnClickListener(v -> onClick.onDataClick(i));
        String image=BASE_URL+orderModel.getImage();
        Picasso.get().load(image).placeholder(R.drawable.user_placeholder).into(ivProfile);
        txtUserName.setText(orderModel.getPatient_name());
        txtUserId.setText(String.format("Order ID: %s", orderModel.getMedicine_order_id()));
        txtContact.setText(String.format("Contact: %s", orderModel.getPatient_phone()));
        txtAddress.setText(String.format("Address: %s", orderModel.getPatient_address()));
        switch (orderModel.getDelivery_status()){
            case "1":
                cardStatus.setCardBackgroundColor(ContextCompat.getColor(context,R.color.green));
                txtDeliveryAssign.setText("Delivery Assigned");
                break;

            case"3":
                cardStatus.setCardBackgroundColor(ContextCompat.getColor(context,R.color.darkpurple));
                txtDeliveryAssign.setText(context.getString(R.string.delivered));
                llMinAmt.setVisibility(View.VISIBLE);
                txtPrice.setText(MessageFormat.format("₹{0}", orderModel.getTotal_amount()));
                break;

            case"2":
                cardStatus.setCardBackgroundColor(ContextCompat.getColor(context,R.color.light_red));
                txtDeliveryAssign.setText(context.getString(R.string.out));
                break;

            case "0":
                cardStatus.setCardBackgroundColor(ContextCompat.getColor(context,R.color.light_red));
                txtDeliveryAssign.setText("Assign For Delivery");
                break;

        }

        llCCall.setOnClickListener(v -> onClick.onCallClick(i));




    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }
}
