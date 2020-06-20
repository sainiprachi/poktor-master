package com.procter.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.model.faq.DataItem;

import java.util.List;

public class FaqAdapter extends CommonRecyclerAdapter {

    Context context;
    private List<DataItem> data;

    public FaqAdapter(FragmentActivity activity, List<DataItem> data) {
        this.context =activity;
        this.data =data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommonViewHolder(parent, R.layout.rv_faq_item_layout) {
            @Override
            public void onItemSelected(int position) {

            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {

        View view = holder.getView();
        TextView textView = view.findViewById(R.id.tvFaq);
        ImageView imageView = view.findViewById(R.id.ivImage);
        RecyclerView recyclerView = view.findViewById(R.id.rvSecondList);


        DataItem dataItem = data.get(position);

        textView.setText(dataItem.getQuestion());


        view.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
             if (dataItem.isClick()){
                 dataItem.setClick(false);
                 recyclerView.setVisibility(View.GONE);
                 imageView.setImageDrawable(context.getDrawable(R.drawable.arrowleft));

             }else {
                 dataItem.setClick(true);
                 recyclerView.setVisibility(View.VISIBLE);
                 FaqAdapter2 faqAdapter2 = new FaqAdapter2(context,dataItem.getAnswers());
                 recyclerView.setAdapter(faqAdapter2);
                 faqAdapter2.notifyDataSetChanged();
                 imageView.setImageDrawable(context.getDrawable(R.drawable.arrowdown));

             }


            }
        });

        view.setOnClickListener(v -> {
            if (dataItem.isClick()){
                dataItem.setClick(false);
                notifyDataSetChanged();
            }else {
                for (int j = 0; j < data.size(); j++) {
                    data.get(j).setClick(false);
                }
                dataItem.setClick(true);
                notifyDataSetChanged();

            }

        });


        if (dataItem.isClick()){
            recyclerView.setVisibility(View.VISIBLE);
            FaqAdapter2 faqAdapter2 = new FaqAdapter2(context,dataItem.getAnswers());
            recyclerView.setAdapter(faqAdapter2);
            faqAdapter2.notifyDataSetChanged();
            imageView.setImageResource(R.drawable.arrowdown);

        }else {
            recyclerView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.arrowleft);

        }
      /*  rlExpand.setOnClickListener(v -> {
            if (dataBean.isSelect()){
                dataBean.setSelect(false);
                notifyDataSetChanged();
            }else {
                for (int j = 0; j < deliveryList.size(); j++) {
                    deliveryList.get(j).setSelect(false);
                }
                dataBean.setSelect(true);
                notifyDataSetChanged();

            }*/






        }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
