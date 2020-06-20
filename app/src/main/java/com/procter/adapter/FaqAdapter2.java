package com.procter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;

import java.util.List;

public class FaqAdapter2 extends CommonRecyclerAdapter {

    Context context;
    private List<String> answers;

    public FaqAdapter2(Context context, List<String> answers) {
        this.context =context;
        this.answers =answers;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommonViewHolder(parent, R.layout.rv_item_second_layout) {
            @Override
            public void onItemSelected(int position) {

            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {

        View view = holder.getView();
        TextView textView = view.findViewById(R.id.tvFaq);
        textView.setText(answers.get(position));



    }

    @Override
    public int getItemCount() {
        return answers.size();
    }
}
