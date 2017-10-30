package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.jyt.baseapp.bean.TimeData;

import java.util.List;

/**
 * @author LinWei on 2017/10/20 11:37
 */
public class MyBarChart extends BarChart {
    private MyMarrkerViewLeft mMarkerLeft;
    private MyMarrkerViewBottom mMarkerBottom;
    private List<TimeData> data;
    private List<String> time;
    public MyBarChart(Context context) {
        super(context);
    }

    public MyBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMarrkerView(MyMarrkerViewLeft left ,MyMarrkerViewBottom bottom, List<TimeData> data,List<String> time){
        this.mMarkerLeft =left;
        this.mMarkerBottom=bottom;
        this.data=data;
        this.time=time;
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (!mDrawMarkers || !valuesToHighlight())
            return;
        for (int i = 0; i < mIndicesToHighlight.length; i++) {
            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            Highlight highlight = mIndicesToHighlight[i];
            float[] pos = getMarkerPosition(highlight);
            //mIndicesToHighlight[i].getY();高亮左方的Y轴坐标，即要显示的内容
            //Y轴
            //            String value = (int)mIndicesToHighlight[i].getY() + "";
            String value =data.get((int) mIndicesToHighlight[i].getX()).vol;
            if (value != null && mMarkerLeft !=null) {
                mMarkerLeft.setData(value);//显示的内容
                mMarkerLeft.refreshContent(e, mIndicesToHighlight[i]);
                mMarkerLeft.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mMarkerLeft.layout(0, 0, mMarkerLeft.getMeasuredWidth(),
                        mMarkerLeft.getMeasuredHeight());
                mMarkerLeft.draw(canvas, mViewPortHandler.contentLeft(), pos[1] - mMarkerLeft.getHeight() / 2);
            }
            if (time!=null && mMarkerBottom!=null){
                String xdata=time.get((int) mIndicesToHighlight[i].getX()).split(" ")[1];
                mMarkerBottom.setData(xdata.substring(0,xdata.lastIndexOf(":")));
                mMarkerBottom.refreshContent(e,mIndicesToHighlight[i]);
                mMarkerBottom.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mMarkerBottom.layout(0, 0, mMarkerBottom.getMeasuredWidth(),
                        mMarkerBottom.getMeasuredHeight());
                mMarkerBottom.draw(canvas, pos[0]-mMarkerBottom.getWidth()/2, mViewPortHandler.contentBottom());
            }
        }
    }


}
