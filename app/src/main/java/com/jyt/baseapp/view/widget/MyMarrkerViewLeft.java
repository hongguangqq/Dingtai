package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.jyt.baseapp.R;

import java.text.DecimalFormat;

/**
 * @author LinWei on 2017/9/15 00:43
 */
public class MyMarrkerViewLeft extends MarkerView {

    private TextView marker_tv;
    private DecimalFormat mFormat;
    private float num;
    private String stringnum;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarrkerViewLeft(Context context, int layoutResource) {
        super(context, layoutResource);
        mFormat=new DecimalFormat("#0.00");
        marker_tv= (TextView) findViewById(R.id.marker_tv);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (stringnum==null){
            marker_tv.setText(mFormat.format(num));
        }else {
            marker_tv.setText(stringnum);
        }
    }

    public void setData(float num){

        this.num=num;
    }

    public void  setData(String num){
        stringnum=num;
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
