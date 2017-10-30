package com.jyt.baseapp.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author LinWei on 2017/9/25 14:50
 */
public class TabAdapter extends PagerAdapter {
    private List<String> title;
    private List<View> vp;
    public TabAdapter(List<String> title,List<View> vp){
        this.title=title;
        this.vp=vp;
    }
    @Override
    public int getCount() {
        return vp.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(vp.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(vp.get(position));
        return vp.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
