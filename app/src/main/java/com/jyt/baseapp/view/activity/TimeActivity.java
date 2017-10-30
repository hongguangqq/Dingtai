package com.jyt.baseapp.view.activity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.OrderAdapter;
import com.jyt.baseapp.adapter.TabAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.OrderData;
import com.jyt.baseapp.bean.TimeBean;
import com.jyt.baseapp.bean.TimeData;
import com.jyt.baseapp.bean.TodayBean;
import com.jyt.baseapp.model.FenshiModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.ThreadManager;
import com.jyt.baseapp.view.widget.MyBarChart;
import com.jyt.baseapp.view.widget.MyLineChart;
import com.jyt.baseapp.view.widget.MyMarrkerViewBottom;
import com.jyt.baseapp.view.widget.MyMarrkerViewLeft;
import com.jyt.baseapp.view.widget.MyMarrkerViewRight;
import com.jyt.baseapp.view.widget.MyXFormatter;
import com.jyt.baseapp.view.widget.MyYAxis;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author LinWei on 2017/9/26 15:17
 */
public class TimeActivity extends BaseActivity {


    @BindView(R.id.vp_time)
    ViewPager mVp;
    @BindView(R.id.tv_time_goodsName)
    TextView tv_goodsName;
    @BindView(R.id.tl_time_tab)
    TabLayout mTabLayout;
    @BindView(R.id.chart_time_line)
    MyLineChart mLineChart;
    @BindView(R.id.chart_time_bar)
    MyBarChart mBarChart;
    @BindView(R.id.iv_time_back)
    ImageView iv_back;
    private FenshiModel mModel;
    private List<String> title;
    private List<View> tabList;
    private View tab_pen;
    private View tab_quotation;
    private String goodsCode;
    private String goodsId;
    private String goodsName;
    private XAxis mXLineAxis;
    private XAxis mXBarAxis;
    private MyYAxis mMLineYAxisRight;
    private MyYAxis mMLineYAxisLeft;
    private LineDataSet d1, d2;
    private ArrayList<OrderData> listData;
    private OrderAdapter mOrderAdapter;
    private ListView mLv_container;
    private MyXFormatter formatter;
    private TextView tv_time;//当前年月日时间
    private TextView tv_priceChangeRatio;//涨跌幅
    private TextView tv_totalPrice;//交易量
    private TextView tv_closePrice;//昨收市价
    private TextView tv_openPrice;//今日市价
    private TextView tv_showTopPrice;//今最高价
    private TextView tv_showFloorPrice;//今最低价
    private ChartTask mTask;
    private boolean isFirst=true;
    private int num;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_DATA_SUCCESS:
                    TimeBean bean= (TimeBean) msg.obj;
                    if (bean!=null){
                        mLineChart.clear();
                        mBarChart.clear();
                        ArrayList<TimeData> test=new ArrayList<>();
                        for (int i = 0; i < num; i++) {
                            test.add(bean.data.get(i));
                        }
                        if (bean.data.size()<18){
                            num=bean.data.size();
                        }

                        setLineData(bean.data);
                        setBarData(bean.data);
                        Lastinvalidate(bean);
                    }
                    break;
                case Const.STATE_TODAY_SUCCESS:
                    setTodayData((TodayBean) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };



