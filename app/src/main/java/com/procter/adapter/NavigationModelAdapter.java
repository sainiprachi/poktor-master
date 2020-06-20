package com.procter.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.model.NavigationModel;

import java.util.ArrayList;

public class NavigationModelAdapter extends CommonRecyclerAdapter {
    private ArrayList<NavigationModel> navigationModelArrayList;
    private onNavClickListener onNavClick;


    public NavigationModelAdapter(ArrayList<NavigationModel> navigationModels,onNavClickListener onNavClick) {
        this.navigationModelArrayList = navigationModels;
        this.onNavClick=onNavClick;
    }

   public interface onNavClickListener{
        void onNavClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonViewHolder(viewGroup, R.layout.navigation_adapter) {
            @Override
            public void onItemSelected(int position) {

            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder viewHolder, int i) {
        View view = viewHolder.getView();
        ImageView ivImg = view.findViewById(R.id.ivImg);
        TextView txtHome = view.findViewById(R.id.txtHome);
        NavigationModel navigationModel = navigationModelArrayList.get(i);
        txtHome.setText(navigationModel.itemName);
        ivImg.setImageResource(navigationModel.itemImg);
        LinearLayout llLinear=view.findViewById(R.id.llLinear);
        llLinear.setOnClickListener(v -> onNavClick.onNavClick(i));

    }

    @Override
    public int getItemCount() {
        return navigationModelArrayList.size();
    }
}
