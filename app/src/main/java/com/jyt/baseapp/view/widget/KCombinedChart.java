package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.jyt.baseapp.bean.KChartData;

import java.util.List;

/**
 * @author LinWei on 2017/10/9 14:08
 */
public class KCombinedChart  extends CombinedChart{
    private MyMarrkerViewLeft mMarkerLeft;
    private MyMarrkerViewBottom mMarkerBottom;
    private List<KChartData> data;
    private List<String> ydata;
    private List<String> xdata;
    private int state=0;


    public KCombinedChart(Context context) {
        super(context);
    }

    public KCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        getRendererLeftYAxis().setShares(true);
    }

    public void setMarrkerView(MyMarrkerViewLeft left , List<String> ydata){
        this.mMarkerLeft =left;
        this.ydata=ydata;
    }

    public void setMarrkerView(MyMarrkerViewLeft left ,MyMarrkerViewBottom bottom, List<String> ydata,List<String> xdata){
        this.mMarkerLeft =left;
        this.mMarkerBottom=bottom;
        this.ydata=ydata;
        this.xdata=xdata;

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
            String yvalue =null;
            if (ydata==null){
                //为空时，直接取高度
                yvalue = mIndicesToHighlight[i].getY()+"";
            }else {
                yvalue=ydata.get((int) highlight.getX());
                //数值大于亿，需要做处理
                float leftfValue=Float.valueOf(yvalue);
                if (leftfValue>100000000){
                    leftfValue=leftfValue/100000000;
                    yvalue=leftfValue+"亿";
                }
            }
            if (mMarkerLeft !=null) {
                mMarkerLeft.setData(yvalue);//显示的内容
                mMarkerLeft.refreshContent(e, mIndicesToHighlight[i]);
                mMarkerLeft.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mMarkerLeft.layout(0, 0, mMarkerLeft.getMeasuredWidth(),
                        mMarkerLeft.getMeasuredHeight());
                mMarkerLeft.draw(canvas, mViewPortHandler.contentLeft(), pos[1] - mMarkerLeft.getHeight() / 2);
            }
            //X轴

            if (xdata!=null && mMarkerBottom!=null){
                String xvalue=xdata.get((int)highlight.getX());
                mMarkerBottom.setData(xvalue);
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
