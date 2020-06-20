package com.procter.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.procter.R;
import com.procter.fragment.AddExecutiveFragment;
import com.procter.fragment.EditExecutiveFragment;
import com.procter.model.UploadedImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ashish on 24/7/19.
 */

public class UploadDocsAdapter extends CommonRecyclerAdapter {

    private Activity activity;
    private ArrayList<UploadedImageModel> uploadedImageModels;
    private AddExecutiveFragment addExecutiveFragment;
    private EditExecutiveFragment editExecutiveFragment;

    public UploadDocsAdapter(FragmentActivity activity, ArrayList<UploadedImageModel> uploadedImageModels, AddExecutiveFragment addExecutiveFragment) {
        this.activity = activity;
        this.uploadedImageModels = uploadedImageModels;
        this.addExecutiveFragment = addExecutiveFragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonViewHolder(viewGroup, R.layout.layout_upload_docs_adapter) {
            @Override
            public void onItemSelected(int position) {

            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        View view = holder.getView();
        ImageView iv_itemImage = view.findViewById(R.id.ivAdd);
        RelativeLayout ivRemove = view.findViewById(R.id.rlRemoveImage);
        String uploadedFileName = uploadedImageModels.get(position).getImageFile().getName();
        if (uploadedFileName.contains(".pdf")) {
            iv_itemImage.setImageResource(R.drawable.ic_pdf);
        } else if (uploadedFileName.contains(".docx")) {
            iv_itemImage.setImageResource(R.drawable.ic_word);
        } else if (uploadedFileName.contains(".doc")) {
            iv_itemImage.setImageResource(R.drawable.ic_word);
        } else if (uploadedImageModels.get(position).getImageUrl().equals(""))
            iv_itemImage.setImageBitmap(uploadedImageModels.get(position).getImageBitmap());
        else
            Picasso.get().load(uploadedImageModels.get(position).getImageUrl()).into(iv_itemImage);

        ivRemove.setOnClickListener(view1 -> addExecutiveFragment.removeImage(position));
    }

    @Override
    public int getItemCount() {
        return uploadedImageModels.size();
    }
}