    @Override
    protected int getLayoutId() {
        return R.layout.activity_time;
    }
    private TabAdapter mTabAdapter;

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsCode=getIntent().getStringExtra("goodsCode");
        goodsId=getIntent().getStringExtra("goodsId");
        goodsName=getIntent().getStringExtra("goodsName");
        tv_goodsName.setText("历史分时："+goodsName);
//        goodsCode="31802";
//        goodsId="266264a37daa11e79c5c1c1b0d7314c3";
        init();
        initVp();
        initLineChart();
        initBarChart();
        ThreadManager.getThreadPool().execute(mTask);
    }


    private void init() {
        mModel = new FenshiModel();
        title = new ArrayList<>();
        title.add("分笔");
        title.add("行情");
        tabList = new ArrayList<>();
        tab_pen = View.inflate(this, R.layout.tab_detail, null);
        tab_quotation = View.inflate(this, R.layout.tab_quotation, null);
        tabList.add(tab_pen);
        tabList.add(tab_quotation);
        mTabAdapter = new TabAdapter(title, tabList);
        mTask = new ChartTask();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initVp() {
        mLv_container = (ListView) tab_pen.findViewById(R.id.lv_tab_detail_container);
        tv_time= (TextView) tab_quotation.findViewById(R.id.tv_tab_time_creatTime);
        tv_openPrice= (TextView) tab_quotation.findViewById(R.id.tv_tab_time_openPrice);
        tv_closePrice= (TextView) tab_quotation.findViewById(R.id.tv_tab_time_closePrice);
        tv_showTopPrice= (TextView) tab_quotation.findViewById(R.id.tv_tab_time_showTopPrice);
        tv_showFloorPrice= (TextView) tab_quotation.findViewById(R.id.tv_tab_time_showFloorPrice);
        tv_totalPrice= (TextView) tab_quotation.findViewById(R.id.tv_tab_time_totalPrice);
        tv_priceChangeRatio= (TextView) tab_quotation.findViewById(R.id.tv_tab_time_priceChangeRatio);
        tv_time.setText(BaseUtil.currentTime("yyyy:MM:dd"));
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(title.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(title.get(1)));
        mVp.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mVp);
        mTabLayout.setTabsFromPagerAdapter(mTabAdapter);
        listData=new ArrayList<>();
        mOrderAdapter=new OrderAdapter(this,listData);
        mLv_container.setAdapter(mOrderAdapter);
    }

    private void initLineChart() {
        mLineChart.setScaleEnabled(false);
        mLineChart.setDrawBorders(true);
        mLineChart.setClipValuesToContent(true);
        mLineChart.setBorderWidth(1);
        mLineChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        mLineChart.setDescription(null);
        Legend lineChartLegend = mLineChart.getLegend();
        lineChartLegend.setEnabled(false);
        //X轴
        mXLineAxis = mLineChart.getXAxis();
        mXLineAxis.setDrawLabels(false);
        mXLineAxis.setDrawGridLines(false);
        mXLineAxis.setDrawAxisLine(false);
        //左Y轴
        mMLineYAxisLeft=mLineChart.getMyAxisLeft();
        mMLineYAxisLeft.setLabelCount(5, true);
        mMLineYAxisLeft.setDrawLabels(true);
        mMLineYAxisLeft.setDrawGridLines(false);
        //右Y轴
        mMLineYAxisRight=mLineChart.getMyAxisRight();
        mMLineYAxisRight.setLabelCount(2, true);
        mMLineYAxisRight.setBaseValue(0f);
        mMLineYAxisRight.setDrawLabels(true);
        mMLineYAxisRight.setStartAtZero(false);
        mMLineYAxisRight.setDrawGridLines(false);
        mMLineYAxisRight.setDrawAxisLine(false);
        mMLineYAxisRight.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return BaseUtil.getDecimalNum("#0.00%",value);
            }
        });
        //基准线
        LimitLine ll = new LimitLine(0);
        ll.setLineWidth(1f);
        ll.setLineColor(getResources().getColor(R.color.gray));
        ll.enableDashedLine(10f, 10f, 0f);
        mMLineYAxisRight.addLimitLine(ll);
        mMLineYAxisRight.setBaseValue(0);

        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mBarChart.highlightValue(h);
            }

            @Override
            public void onNothingSelected() {
                mBarChart.highlightValue(null);
            }
        });
    }

    private void initBarChart(){
        mBarChart.setScaleEnabled(false);
        mBarChart.setDrawBorders(true);
        mBarChart.setBorderWidth(1);
        mBarChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        mBarChart.setDescription(null);
        Legend lineChartLegend = mBarChart.getLegend();
        lineChartLegend.setEnabled(false);
        //X轴
        mXBarAxis = mBarChart.getXAxis();
        mXBarAxis.setDrawLabels(true);
        mXBarAxis.setDrawGridLines(false);
        mXBarAxis.setDrawAxisLine(false);
        mXBarAxis.setTextColor(Color.RED);
        mXBarAxis.setAvoidFirstLastClipping(true);
        mXBarAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //左Y轴
        YAxis yAxisLeft=mBarChart.getAxisLeft();
        yAxisLeft.setDrawLabels(true);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        //右Y轴
        YAxis yAxisRight=mBarChart.getAxisRight();
        yAxisRight.setDrawLabels(true);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);

        mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mLineChart.highlightValue(h);
            }

            @Override
            public void onNothingSelected() {
                mLineChart.highlightValue(null);
            }
        });
    }

    private boolean IsNoConnect;
    /**
     * 数据获取线程
     */
    private class ChartTask implements Runnable{

        @Override
        public void run() {
            while (!IsNoConnect){
                mModel.getFenshiData(TimeActivity.this, goodsCode, goodsId, new FenshiModel.ResultFenshiListener() {
                    @Override
                    public void ResultData(boolean isSuccess, TimeBean data) {
                        if (isSuccess){
                            Message message=new Message();
                            message.what=Const.STATE_DATA_SUCCESS;
                            message.obj=data;
                            mHandler.sendMessage(message);
                        }
                    }
                });

                mModel.getTodayData(TimeActivity.this, goodsId, "22", new FenshiModel.ResultTodayListener() {
                    @Override
                    public void ResultData(boolean isSuccess, TodayBean data) {
                        if (isSuccess){
                            Message message=new Message();
                            message.what= Const.STATE_TODAY_SUCCESS;
                            message.obj=data;
                            mHandler.sendMessage(message);
                        }
                    }
                });
                SystemClock.sleep(1000);
            }
        }
    }




    private void setLineData(List<TimeData> data){
        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            lineCJEntries.add(new Entry(i,Float.valueOf(data.get(i).close)));
            lineJJEntries.add(new Entry(i,Float.valueOf(data.get(i).totalPrice)/Float.valueOf(data.get(i).vol)));
        }
        d1 = new LineDataSet(lineCJEntries, "成交价");
        d2 = new LineDataSet(lineJJEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        d1.setDrawCircles(false);
        d2.setDrawCircles(false);
        d1.setColor(getResources().getColor(R.color.skyblue));
        d2.setColor(getResources().getColor(R.color.yellow));
        d1.setHighLightColor(Color.WHITE);
        d2.setHighlightEnabled(false);
        d1.setDrawFilled(true);
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        d1.setCubicIntensity(0.2f);
        d2.setCubicIntensity(0.2f);
        sets.add(d1);
        sets.add(d2);
        LineData lineData=new LineData(sets);
        mLineChart.setData(lineData);
    }

    private void setBarData(List<TimeData> data){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            barEntries.add(new BarEntry(i,Float.valueOf(data.get(i).vol)));
        }

        BarDataSet barDataSet=new BarDataSet(barEntries,"");
        barDataSet.setValueTextSize(0f);
        barDataSet.setHighLightColor(Color.WHITE);
        barDataSet.setBarBorderWidth(0.2f);
        barDataSet.setFormLineWidth(0.5f);
        barDataSet.setColor(Color.RED);
        barDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        mBarChart.setData(new BarData(barDataSet));

        String[] time=new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            time[i]=data.get(i).time.split(" ")[1];
        }
        formatter=new MyXFormatter(time);
        if (data.size()<15){
            mXBarAxis.setLabelOnlyOne(true);
            mXBarAxis.setLabelCount(1,true);
        }else {
            mXBarAxis.setLabelOnlyOne(false);
            mXBarAxis.setLabelCount(5,true);
        }
        mXBarAxis.setValueFormatter(formatter);
    }

    private void setTodayData(TodayBean bean){
        mOrderAdapter.setData(bean.orderList);
        mOrderAdapter.notifyDataSetChanged();
        tv_openPrice.setText(bean.openPrice);
        tv_closePrice.setText(bean.closePrice);
        tv_showTopPrice.setText(bean.showTopPrice);
        tv_showFloorPrice.setText(bean.showFloorPrice);
        tv_totalPrice.setText(BaseUtil.getDecimalNum("#0.00",Float.valueOf(bean.totalPrice)/100000000)+"");
        tv_priceChangeRatio.setText(BaseUtil.getDecimalNum("#0.00%",Float.valueOf(bean.priceChangeRatio)));
    }

    private void Lastinvalidate(TimeBean bean){
        mMLineYAxisLeft.setAxisMaximum(bean.getMax());
        mMLineYAxisLeft.setAxisMinimum(bean.getMin());
        mMLineYAxisRight.setAxisMaximum(bean.getPercentMax());
        mMLineYAxisRight.setAxisMinimum(bean.getPercentMin());
        MyMarrkerViewLeft lineleft=new MyMarrkerViewLeft(TimeActivity.this,R.layout.mymarkerview);
        MyMarrkerViewRight lineright=new MyMarrkerViewRight(TimeActivity.this,R.layout.mymarkerview);
        mLineChart.setMarker(lineleft,lineright,bean);



        MyMarrkerViewLeft Barleft=new MyMarrkerViewLeft(TimeActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom BarBottom=new MyMarrkerViewBottom(TimeActivity.this,R.layout.mymarkerview);
        mBarChart.setMarrkerView(Barleft,BarBottom,bean.data,bean.getTime());




        if (bean.data.size()<18){
            mLineChart.getViewPortHandler().setMaximumScaleX(culcMaxscale(10));
            mBarChart.getViewPortHandler().setMaximumScaleX(culcMaxscale(10));
            mXLineAxis.setAxisMaximum(18);
            mXBarAxis.setAxisMaximum(18);
        }else {
            mLineChart.getViewPortHandler().setMaximumScaleX(culcMaxscale(bean.data.size()/2));
            mBarChart.getViewPortHandler().setMaximumScaleX(culcMaxscale(bean.data.size()/2));
        }



        mXBarAxis.setAxisMinimum(-0.2f);
        mXLineAxis.setAxisMinimum(-0.2f);

        if (isFirst){
            setOffset();
            isFirst=false;
        }
        if (i!=1){
            i++;
        }else {
            mLineChart.setVisibility(View.VISIBLE);
            mBarChart.setVisibility(View.VISIBLE);
        }
        mLineChart.performClick();
        mBarChart.performClick();
        mLineChart.invalidate();
        mBarChart.invalidate();



    }
    private int i=0;

    private float culcMaxscale(float count) {
        float max = 1;
        max = count / 127 * 5;
        return max;
    }

    /*设置量表对齐*/
    private void setOffset() {
        float lineTop = mLineChart.getViewPortHandler().offsetTop();
        float lineLeft = mLineChart.getViewPortHandler().offsetLeft();
        float barLeft = mBarChart.getViewPortHandler().offsetLeft();
        float lineRight = mLineChart.getViewPortHandler().offsetRight();
        float barRight = mBarChart.getViewPortHandler().offsetRight();
        float lineBottom = mLineChart.getViewPortHandler().offsetBottom();
        float barBottom = mBarChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
//            offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
//            mBarChart.setExtraLeftOffset(offsetLeft);
            transLeft = lineLeft;

        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            mLineChart.setExtraLeftOffset(offsetLeft-2);
            transLeft = barLeft;
        }
/*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/

        if (barRight < lineRight) {
//            offsetRight = Utils.convertPixelsToDp(lineRight- getTextWidth()*1.5f);
            offsetRight = Utils.convertPixelsToDp(lineRight-barRight);
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            mLineChart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        mLineChart.setViewPortOffsets(transLeft, lineTop, transRight-16, lineBottom);
        mBarChart.setViewPortOffsets(transLeft, 5, transRight-16, barBottom);

    }

    private float getTextWidth(){
        Paint paint= new Paint();
        Rect bounds=new Rect();
        paint.getTextBounds("0.69%",0,"0.69%".length(),bounds);
        return bounds.width();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask!=null){
            IsNoConnect=true;
            ThreadManager.getThreadPool().cancel(mTask);
        }
    }
}
