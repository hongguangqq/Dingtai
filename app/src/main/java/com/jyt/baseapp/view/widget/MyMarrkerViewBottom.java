package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.jyt.baseapp.R;

/**
 * @author LinWei on 2017/9/15 00:43
 */
public class MyMarrkerViewBottom extends MarkerView {

    private TextView marker_tv;
    private String time;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarrkerViewBottom(Context context, int layoutResource) {
        super(context, layoutResource);
        marker_tv= (TextView) findViewById(R.id.marker_tv);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        marker_tv.setText(time);
    }

    public void setData(String time){
        this.time=time;
    }

    @Override
    public float getTranslationY() {
        return -(getWidth() / 2);
    }

    @Override
    public float getTranslationX() {
        return -getHeight();
    }
}
