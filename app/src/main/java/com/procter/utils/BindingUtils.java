package com.procter.utils;

import android.webkit.WebView;

import androidx.databinding.BindingAdapter;

public class BindingUtils {

    @BindingAdapter("webViewData")
    public static void loadDataToWebView(WebView view, String webViewData){
        view.getSettings().setJavaScriptEnabled(true);
        view.clearHistory();
        view.clearFormData();
        if (webViewData != null && !webViewData.equals("")) {
            view.loadData(webViewData, "text/html", "UTF-8");
        }
    }
}
