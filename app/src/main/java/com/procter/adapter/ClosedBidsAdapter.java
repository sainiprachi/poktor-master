package com.procter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.model.closed.ClosedItem;
import com.procter.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClosedBidsAdapter extends RecyclerView.Adapter<ClosedBidsAdapter.MyViewHolder> {
    private static final String TAG = "ClosedBidsAdapter";

    @BindView(R.id.ivProfile)
    ImageView ivProfile;

    Context context;
    List<ClosedItem> closedItemList;
    String userId;

    private onClick onClick;


    public interface onClick{
        void position(int position);
    }

    public ClosedBidsAdapter(Context context, List<ClosedItem> closedItemList, String userId, onClick onClick) {
        this.context = context;
        this.closedItemList = closedItemList;
        this.userId = userId;
        this.onClick =onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.closed_bid_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String imageUrl = closedItemList.get(position).getPatientImage();
        if (imageUrl != null && imageUrl.length() > 0) {
            Picasso.get().load(imageUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_face_big)).into(holder.civProfile);
        }
        Log.d(TAG, "onBindViewHolder: "+closedItemList.get(position).getPatientName());
        holder.tvName.setText(closedItemList.get(position).getPatientName());
        holder.tvOrderId.setText(closedItemList.get(position).getMedicineOrderId());
        holder.tvMRPPrice.setText(closedItemList.get(position).getTotalSellingPrice());
        holder.tvClosedPrice.setText(closedItemList.get(position).getTotalPharmacyPrice());
        if (userId.equals(closedItemList.get(position).getPharmacyId())) {
            holder.tvStatus.setText(context.getResources().getString(R.string.won));
            holder.llStatusBtn.setCardBackgroundColor(context.getResources().getColor(R.color.green));
        }else {
            holder.tvStatus.setText(context.getResources().getString(R.string.lost));
            holder.llStatusBtn.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.tvOrderedDate.setText(Utils.FormatDatemdMMyy(closedItemList.get(position).getOrdered_date()));
        holder.tvDistance.setText(closedItemList.get(position).getDistance());


        holder.ll.setOnClickListener(v -> {
            onClick.position(position);
            //click.onChildClick(closedItemList.get(position).getMedicineOrderId());
        });
    }

    @Override
    public int getItemCount() {
        return closedItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civProfile)
        CircleImageView civProfile;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvOrderedDate)
        TextView tvOrderedDate;
        @BindView(R.id.tvOrderId)
        TextView tvOrderId;
        @BindView(R.id.tvDistance)
        TextView tvDistance;
        @BindView(R.id.llTime)
        LinearLayout llTime;
        @BindView(R.id.tvMRPPrice)
        TextView tvMRPPrice;
        @BindView(R.id.tvClosedPrice)
        TextView tvClosedPrice;
        @BindView(R.id.llMinAmt)
        LinearLayout llMinAmt;
        @BindView(R.id.llCurrent)
        LinearLayout llCurrent;
        @BindView(R.id.llStatusBtn)
        CardView llStatusBtn;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.ll)
        LinearLayout ll;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
