package com.jyt.baseapp.view.widget;

import android.graphics.Color;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * @author LinWei on 2017/9/19 15:12
 */
public class MyXFormatter implements IAxisValueFormatter {
    private String[] mValues;

    public MyXFormatter(String[] values) {
        this.mValues = values;
    }


    public void setValues(String[] values){
        this.mValues=values;
    }

    public String[] getValues(){
        return this.mValues;
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        axis.setTextColor(Color.RED);
        return mValues[(int) value % mValues.length];
    }
}
