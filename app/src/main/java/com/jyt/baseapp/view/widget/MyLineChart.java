package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.jyt.baseapp.bean.TimeBean;

/**
 * @author LinWei on 2017/10/17 11:44
 */
public class MyLineChart extends LineChart {
    private MyMarrkerViewLeft mMarrkerViewLeft;
    private MyMarrkerViewRight mMarrkerViewRight;
    private TimeBean bean;
    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();


        mAxisRight = new MyYAxis(YAxis.AxisDependency.RIGHT);
        mAxisRendererRight = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisRight, mRightAxisTransformer);

        mAxisLeft = new MyYAxis(YAxis.AxisDependency.LEFT);
        mAxisRendererLeft = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisLeft, mLeftAxisTransformer);

    }

    public MyYAxis getMyAxisLeft(){
        return (MyYAxis) mAxisLeft;
    }

    public MyYAxis getMyAxisRight(){
        return (MyYAxis) mAxisRight;
    }

    public void setMarker(MyMarrkerViewLeft markerLeft,MyMarrkerViewRight markerRight,TimeBean bean){
        mMarrkerViewLeft=markerLeft;
        mMarrkerViewRight=markerRight;
        this.bean=bean;

    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (!mDrawMarkers || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            Highlight highlight = mIndicesToHighlight[i];
            //            String data= (String) e.getData();

            float[] pos = getMarkerPosition(highlight);
            //mIndicesToHighlight[i].getY();高亮左方的Y轴坐标，即要显示的内容
            if (mMarrkerViewLeft!=null){
                //左Y轴
                mMarrkerViewLeft.setData(Float.valueOf(bean.data.get((int) mIndicesToHighlight[i].getX()).close));//显示的内容
                mMarrkerViewLeft.refreshContent(e,mIndicesToHighlight[i]);
                mMarrkerViewLeft.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mMarrkerViewLeft.layout(0, 0, mMarrkerViewLeft.getMeasuredWidth(),
                        mMarrkerViewLeft.getMeasuredHeight());
                mMarrkerViewLeft.draw(canvas,mViewPortHandler.contentLeft(), pos[1] - mMarrkerViewLeft.getHeight() / 2);

            }
            if (mMarrkerViewRight!=null){
                //右Y轴
                float num=(Float.valueOf(bean.data.get((int) mIndicesToHighlight[i].getX()).close)-bean.centerNum())/bean.centerNum();
//                Log.e("@#","num="+centerNum);
                mMarrkerViewRight.setData(num);//显示的内容
                mMarrkerViewRight.refreshContent(e,mIndicesToHighlight[i]);
                mMarrkerViewRight.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mMarrkerViewRight.layout(0, 0, mMarrkerViewLeft.getMeasuredWidth(),
                        mMarrkerViewRight.getMeasuredHeight());
                mMarrkerViewRight.draw(canvas,mViewPortHandler.contentRight(), pos[1] - mMarrkerViewLeft.getHeight() / 2);
            }



        }

    }

}

