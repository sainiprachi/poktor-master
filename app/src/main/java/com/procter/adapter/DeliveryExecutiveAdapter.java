package com.procter.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.procter.R;
import com.procter.model.DeliveryExecutiveModel;
import com.procter.utils.CusDialogProg;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.procter.ui.authentications.forgetpass.ForgetPasswordFragment.TAG;

public class DeliveryExecutiveAdapter extends CommonRecyclerAdapter {
    private ArrayList<DeliveryExecutiveModel.DataBean> deliveryList;
    private Context context;
    private onClickListener onClickListener;




    public DeliveryExecutiveAdapter(Context context, ArrayList<DeliveryExecutiveModel.DataBean>
            deliveryList, onClickListener onClickListener) {
        this.context = context;
        this.deliveryList = deliveryList;
        this.onClickListener = onClickListener;
    }

    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonViewHolder(viewGroup, R.layout.parent_executive_list) {
            @Override
            public void onItemSelected(int position) {


            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder viewHolder, final int i) {
        DeliveryExecutiveModel.DataBean dataBean = deliveryList.get(i);
        View view = viewHolder.getView();
        final ImageView ivCollapse = view.findViewById(R.id.ivCollapse);
        final ImageView ivLocation = view.findViewById(R.id.ivLocation);
        final ImageView ivEdit = view.findViewById(R.id.ivEdit);
        final TextView txtName = view.findViewById(R.id.txtName);
        final CardView cardCall = view.findViewById(R.id.cardCall);
        final RelativeLayout rlExpand = view.findViewById(R.id.rlExpand);
        final TextView txtId = view.findViewById(R.id.txtId);
        final TextView txtContactNu = view.findViewById(R.id.txtContactNu);
        final TextView txtAddress = view.findViewById(R.id.txtAddress);
        final TextView txtEmail = view.findViewById(R.id.txtEmail);
        final TextView txtVehiclenumber = view.findViewById(R.id.txtVehiclenumber);
        final CircleImageView ivUserImage = view.findViewById(R.id.ivUserImage);
        final LinearLayout llChildView = view.findViewById(R.id.llChildView);
        String driving_license;
        String insurance_proof;
        String registration_certificate;
        ArrayList<String> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(dataBean.getDriving_license())) {
            driving_license = "http://52.200.8.36/poktor/" + dataBean.getDriving_license();
            arrayList.add(driving_license);

        }
        if (!TextUtils.isEmpty(dataBean.getOwner_id())) {
            insurance_proof = "http://52.200.8.36/poktor/" + dataBean.getOwner_id();
            arrayList.add(insurance_proof);
        }
        if ((!TextUtils.isEmpty(dataBean.getOther_document()))) {
            registration_certificate = "http://52.200.8.36/poktor/" + dataBean.getOther_document();
            arrayList.add(registration_certificate);
        }

        cardCall.setOnClickListener(v -> onClickListener.onCallClick(i));
        ivEdit.setOnClickListener(v -> onClickListener.onEditClick(i));
        ivLocation.setOnClickListener(v -> onClickListener.onLocationClick(i));


        RecyclerView recyclerExec = view.findViewById(R.id.recyclerExec);
        recyclerExec.setAdapter(new ExecutiveImageAdapter(arrayList, position -> {
           String url=arrayList.get(position);
            showDialog(url);
          /* if (url.contains(".pdf")){

           }*/
        }));

        recyclerExec.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        ivCollapse.setOnClickListener(v -> {
            if (dataBean.isSelect()) {
                dataBean.setSelect(false);
                notifyDataSetChanged();
            } else {
                for (int j = 0; j < deliveryList.size(); j++) {
                    deliveryList.get(j).setSelect(false);
                }
                dataBean.setSelect(true);
                notifyDataSetChanged();

            }

        });


        if (dataBean.isSelect()) {
            ivCollapse.setVisibility(View.VISIBLE);
            llChildView.setVisibility(View.VISIBLE);
            ivCollapse.setImageResource(R.drawable.arrowdown);
        } else {
            llChildView.setVisibility(View.GONE);
            ivCollapse.setVisibility(View.GONE);
          //  ivCollapse.setImageResource(R.drawable.arrowleft);

        }
        rlExpand.setOnClickListener(v -> {
            if (dataBean.isSelect()) {
                dataBean.setSelect(false);
                notifyDataSetChanged();
            } else {
                for (int j = 0; j < deliveryList.size(); j++) {
                    deliveryList.get(j).setSelect(false);
                }
                dataBean.setSelect(true);
                notifyDataSetChanged();

            }


        });


        String url = "http://52.200.8.36/poktor/" + dataBean.getImage();
        Picasso.get().load(url).error(R.drawable.user_image).into(ivUserImage);
        txtName.setText(dataBean.getName());
        txtAddress.setText(dataBean.getAddress());
        txtId.setText(String.format("ID: %s", dataBean.getId()));
        txtContactNu.setText(String.format("Contact: %s", dataBean.getPhone()));
        txtEmail.setText(String.format("Email: %s", dataBean.getEmail()));
        txtAddress.setText(String.format("%s", dataBean.getAddress()));
        txtVehiclenumber.setText(dataBean.getVehicle_number());
        ImageView ivDelete = view.findViewById(R.id.ivDelete);
        ivDelete.setOnClickListener(v -> onClickListener.onDeleteClick(i));


    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public interface onClickListener {
        void onDeleteClick(int position);
        void onCallClick(int position);
        void onEditClick(int position);
        void onLocationClick(int position);

    }


    private void showDialog(String myPdfUrl){
        final Dialog bottomSheetDialog = new Dialog(context,R.style.full_screen_dialog);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.show_webview);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        PDFView webview=bottomSheetDialog.findViewById(R.id.webview);
        webview.requestFocus();
        CusDialogProg cusDialogProg=new CusDialogProg(context);
        PhotoView ivImage=bottomSheetDialog.findViewById(R.id.ivImage);
        if (!myPdfUrl.contains(".pdf")){
         ivImage.setVisibility(View.VISIBLE);
         webview.setVisibility(View.GONE);
         Picasso.get().load(myPdfUrl).into(ivImage);

        }else {

            ivImage.setVisibility(View.GONE);
            String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
            FileLoader.with(context)
                    .load(myPdfUrl)
                    .fromDirectory("PDFFiles", FileLoader.DIR_EXTERNAL_PUBLIC)
                    .asFile(new FileRequestListener<File>() {
                        @Override
                        public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                            Log.d(TAG, "Successfully loaded");
                            webview.setVisibility(View.VISIBLE);
                            webview.fromFile(response.getBody())
                                    .enableSwipe(false) // allows to block changing pages using swipe
                                    .swipeHorizontal(false)
                                    .enableDoubletap(true)
                                    .defaultPage(0)
                                    .onError(t -> {

                                    })
                                    .onPageError((page, t) -> {

                                    })
                                    .spacing(0)
                                    .load();
                        }

                        @Override
                        public void onError(FileLoadRequest request, Throwable t) {

                        }
                    });

        }







           /* webview.getSettings().setJavaScriptEnabled(true);
            webview.loadUrl(url);

            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100) {
                        cusDialogProg.show();
                    }
                    if (progress == 100) {
                        cusDialogProg.dismiss();
                    }
                }
            });

        }*/
        ImageView ivImgBack=bottomSheetDialog.findViewById(R.id.ivImgBack);
        ivImgBack.setOnClickListener(v -> bottomSheetDialog.dismiss());





        lp.copyFrom(Objects.requireNonNull(bottomSheetDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        bottomSheetDialog.getWindow().setAttributes(lp);
        bottomSheetDialog.show();
    }
}
