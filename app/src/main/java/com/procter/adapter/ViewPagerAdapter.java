package com.procter.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.procter.R;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;

    private int[] layouts = {R.layout.layout_one_viewpager, R.layout.layout_two_viewpager, R.layout.layout_three_viewpager};


    public ViewPagerAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(layouts[position], container, false);

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }


}
