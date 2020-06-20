package com.procter.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExecutiveImageAdapter extends CommonRecyclerAdapter {
    private ArrayList<String>arrayList;
    private onPdfClick onPdfClick;


    ExecutiveImageAdapter(ArrayList<String> arrayList,onPdfClick onPdfClick){
      this.arrayList=arrayList;
      this.onPdfClick=onPdfClick;
    }

    public interface onPdfClick{
        void onClick(int position);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonViewHolder(viewGroup, R.layout.exectutive_image_adapter) {
            @Override
            public void onItemSelected(int position) {
              onPdfClick.onClick(position);
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder viewHolder, int i) {
        View view=viewHolder.getView();
        ImageView ivUserImage=view.findViewById(R.id.ivUserImage);
        if (arrayList.get(i).contains(".pdf")){
            ivUserImage.setImageResource(R.drawable.ic_pdf);
        }else {
            Picasso.get().load(arrayList.get(i)).fit().into(ivUserImage);
        }

        CardView card_griditem=view.findViewById(R.id.card_griditem);
        card_griditem.setOnClickListener(v -> onPdfClick.onClick(i));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
