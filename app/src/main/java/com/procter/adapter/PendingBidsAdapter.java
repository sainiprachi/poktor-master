package com.procter.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.model.pending.PendingItem;
import com.procter.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PendingBidsAdapter extends RecyclerView.Adapter<PendingBidsAdapter.MyViewHolder> {

    private Context context;
    private List<PendingItem> pendingItemList;
    private String userId;
    Click click;

    public PendingBidsAdapter(Context context, List<PendingItem> pendingItemList, String userId,Click click) {
        this.context = context;
        this.pendingItemList = pendingItemList;
        this.userId = userId;
        this.click =click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_bid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String imageUrl = pendingItemList.get(position).getPatientImage();
        if (imageUrl != null && imageUrl.length() > 0) {
            Picasso.get().load(imageUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_face_big)).into(holder.civProfile);
        }
        holder.tvOrderId.setText(context.getResources().getString(R.string.order_id, pendingItemList.get(position).getMedicineOrderId()));
        holder.tvName.setText(pendingItemList.get(position).getPatientName());
//        holder.tvDateTime.setText(pendingItemList.get(position).getDistance());

        holder.tvMRPPrice.setText(pendingItemList.get(position).getTotalSellingPrice());
        holder.tvCurrentPrice.setText(pendingItemList.get(position).getTotalPharmacyPrice());

        if (userId.equals(pendingItemList.get(position).getPharmacyId())){
            holder.tvMyBid.setVisibility(View.VISIBLE);
            holder.tvRemainingTime.setVisibility(View.GONE);
            holder.tvRemainingLabel.setVisibility(View.GONE);
            holder.cvRemainingAmount.setVisibility(View.GONE);
            holder.tvMyAmount.setText(pendingItemList.get(position).getTotalPharmacyPrice());
        }else {
            holder.tvMyBid.setVisibility(View.GONE);
            holder.tvRemainingTime.setVisibility(View.VISIBLE);
            holder.tvRemainingLabel.setVisibility(View.VISIBLE);
            holder.tvMyAmount.setVisibility(View.GONE);
            holder.llMyAmount.setVisibility(View.GONE);

            holder.cvRemainingAmount.setVisibility(View.VISIBLE);
        }

        holder.tvOtherPharmacyAmount.setText(pendingItemList.get(position).getBidPrice());
        holder.tvDateTime.setText(Utils.FormatDatemdMMyy(pendingItemList.get(position).getOrdered_date()));



        floatingTime(Utils.floatingTimeInMillSeconds(Utils.getCurrentDateTime(),Utils.convertDatetoddMMyyy(pendingItemList.get(position).getExpire_time())),holder.tvRemainingTime,holder.tvRemainingLabel);

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onChildClick(pendingItemList.get(position).getId());
            }
        });


    }

    public void floatingTime(long mill, TextView tvDeliveryTime,TextView tvRemaining){


        new CountDownTimer(mill,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvDeliveryTime.setText((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)) + ":"
                        + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));

                tvRemaining.setText("remaining");

            }

            @Override
            public void onFinish() {
                tvDeliveryTime.setText("");
                tvRemaining.setText("");
            }
        }.start();
    }

    @Override
    public int getItemCount() {
        return pendingItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civProfile)
        CircleImageView civProfile;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvOrderId)
        TextView tvOrderId;
        @BindView(R.id.tvDateTime)
        TextView tvDateTime;
        @BindView(R.id.llTime)
        LinearLayout llTime;
        @BindView(R.id.tvMRPPrice)
        TextView tvMRPPrice;
        @BindView(R.id.tvMyBid)
        TextView tvMyBid;
        @BindView(R.id.tvRemainingTime)
        TextView tvRemainingTime;
        @BindView(R.id.tvRemainingLabel)
        TextView tvRemainingLabel;
        @BindView(R.id.tvCurrentPrice)
        TextView tvCurrentPrice;
        @BindView(R.id.tvMyAmount)
        TextView tvMyAmount;
        @BindView(R.id.cvRemainingAmount)
        CardView cvRemainingAmount;
        @BindView(R.id.tvOtherPharmacyAmount)
        TextView tvOtherPharmacyAmount;
        @BindView(R.id.llMinAmt)
        LinearLayout llMinAmt;
        @BindView(R.id.llCurrent)
        LinearLayout llCurrent;
        @BindView(R.id.ll)
        LinearLayout ll;


        @BindView(R.id.llMyAmount)
        LinearLayout llMyAmount;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface Click{

        void onChildClick(String medicineOrderId);
    }

}
