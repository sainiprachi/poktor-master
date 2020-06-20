package com.procter.adapter;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.model.OpenBidsModel;
import com.procter.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.procter.retrofit.ApiClient.BASE_URL;

public class OpenBidsAdapter extends RecyclerView.Adapter<OpenBidsAdapter.ViewHolder> {
    private static final String TAG = "OpenBidsAdapter";
    private ArrayList<OpenBidsModel.ResponseBean.DataBean> dataBeanArrayList;
    private Handler handler = new Handler();
    private Runnable runnable;
    private String EVENT_DATE_TIME = "2019-08-05 09:11:19";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private onClickListener onClickListener;
    CountDownTimer countDownTimer;


    public interface onClickListener{
        void getwatchlistClick(int position);
        void ondeleteClick(int position);
        void onItemClick(OpenBidsModel.ResponseBean.DataBean data);
    }

    public OpenBidsAdapter(ArrayList<OpenBidsModel.ResponseBean.DataBean> dataBeanArrayList,onClickListener onClickListener) {
        this.dataBeanArrayList = dataBeanArrayList;
        this.onClickListener=onClickListener;
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }


    private void calculateTime(OpenBidsModel.ResponseBean.DataBean orderDetails, TextView txtCountdownTimer) {
        try {
            Date createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(orderDetails.getExpire_time());
            if (createdAt.before(new Date())) {
                if ((30 * 60000) - getTimeDifference(new Date(), createdAt) > 0)
                    showTimer((30 * 60000) - getTimeDifference(new Date(), createdAt),txtCountdownTimer);
                else {
                    txtCountdownTimer.setVisibility(View.GONE);
                    //  min.setValue("00");
                    // sec.setValue("00");
                }
            } else {
                txtCountdownTimer.setVisibility(View.GONE);
                // min.setValue("00");
                //sec.setValue("00");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }



    public void removeItem(int position) {
        dataBeanArrayList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataBeanArrayList.size());
    }

    public void restoreItem(OpenBidsModel.ResponseBean.DataBean item, int position) {
        dataBeanArrayList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void floatingTime(long mill, TextView tvDeliveryTime,TextView tvRemaining){



        countDownTimer = new CountDownTimer(mill,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;

                tvDeliveryTime.setText(f.format(min) + ":" + f.format(sec));
                tvRemaining.setText("remaining");


            }

            @Override
            public void onFinish() {
                tvDeliveryTime.setText("");
                tvRemaining.setText("");
            }
        }.start();
    }

    private long getTimeDifference(Date startTime, Date endTime) {
        return startTime.getTime() - endTime.getTime();
    }

    private void showTimer(long duration, TextView txtCountdownTimer) {
        Log.d(TAG, "showTimer: " + duration);
        new CountDownTimer(duration, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long remainingSecs = millisUntilFinished / 1000;

                txtCountdownTimer.setText(MessageFormat.format("{0}:{1}\nremaining", String.format(Locale.getDefault(), "%02d", remainingSecs / 60), String.format(Locale.getDefault(), "%02d", (remainingSecs % 60))));



                // min.postValue(String.format(Locale.getDefault(), "%02d", remainingSecs / 60));
              //  sec.postValue(String.format(Locale.getDefault(), "%02d", (remainingSecs % 60)));

                // txtCountdownTimer.setText(String.format(Locale.getDefault(), "%02d", remainingSecs / 60));
                 //sec.postValue(String.format(Locale.getDefault(), "%02d", (remainingSecs % 60)));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.open_bids_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OpenBidsModel.ResponseBean.DataBean dataBean = dataBeanArrayList.get(position);

        holder.txtPatientName.setText(dataBean.getPatient_name());
        holder.txtOrderId.setText("Order ID: "+dataBean.getMedicine_order_id());
        holder.totalMrp.setText(String.format("%s", dataBean.getTotal_selling_price()));
        holder.txtDistance.setText(String.format("%sKm away", dataBean.getPatient_distance()));

        if (TextUtils.isEmpty(dataBean.getPharmacy_id())){
            holder.txtPharmacyPrice.setText(String.format("%s", dataBean.getTotal_selling_price()));
        }else {
            holder.txtPharmacyPrice.setText(String.format("%s", dataBean.getTotal_pharmacy_price()));
        }

        String image=BASE_URL+dataBean.getPatient_image();
        Picasso.get().load(image).placeholder(R.drawable.user_placeholder).into(holder.ivProfile);

//        ivDelete.setOnClickListener(v -> {
//            onClickListener.ondeleteClick(i);
//           // swipe.animateReset();
//        });
//        ivWatch.setOnClickListener(v -> {
//            Log.d(TAG, "onBindViewHolder: Delete=========");
//
//            onClickListener.getwatchlistClick(i);
//            //swipe.animateReset();
//        });

        holder.cvOpenBid.setOnClickListener(v -> {
            onClickListener.onItemClick(dataBeanArrayList.get(position));
        });


        floatingTime(Utils.floatingTimeInMillSeconds(Utils.getCurrentDateTime(),Utils.convertDatetoddMMyyy(dataBean.getExpire_time())),holder.txtCountdownTimer,holder.tvRemaining);

    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataBeanArrayList.size();
    }

    private void countDownStart(TextView txtCountdownTimer) {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (current_date.after(event_date)) {
                        long diff = current_date.getTime()-event_date.getTime() ;
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
//                        Log.d("timerr",""+Seconds);
                        if (Seconds>=30){
                            txtCountdownTimer .setText(MessageFormat.format("{0}\nremaininig", Seconds));
                        }else {
                            txtCountdownTimer.setVisibility(View.GONE);
                        }

                    } else {

                        handler.removeCallbacks(runnable);
                    }
                    handler.postDelayed(this, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPatientName ;
        CardView cvOpenBid;
        LinearLayout swipe ;
        TextView txtCountdownTimer;
        TextView txtOrderId ;
        TextView totalMrp ;
        TextView txtPharmacyPrice;
        TextView txtDistance ;
        TextView tvRemaining ;
        ImageView ivProfile;


        public ViewHolder(@NonNull View view) {
            super(view);
            txtPatientName = view.findViewById(R.id.txtPatientName);
            cvOpenBid = view.findViewById(R.id.cvOpenBid);
            swipe = view.findViewById(R.id.swipe);
            txtCountdownTimer = view.findViewById(R.id.txtCountdownTimer);
            txtOrderId = view.findViewById(R.id.txtOrderId);
            totalMrp = view.findViewById(R.id.totalMrp);
            txtPharmacyPrice = view.findViewById(R.id.txtPharmacyPrice);
            txtDistance = view.findViewById(R.id.txtDistance);
            ivProfile = view.findViewById(R.id.ivProfile);
            tvRemaining = view.findViewById(R.id.tvRemaininig);
        }
    }

   /* protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }*/
}
