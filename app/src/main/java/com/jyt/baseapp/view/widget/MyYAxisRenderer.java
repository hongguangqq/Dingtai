package com.jyt.baseapp.view.widget;

import android.graphics.Canvas;
import android.graphics.Color;

import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author LinWei on 2017/10/17 11:48
 */
public class MyYAxisRenderer extends YAxisRenderer {
    protected MyYAxis mYAxis;
    public MyYAxisRenderer(ViewPortHandler viewPortHandler, MyYAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
        mYAxis = yAxis;

    }

    @Override
    protected void computeAxisValues(float min, float max) {
//      /*折线图左边没有basevalue，则调用系统*/
        if (Float.isNaN(mYAxis.getBaseValue())) {
            super.computeAxisValues(min, max);
            return;
        }
        float base = mYAxis.getBaseValue();
        float yMin = min;
        int labelCount = mYAxis.getLabelCount();
//        Log.e("@#","yMin"+yMin);
        float interval = (base - yMin) / labelCount;
        int n = labelCount * 2 + 1;
        mYAxis.mEntryCount = n;
        mYAxis.mEntries = new float[n];
        int i;
        float f;
        for (f = min, i = 0; i < n; f += interval, i++) {
            mYAxis.mEntries[i] = f;
        }
    }

    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        for (int i = from; i < to; i++) {

            String text = mYAxis.getFormattedLabel(i);//Y轴竖直
            //如果需要大于0显示字体为红色的，小于0字体为绿色的话，需要设置isShares为true


            if ('-' == (text.charAt(0))) {
                mAxisLabelPaint.setColor(Color.GREEN);
            } else {
                mAxisLabelPaint.setColor(Color.RED);
            }
            if (text.equalsIgnoreCase("0.00%")) {
                mAxisLabelPaint.setColor(Color.WHITE);
            }

            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset+20, mAxisLabelPaint);
        }
    }

}
