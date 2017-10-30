package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.OrderAdapter;
import com.jyt.baseapp.adapter.TabAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.CoupleChartGestureListener;
import com.jyt.baseapp.bean.KChartBean;
import com.jyt.baseapp.bean.KChartData;
import com.jyt.baseapp.bean.OrderData;
import com.jyt.baseapp.bean.TodayBean;
import com.jyt.baseapp.model.ChartModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.CalcStockUtil;
import com.jyt.baseapp.util.ThreadManager;
import com.jyt.baseapp.view.widget.KCombinedChart;
import com.jyt.baseapp.view.widget.MyMarrkerViewBottom;
import com.jyt.baseapp.view.widget.MyMarrkerViewLeft;
import com.jyt.baseapp.view.widget.MyXFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class QuotationActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tl_quotation_tab)
    TabLayout mTabLayout;
    @BindView(R.id.vp_quotation)
    ViewPager mVp;
    @BindView(R.id.chart_k_combined)
    KCombinedChart combinedchart;
    @BindView(R.id.chart_k_bottom)
    KCombinedChart bottomchart;
    @BindView(R.id.tv_quotation_goodsName)
    TextView tv_goodsName;
    @BindView(R.id.tv_k_time)
    TextView tv_starTime;
    @BindView(R.id.tv_k_m1)
    TextView tv_m1;
    @BindView(R.id.tv_k_m5)
    TextView tv_m5;
    @BindView(R.id.tv_k_m15)
    TextView tv_m15;
    @BindView(R.id.tv_k_m30)
    TextView tv_m30;
    @BindView(R.id.tv_k_d1)
    TextView tv_d1;
    @BindView(R.id.tv_k_d5)
    TextView tv_d5;
    @BindView(R.id.tv_k_d20)
    TextView tv_d20;
    @BindView(R.id.tv_k_d60)
    TextView tv_d60;
    @BindView(R.id.tv_k_d250)
    TextView tv_d250;
    @BindView(R.id.tv_k_macd)
    TextView tv_macd;
    @BindView(R.id.tv_k_trix)
    TextView tv_trix;
    @BindView(R.id.tv_k_brar)
    TextView tv_brar;
    @BindView(R.id.tv_k_cr)
    TextView tv_cr;
    @BindView(R.id.tv_k_vr)
    TextView tv_vr;
    @BindView(R.id.tv_k_obv)
    TextView tv_obv;
    @BindView(R.id.tv_k_emv)
    TextView tv_emv;
    @BindView(R.id.tv_k_vol)
    TextView tv_vol;
    @BindView(R.id.tv_k_rsi)
    TextView tv_rsi;
    @BindView(R.id.tv_k_kdj)
    TextView tv_kdj;
    @BindView(R.id.tv_k_cci)
    TextView tv_cci;
    @BindView(R.id.tv_k_mtm)
    TextView tv_mtm;
    @BindView(R.id.tv_k_psy)
    TextView tv_psy;
    @BindView(R.id.iv_k_back)
    ImageView iv_back;
    private String goodsId;
    private String goodsName;
    private String goodsCode;
    private boolean isMove =true; //是否要移动后最后一条数据
    private boolean isDay=true;   //是否仅仅访问当天数据
    private boolean isFirst=false;
    private boolean isToLink;//所有按键都会触发一次，之后根据 mf重新赋值，用于是否联网的操作
    private int Mode=7;
    private int sum;
    private String mf="2";//2为分钟 1为日
    private int stateTime=1;  //数据转换的节点
    private ChartModel mModel;
    private List<String> tabTitle;
    private List<View> tabList;
    private View tab_info;//商品信息
    private View tab_detail;//交易明细
    private TabAdapter mTabAdapter;
    private XAxis xAxis;
    private XAxis bAxis;
    private YAxis axisLeft;
    private YAxis axisRight;
    private YAxis baxisRight;
    private MyXFormatter formatter;
    private KChartBean mDatas;
    /*-----------------------------------------Tab------------------------------------------------*/
    private TextView tv_tprice5;
    private TextView tv_tnum5;
    private TextView tv_tprice4;
    private TextView tv_tnum4;
    private TextView tv_tprice3;
    private TextView tv_tnum3;
    private TextView tv_tprice2;
    private TextView tv_tnum2;
    private TextView tv_tprice1;
    private TextView tv_tnum1;
    private TextView tv_oprice1;
    private TextView tv_onum1;
    private TextView tv_oprice2;
    private TextView tv_onum2;
    private TextView tv_oprice3;
    private TextView tv_onum3;
    private TextView tv_oprice4;
    private TextView tv_onum4;
    private TextView tv_oprice5;
    private TextView tv_onum5;
    private TextView tv_price;//最新价
    private TextView tv_priceChangeRatio;//涨跌幅
    private TextView tv_inventory;//持仓量
    private TextView tv_totalPrice;//交易量
    private TextView tv_closePrice;//昨收市价
    private TextView tv_openPrice;//今日市价
    private TextView tv_showTopPrice;//今最高价
    private TextView tv_showFloorPrice;//今最低价
    private TextView tv_turnoverRate;//换手率
    private TextView tv_inflows;//净宗入额
    private TextView tv_bigInflows;//大宗流入

    private List<OrderData> listData;
    private ListView lv_container;
    private OrderAdapter ordderAdapter;
    /*--------------------------------------------------------------------------------------------*/
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_DATA_SUCCESS:
                    mDatas= (KChartBean) msg.obj;
                    if(mDatas.getK()!=null || mDatas.getK().size()>0){
                        combinedchart.setVisibility(View.VISIBLE);
                        combinedchart.clear();
                        bottomchart.setVisibility(View.VISIBLE);
                        bottomchart.clear();
                        KChartBean bean=mStockUtil.transformTime(mDatas,stateTime);//转换后的数据
                        String[] time=new String[bean.getK().size()];
                        for (int i=0;i<bean.getK().size();i++){
                            if (isDay){
                                time[i]=bean.getK().get(i).getTimeD();
                            }else {
                                time[i]=bean.getK().get(i).getTimeY();
                            }
                        }
                        drawK(bean,time);
                        switch (Mode) {
                            case 0:
                                drawMacd(bean,time);
                                break;
                            case 1:
                                drawTRIX(bean,time);
                                break;
                            case 2:
                                drawBRAR(bean,time);
                                break;
                            case 3:
                                drawCR(bean,time);
                                break;
                            case 4:
                                drawVR(bean,time);
                                break;
                            case 5:
                                drawOBV(bean,time);
                                break;
                            case 6:
                                drawEMV(bean,time);
                                break;
                            case 7:
                                drawVol(bean,time);
                                break;
                            case 8:
                                drawRSI(bean,time);
                                break;
                            case 9:
                                drawKDJ(bean,time);
                                break;
                            case 10:
                                drawCCI(bean,time);
                                break;
                            case 11:
                                drawMTM(bean,time);
                                break;
                            case 12:
                                drawPSY(bean,time);
                                break;
                            default:
                                break;
                        }


                        Lastinvalidate(bean);

                    }
                    break;
                case Const.STATE_TODAY_SUCCESS:

                    setTodayData((TodayBean) msg.obj);
                    break;
            }
        }
    };
    private ChartTask mTask;
    private TimeTask timeTask;
    private CalcStockUtil mStockUtil;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_quotation;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        tv_m1.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
        initVp();
        initTab();
        initCombinedChart();
        initBottomChart();
        initListener();
        ThreadManager.getThreadPool().execute(mTask);
        timeTask.start();


    }

    private void init() {
        mStockUtil = new CalcStockUtil();
        goodsId=getIntent().getStringExtra("goodsId");
        goodsName=getIntent().getStringExtra("goodsName");
        goodsCode=getIntent().getStringExtra("goodsCode");
        Log.e("@#",goodsId+"--"+goodsName+"--"+goodsCode);
        tv_goodsName.setText(goodsName);
        mf="2";
        mTask = new ChartTask();
        timeTask=new TimeTask();
        mModel = new ChartModel();
        tabTitle=new ArrayList<>();
        tabTitle.add("商品信息");
        tabTitle.add("交易明细");


        tabList=new ArrayList<>();
        tab_info=View.inflate(this,R.layout.tab_iteminfo,null);
        tab_detail=View.inflate(this,R.layout.tab_detail,null);
        tabList.add(tab_info);
        tabList.add(tab_detail);
        mTabAdapter = new TabAdapter(tabTitle,tabList);

        tv_vol.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
    }



    private void initVp() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitle.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitle.get(1)));
        mVp.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mVp);
        mTabLayout.setTabsFromPagerAdapter(mTabAdapter);
    }

    private void initTab(){
        //商品信息
        tv_tprice1= (TextView) tab_info.findViewById(R.id.tv_tab_info_tprice1);
        tv_tprice2= (TextView) tab_info.findViewById(R.id.tv_tab_info_tprice2);
        tv_tprice3= (TextView) tab_info.findViewById(R.id.tv_tab_info_tprice3);
        tv_tprice4= (TextView) tab_info.findViewById(R.id.tv_tab_info_tprice4);
        tv_tprice5= (TextView) tab_info.findViewById(R.id.tv_tab_info_tprice5);
        tv_tnum1= (TextView) tab_info.findViewById(R.id.tv_tab_info_tnum1);
        tv_tnum2= (TextView) tab_info.findViewById(R.id.tv_tab_info_tnum2);
        tv_tnum3= (TextView) tab_info.findViewById(R.id.tv_tab_info_tnum3);
        tv_tnum4= (TextView) tab_info.findViewById(R.id.tv_tab_info_tnum4);
        tv_tnum5= (TextView) tab_info.findViewById(R.id.tv_tab_info_tnum5);

        tv_oprice1= (TextView) tab_info.findViewById(R.id.tv_tab_info_oprice1);
        tv_oprice2= (TextView) tab_info.findViewById(R.id.tv_tab_info_oprice2);
        tv_oprice3= (TextView) tab_info.findViewById(R.id.tv_tab_info_oprice3);
        tv_oprice4= (TextView) tab_info.findViewById(R.id.tv_tab_info_oprice4);
        tv_oprice5= (TextView) tab_info.findViewById(R.id.tv_tab_info_oprice5);
        tv_onum1= (TextView) tab_info.findViewById(R.id.tv_tab_info_onum1);
        tv_onum2= (TextView) tab_info.findViewById(R.id.tv_tab_info_onum2);
        tv_onum3= (TextView) tab_info.findViewById(R.id.tv_tab_info_onum3);
        tv_onum4= (TextView) tab_info.findViewById(R.id.tv_tab_info_onum4);
        tv_onum5= (TextView) tab_info.findViewById(R.id.tv_tab_info_onum5);

        tv_price= (TextView) tab_info.findViewById(R.id.tv_tab_info_price);
        tv_priceChangeRatio= (TextView) tab_info.findViewById(R.id.tv_tab_info_priceChangeRatio);
        tv_inventory= (TextView) tab_info.findViewById(R.id.tv_tab_info_inventory);
        tv_totalPrice= (TextView) tab_info.findViewById(R.id.tv_tab_info_totalPrice);
        tv_closePrice= (TextView) tab_info.findViewById(R.id.tv_tab_info_closePrice);
        tv_openPrice= (TextView) tab_info.findViewById(R.id.tv_tab_info_openPrice);
        tv_showTopPrice= (TextView) tab_info.findViewById(R.id.tv_tab_info_showTopPrice);
        tv_showFloorPrice= (TextView) tab_info.findViewById(R.id.tv_tab_info_showFloorPrice);
        tv_turnoverRate= (TextView) tab_info.findViewById(R.id.tv_tab_info_turnoverRate);
        tv_inflows= (TextView) tab_info.findViewById(R.id.tv_tab_info_inflows);
        tv_bigInflows= (TextView) tab_info.findViewById(R.id.tv_tab_info_bigInflows);
        //交易明细
        lv_container= (ListView) tab_detail.findViewById(R.id.lv_tab_detail_container);
        listData=new ArrayList<>();
        ordderAdapter=new OrderAdapter(this,listData);
        lv_container.setAdapter(ordderAdapter);

    }

    private void initCombinedChart(){
        combinedchart.setDrawBorders(true);
        combinedchart.setBorderWidth(0.5f);
        combinedchart.setDragDecelerationEnabled(false);
        combinedchart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        combinedchart.setDescription(null);
        combinedchart.setDragEnabled(true);
        combinedchart.setScaleYEnabled(false);
        combinedchart.setAutoScaleMinMaxEnabled(false);
        Legend ll= combinedchart.getLegend();
        ll.setEnabled(false);
        ll.setDrawInside(false);
        //X轴
        xAxis=combinedchart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //左Y轴
        axisLeft=combinedchart.getAxisLeft();
        axisLeft.setDrawLabels(true);
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        //右Y轴
        axisRight=combinedchart.getAxisRight();
        axisRight.setDrawLabels(false);
        axisRight.setDrawGridLines(true);
        axisRight.setDrawAxisLine(false);
        axisRight.enableGridDashedLine(10f, 10f, 0f);

        combinedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                bottomchart.highlightValue(h);
            }

            @Override
            public void onNothingSelected() {
                bottomchart.highlightValue(null);
            }
        });
        combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart,new Chart[]{bottomchart}));


    }

    private void initBottomChart(){
        bottomchart.setDrawBorders(true);
        bottomchart.setBorderWidth(0.5f);
        bottomchart.setDragDecelerationEnabled(false);
        bottomchart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        bottomchart.setDescription(null);
        bottomchart.setDragEnabled(true);
        bottomchart.setScaleYEnabled(false);
        bottomchart.setAutoScaleMinMaxEnabled(false);
        Legend ll= bottomchart.getLegend();
        ll.setEnabled(false);
        ll.setDrawInside(false);
        //X轴
        bAxis=bottomchart.getXAxis();
        bAxis.setDrawLabels(true);
        bAxis.setDrawAxisLine(true);
        bAxis.setDrawGridLines(false);
        bAxis.setAvoidFirstLastClipping(true);
        bAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //左Y轴
        YAxis axisLeft=bottomchart.getAxisLeft();
        axisLeft.setDrawLabels(true);
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        //右Y轴
        baxisRight=bottomchart.getAxisRight();
        baxisRight.setDrawLabels(false);
        baxisRight.setDrawGridLines(false);
        baxisRight.setDrawAxisLine(false);

        bottomchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                combinedchart.highlightValue(h);
            }

            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
            }
        });
        bottomchart.setOnChartGestureListener(new CoupleChartGestureListener(bottomchart,new Chart[]{combinedchart}));
    }



    private void initListener(){
        tv_starTime.setOnClickListener(this);
        tv_m1.setOnClickListener(this);
        tv_m5.setOnClickListener(this);
        tv_m15.setOnClickListener(this);
        tv_m30.setOnClickListener(this);
        tv_d1.setOnClickListener(this);
        tv_d5.setOnClickListener(this);
        tv_d20.setOnClickListener(this);
        tv_d60.setOnClickListener(this);
        tv_d250.setOnClickListener(this);

        tv_macd.setOnClickListener(this);
        tv_trix.setOnClickListener(this);
        tv_brar.setOnClickListener(this);
        tv_cr.setOnClickListener(this);
        tv_vr.setOnClickListener(this);
        tv_obv.setOnClickListener(this);
        tv_emv.setOnClickListener(this);
        tv_vol.setOnClickListener(this);
        tv_rsi.setOnClickListener(this);
        tv_kdj.setOnClickListener(this);
        tv_cci.setOnClickListener(this);
        tv_mtm.setOnClickListener(this);
        tv_psy.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    /**
     * 获取图表信息
     * @param goodsID
     * @param type
     */
    private void initCharData(String goodsID,String type){
        mModel.getKData(this, goodsID, type, new ChartModel.ResultKListener() {

            @Override
            public void ResultData(boolean isSuccess, KChartBean data) {
                if (isSuccess){
                    Message message=new Message();
                    message.what= Const.STATE_DATA_SUCCESS;
                    message.obj=data;
                    mHandler.sendMessage(message);

                }
            }
        });
    }

    /**
     * 获取行情当日定理/转让详情
     * @param count
     */
    private void initTodayData(String count){
        mModel.getTodayData(this, goodsId, count, new ChartModel.ResultTodayListener() {
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
    }

    private void setTodayData(TodayBean data){
        if (data!=null){
            tv_tprice1.setText(data.transferFive.get(0).price);
            tv_tprice2.setText(data.transferFive.get(1).price);
            tv_tprice3.setText(data.transferFive.get(2).price);
            tv_tprice4.setText(data.transferFive.get(3).price);
            tv_tprice5.setText(data.transferFive.get(4).price);
            tv_tnum1.setText(data.transferFive.get(0).num);
            tv_tnum2.setText(data.transferFive.get(1).num);
            tv_tnum3.setText(data.transferFive.get(2).num);
            tv_tnum4.setText(data.transferFive.get(3).num);
            tv_tnum5.setText(data.transferFive.get(4).num);

            tv_oprice1.setText(data.orderFive.get(0).price);
            tv_oprice2.setText(data.orderFive.get(1).price);
            tv_oprice3.setText(data.orderFive.get(2).price);
            tv_oprice4.setText(data.orderFive.get(3).price);
            tv_oprice5.setText(data.orderFive.get(4).price);
            tv_onum1.setText(data.orderFive.get(0).num);
            tv_onum2.setText(data.orderFive.get(1).num);
            tv_onum3.setText(data.orderFive.get(2).num);
            tv_onum4.setText(data.orderFive.get(3).num);
            tv_onum5.setText(data.orderFive.get(4).num);

//            String num_priceChangeRatio=data.priceChangeRatio.substring(0,4)+"%";
            int num_totalPrice=(int)(Float.valueOf(data.totalPrice)/100000000);
            int num_inflows=(int)(Float.valueOf(data.inflows)/100000000);
            int num_bigInflows=(int)(Float.valueOf(data.bigInflows)/100000000);
            int num_inventory=(int)(Float.valueOf(data.inventory)/100000000);
            tv_price.setText(data.price);
            tv_priceChangeRatio.setText(BaseUtil.getDecimalNum("#0.00%",Float.valueOf(data.priceChangeRatio)));
            tv_inventory.setText(num_inventory+"亿");
            tv_totalPrice.setText(num_totalPrice+"亿");
            tv_closePrice.setText(data.closePrice);
            tv_openPrice.setText(data.openPrice);
            tv_showTopPrice.setText(data.showTopPrice);
            tv_showFloorPrice.setText(data.showFloorPrice);
            tv_turnoverRate.setText(data.turnoverRate);
            tv_inflows.setText(num_inflows+"亿");
            tv_bigInflows.setText(num_bigInflows+"亿");

            ordderAdapter.setData(data.orderList);
            ordderAdapter.notifyDataSetChanged();

        }
    }

    private boolean IsNoConnect;
    /**
     * 数据获取线程
     */
    private class ChartTask implements Runnable{

        @Override
        public void run() {
            while (!IsNoConnect){
                if (isRefresh){
                    if (!isToLink){
                        if (mf.equals("1")){
                            isDay=false;
                            isToLink =true;
                        }else {
                            isDay=true;
                            isToLink =false;
                        }
                        initCharData(goodsId,mf);
                    }
                    initTodayData("20");
                    isRefresh=false;
                }
            }
        }
    }
    private boolean isRefresh=true;
    private class TimeTask extends Thread{
        @Override
        public void run() {
            super.run();
            while (!IsNoConnect){
                isRefresh=true;
                SystemClock.sleep(2000);
            }
        }
    }


    /**
     * 整理K图数据
     * @param datas
     * @return
     */
    private ArrayList<CandleEntry> createDayCandleData(List<KChartData> datas){
        ArrayList<CandleEntry> candleEntries = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            candleEntries.add(new CandleEntry(i,
                    Float.valueOf(datas.get(i).high),
                    Float.valueOf(datas.get(i).low),
                    Float.valueOf(datas.get(i).open),
                    Float.valueOf(datas.get(i).close)));
        }
        xAxis.setAxisMaximum(candleEntries.size()-0.3f);
        xAxis.setAxisMinimum(-0.4f);
        return candleEntries;
    }

    /**
     * 整理MA线数据
     * @param datas
     * @param line5Entries
     * @param line10Entries
     * @param line20Entries
     * @param line60Entries
     */
    private  void  createDayLine(List<KChartData> datas ,
                                 ArrayList<Entry> line1Entries ,
                                 ArrayList<Entry> line5Entries ,
                                 ArrayList<Entry> line10Entries ,
                                 ArrayList<Entry> line20Entries ,
                                 ArrayList<Entry> line60Entries){

        for (int i = 0; i < datas.size(); i++) {
            if (i>=0){
                line1Entries.add(new Entry( i,Float.valueOf(datas.get(i).close)));
            }
            if (i >= 4) {
                sum = 0;
                line5Entries.add(new Entry( i,getSum(i - 4, i, datas) / 5));
            }
            if (i >= 9) {
                sum = 0;
                line10Entries.add(new Entry( i,getSum(i - 9, i, datas) / 10));
            }
            if (i >= 19) {
                sum = 0;
                line20Entries.add(new Entry( i,getSum(i - 19, i, datas) / 20));
            }
            if (i>59) {
                sum=0;
                line60Entries.add(new Entry( i,getSum(i - 59, i, datas) / 60));
            }
        }

    }

    /**
     * 下方图Vol整合
     * @param datas
     * @return
     */
    private ArrayList<BarEntry> createDayBottom(List<KChartData> datas){
        ArrayList<BarEntry> bottomEntry = new ArrayList<BarEntry>();
        for (int i = 0; i < datas.size(); i++) {
//            float vol=Float.valueOf(datas.get(i).vol);
//            if (vol>100000000){
//                vol=vol/100000000;
//            }
//            bottomEntry.add(new BarEntry(i,vol));
            bottomEntry.add(new BarEntry(i,Float.valueOf(datas.get(i).vol)));

        }
        int size=bottomEntry.size();
        if (size<5){
            bAxis.setLabelCount(size);

        }else {
            bAxis.setLabelCount(5);
            baxisRight.setLabelCount(5);
        }
        bAxis.setAxisMaximum(bottomEntry.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        return bottomEntry;
    }



    /**
     * 生成K图
     * @param candleEntries
     * @return
     */
    public CandleData getCandleData(ArrayList<CandleEntry> candleEntries){
        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "");
        candleDataSet.setFormSize(0f);
        candleDataSet.setHighLightColor(Color.WHITE);
        candleDataSet.setHighlightEnabled(false);
        candleDataSet.setDrawValues(false);
        candleDataSet.setColor(Color.RED);
        candleDataSet.setBarSpace(0.1f);
        candleDataSet.setDecreasingColor(Color.rgb(142, 150, 175));
        candleDataSet.setValueTextSize(0f);
        candleDataSet.setShadowColorSameAsCandle(true);
        candleDataSet.setDecreasingColor(Color.GREEN);
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);//红涨，实体
        candleDataSet.setIncreasingColor(Color.RED);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.STROKE);//绿跌，空心
        candleDataSet.setNeutralColor(Color.RED);//当天价格不涨不跌（一字线）颜色
        CandleData candleData = new CandleData(candleDataSet);
        return candleData;
    }

    /**
     * MA线的生成
     * @param ma
     * @param xVals
     * @param lineEntries
     * @return
     */
    private LineDataSet setMaLine(int ma, String[] xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        lineDataSetMa.setHighLightColor(Color.WHITE);
        if (ma == 1) {
            lineDataSetMa.setHighlightEnabled(true);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(true);
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setDrawValues(false);
        if (ma==1){
            lineDataSetMa.setColor(getResources().getColor(R.color.transparent));
        }else if (ma == 5) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma5));
        } else if (ma == 10) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma10));
        } else if (ma==20){
            lineDataSetMa.setColor(getResources().getColor(R.color.ma20));
        }else {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma60));
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        //        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }

    /**
     * 下方图的生成
     * @param bottomEntry
     * @return
     */
    public BarData getBottomVolData(ArrayList<BarEntry> bottomEntry){
        BarDataSet barDataSet=new BarDataSet(bottomEntry,"");
        barDataSet.setColor(Color.RED);
        barDataSet.setHighLightColor(Color.WHITE);
        barDataSet.setValueTextSize(0);
        barDataSet.setBarBorderColor(R.color.black);

        barDataSet.setBarShadowColor(R.color.black);
        barDataSet.setBarBorderWidth(0.2f);
        BarData barData = new BarData(barDataSet);
        return barData;
    }




    /**
     * 计算MA线的均值
     * @param a
     * @param b
     * @param datas
     * @return
     */
    private float getSum(Integer a, Integer b,List<KChartData> datas) {

        for (int i = a; i <= b; i++) {
            sum += Float.valueOf(datas.get(i).close);
        }
        return sum;
    }



    private float culcMaxscale(float count) {
        float max = 1;
        max = count / 127 * 5;
        return max;
    }
    private float num;
    private void drawK(KChartBean bean , String[] time){
        /*------------------------------combinerchart---------------------------------------------*/
        CombinedData combinedData=new CombinedData();
        ArrayList<Entry> line1Entries = new ArrayList<>();
        ArrayList<Entry> line5Entries = new ArrayList<>();
        ArrayList<Entry> line10Entries = new ArrayList<>();
        ArrayList<Entry> line20Entries = new ArrayList<>();
        ArrayList<Entry> line60Entries = new ArrayList<>();
        //K图
        CandleData candleData=getCandleData(createDayCandleData(bean.getK()));
        //MA线
        createDayLine(bean.getK(),line1Entries,line5Entries,line10Entries,line20Entries,line60Entries);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        int size=bean.getK().size();
        sets.add(setMaLine(1,time,line1Entries));
        if (size>60){
            sets.add(setMaLine(5,time,line5Entries));
            sets.add(setMaLine(10,time,line10Entries));
            sets.add(setMaLine(20,time,line20Entries));
            sets.add(setMaLine(60,time,line60Entries));
        }else if (size>=20 && size<60){
            sets.add(setMaLine(5,time,line5Entries));
            sets.add(setMaLine(10,time,line10Entries));
            sets.add(setMaLine(20,time,line20Entries));
        }else if (size>=10 && size<20){
            sets.add(setMaLine(5,time,line5Entries));
            sets.add(setMaLine(10,time,line10Entries));
        }else if (size>5 && size<10){
            sets.add(setMaLine(5,time,line5Entries));
        }
        LineData lineData = new LineData(sets);
        combinedData.setData(candleData);
        combinedData.setData(lineData);
        combinedchart.setData(combinedData);
        //设置Mark

        MyMarrkerViewLeft right=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            ydata.add(bean.getK().get(i).close);
        }
        combinedchart.setMarrkerView(right,ydata);
//        bottomchart.setExtraRightOffset(num);//对齐设置    10000/-12  100/-20



    }

    public void drawVol(KChartBean bean , String[] time){
        CombinedData bottomData=new CombinedData();
        ArrayList<BarEntry> bottomEntry =createDayBottom(bean.getK());
        bottomData.setData(getBottomVolData(bottomEntry));
        bottomchart.setData(bottomData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            ydata.add(bean.getK().get(i).vol);
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }


    public void drawMacd(KChartBean bean , String[] time){
        List<Entry> DIFF=new ArrayList<>();
        List<Entry> DEA=new ArrayList<>();
        List<Entry> LINE=new ArrayList<>();
        List<BarEntry> BAR=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcMACD(bean.getK(),LINE,DIFF,DEA,BAR);
        //LINE
        LineDataSet lineLINE = new LineDataSet(LINE, "LINE");
        lineLINE.setHighLightColor(Color.WHITE);
        lineLINE.setHighlightEnabled(true);
        lineLINE.setDrawHorizontalHighlightIndicator(false);
        lineLINE.setColor(getResources().getColor(R.color.transparent));
        lineLINE.setLineWidth(0.5f);
        lineLINE.setValueTextSize(0f);
        lineLINE.setDrawCircles(false);
        lineSets.add(lineLINE);
        //DIFF
        LineDataSet lineDIFF = new LineDataSet(DIFF, "DIFF");
        lineDIFF.setHighLightColor(Color.WHITE);
        lineDIFF.setHighlightEnabled(false);
        lineDIFF.setDrawHorizontalHighlightIndicator(false);
        lineDIFF.setColor(getResources().getColor(R.color.ma5));
        lineDIFF.setLineWidth(0.5f);
        lineDIFF.setValueTextSize(0f);
        lineDIFF.setDrawCircles(false);
        lineSets.add(lineDIFF);
        //DEA
        LineDataSet lineDEA = new LineDataSet(DEA, "DIFF");
        lineDEA.setHighLightColor(Color.WHITE);
        lineDEA.setHighlightEnabled(false);
        lineDEA.setDrawHorizontalHighlightIndicator(false);
        lineDEA.setColor(getResources().getColor(R.color.ma10));
        lineDEA.setLineWidth(0.5f);
        lineDEA.setValueTextSize(0f);
        lineDEA.setDrawCircles(false);
        lineSets.add(lineDEA);
        //BAR
        BarDataSet barDataSet=new BarDataSet(BAR,"BAR");
        barDataSet.setValueTextSize(0f);
        barDataSet.setColor(Color.RED);
        barDataSet.setHighlightEnabled(false);
        barDataSet.setHighLightColor(Color.WHITE);

        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        combineData.setData(new BarData(barDataSet));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(BAR.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.00",LINE.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }

    public void drawTRIX(KChartBean bean , String[] time){
        List<Entry> TRIX=new ArrayList<>();
        List<Entry> MATRIX=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcTRIX(bean.getK(),TRIX,MATRIX);
        //TRIX
        if (TRIX.size()>0){
            LineDataSet lineTRIX = new LineDataSet(TRIX, "TRIX");
            lineTRIX.setHighLightColor(Color.WHITE);
            lineTRIX.setHighlightEnabled(true);
            lineTRIX.setDrawHorizontalHighlightIndicator(false);
            lineTRIX.setColor(getResources().getColor(R.color.ma5));
            lineTRIX.setLineWidth(0.5f);
            lineTRIX.setValueTextSize(0f);
            lineTRIX.setDrawCircles(false);
            lineSets.add(lineTRIX);
        }
        //MATRIX
        if (MATRIX.size()>0){
            LineDataSet lineMATRIX = new LineDataSet(MATRIX, "MATRIX");
            lineMATRIX.setHighLightColor(Color.WHITE);
            lineMATRIX.setHighlightEnabled(false);
            lineMATRIX.setDrawHorizontalHighlightIndicator(false);
            lineMATRIX.setColor(getResources().getColor(R.color.ma10));
            lineMATRIX.setLineWidth(0.5f);
            lineMATRIX.setValueTextSize(0f);
            lineMATRIX.setDrawCircles(false);
            lineSets.add(lineMATRIX);
        }
        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(TRIX.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < TRIX.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.000",TRIX.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }

    public void drawBRAR(KChartBean bean , String[] time){
        List<Entry> BRAR=new ArrayList<>();
        List<Entry> MABRAR=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcBRAR(bean.getK(),BRAR,MABRAR);
        //BRAR
        LineDataSet lineBRAR = new LineDataSet(BRAR, "BRAR");
        lineBRAR.setHighLightColor(Color.WHITE);
        lineBRAR.setHighlightEnabled(true);
        lineBRAR.setDrawHorizontalHighlightIndicator(false);
        lineBRAR.setColor(getResources().getColor(R.color.ma5));
        lineBRAR.setLineWidth(0.5f);
        lineBRAR.setValueTextSize(0f);
        lineBRAR.setDrawCircles(false);
        lineSets.add(lineBRAR);
        //MABRAR
        if (MABRAR.size()>0){
            LineDataSet lineMABRAR = new LineDataSet(MABRAR, "MABRAR");
            lineMABRAR.setHighLightColor(Color.WHITE);
            lineMABRAR.setHighlightEnabled(false);
            lineMABRAR.setDrawHorizontalHighlightIndicator(false);
            lineMABRAR.setColor(getResources().getColor(R.color.ma10));
            lineMABRAR.setLineWidth(0.5f);
            lineMABRAR.setValueTextSize(0f);
            lineMABRAR.setDrawCircles(false);
            lineSets.add(lineMABRAR);
        }
        Log.e("#",""+BRAR.size());
        Log.e("#",""+MABRAR.size());
        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));

        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(BRAR.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < BRAR.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.000",BRAR.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }

    public void drawCR(KChartBean bean , String[] time){
        List<Entry> CR=new ArrayList<>();
        List<Entry> MA1=new ArrayList<>();
        List<Entry> MA2=new ArrayList<>();
        List<Entry> MA3=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcCR(bean.getK(),CR,MA1,MA2,MA3);
        //CR
        LineDataSet lineCR = new LineDataSet(CR, "CR");
        lineCR.setHighLightColor(Color.WHITE);
        lineCR.setHighlightEnabled(true);
        lineCR.setDrawHorizontalHighlightIndicator(false);
        lineCR.setColor(getResources().getColor(R.color.ma5));
        lineCR.setLineWidth(0.5f);
        lineCR.setValueTextSize(0f);
        lineCR.setDrawCircles(false);
        lineSets.add(lineCR);
        if (MA1.size()>0){
            LineDataSet lineMA1 = new LineDataSet(MA1, "MA1");
            lineMA1.setHighLightColor(Color.WHITE);
            lineMA1.setHighlightEnabled(false);
            lineMA1.setDrawHorizontalHighlightIndicator(false);
            lineMA1.setColor(getResources().getColor(R.color.ma10));
            lineMA1.setLineWidth(0.5f);
            lineMA1.setValueTextSize(0f);
            lineMA1.setDrawCircles(false);
            lineSets.add(lineMA1);
        }

        if (MA2.size()>0){
            LineDataSet lineMA2 = new LineDataSet(MA2, "MA2");
            lineMA2.setHighLightColor(Color.WHITE);
            lineMA2.setHighlightEnabled(false);
            lineMA2.setDrawHorizontalHighlightIndicator(false);
            lineMA2.setColor(getResources().getColor(R.color.ma20));
            lineMA2.setLineWidth(0.5f);
            lineMA2.setValueTextSize(0f);
            lineMA2.setDrawCircles(false);
            lineSets.add(lineMA2);
        }

        if (MA3.size()>0){
            LineDataSet lineMA3 = new LineDataSet(MA3, "MA3");
            lineMA3.setHighLightColor(Color.WHITE);
            lineMA3.setHighlightEnabled(false);
            lineMA3.setDrawHorizontalHighlightIndicator(false);
            lineMA3.setColor(getResources().getColor(R.color.ma60));
            lineMA3.setLineWidth(0.5f);
            lineMA3.setValueTextSize(0f);
            lineMA3.setDrawCircles(false);
            lineSets.add(lineMA3);
        }
        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(CR.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < CR.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.00",CR.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }

    public void drawVR(KChartBean bean , String[] time){
        List<Entry> MAVV=new ArrayList<>();
        List<Entry> VRData=new ArrayList<>();
        List<Entry> MAVR=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcVR(bean.getK(),MAVV,VRData,MAVR);
        //MAVV
        if (VRData.size()>0){
            LineDataSet lineMAVV = new LineDataSet(MAVV, "MAVV");
            lineMAVV.setHighLightColor(Color.WHITE);
            lineMAVV.setHighlightEnabled(true);
            lineMAVV.setDrawHorizontalHighlightIndicator(false);
            lineMAVV.setColor(getResources().getColor(R.color.transparent));
            lineMAVV.setLineWidth(0.5f);
            lineMAVV.setValueTextSize(0f);
            lineMAVV.setDrawCircles(false);
            lineSets.add(lineMAVV);
        }
        //VR
        if (VRData.size()>0){
            LineDataSet lineVR = new LineDataSet(VRData, "VR");
            lineVR.setHighLightColor(Color.WHITE);
            lineVR.setHighlightEnabled(false);
            lineVR.setDrawHorizontalHighlightIndicator(false);
            lineVR.setColor(getResources().getColor(R.color.ma5));
            lineVR.setLineWidth(0.5f);
            lineVR.setValueTextSize(0f);
            lineVR.setDrawCircles(false);
            lineSets.add(lineVR);
        }
        //MAVR
        if (MAVR.size()>0){
            LineDataSet lineMAVR = new LineDataSet(MAVR, "MAVR");
            lineMAVR.setHighLightColor(Color.WHITE);
            lineMAVR.setHighlightEnabled(false);
            lineMAVR.setDrawHorizontalHighlightIndicator(false);
            lineMAVR.setColor(getResources().getColor(R.color.ma10));
            lineMAVR.setLineWidth(0.5f);
            lineMAVR.setValueTextSize(0f);
            lineMAVR.setDrawCircles(false);
            lineSets.add(lineMAVR);
        }
        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(MAVR.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
       if (VRData.size()>0){
           MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
           MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
           List<String> ydata=new ArrayList<>();
           for (int i = 0; i < MAVV.size(); i++) {
               ydata.add(BaseUtil.getDecimalNum("#0.00",MAVV.get(i).getY()));
           }
           List<String> xdata=new ArrayList<>();
           for (int i = 0; i < bean.getK().size(); i++) {
               xdata.add(time[i]);
           }
           bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
       }
    }

    public void drawOBV(KChartBean bean , String[] time){
        List<Entry> OBV=new ArrayList<>();
        List<Entry> MAOBV=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcOBV(bean.getK(),OBV,MAOBV);
        //OBV
        LineDataSet lineOBV = new LineDataSet(OBV, "OBV");
        lineOBV.setHighLightColor(Color.WHITE);
        lineOBV.setHighlightEnabled(true);
        lineOBV.setDrawHorizontalHighlightIndicator(false);
        lineOBV.setColor(getResources().getColor(R.color.ma5));
        lineOBV.setLineWidth(0.5f);
        lineOBV.setValueTextSize(0f);
        lineOBV.setDrawCircles(false);
        lineSets.add(lineOBV);
        //MAOBV
        if (MAOBV.size()>0){
            LineDataSet lineMAOBV = new LineDataSet(MAOBV, "MAOBV");
            lineMAOBV.setHighLightColor(Color.WHITE);
            lineMAOBV.setHighlightEnabled(false);
            lineMAOBV.setDrawHorizontalHighlightIndicator(false);
            lineMAOBV.setColor(getResources().getColor(R.color.ma10));
            lineMAOBV.setLineWidth(0.5f);
            lineMAOBV.setValueTextSize(0f);
            lineMAOBV.setDrawCircles(false);
            lineSets.add(lineMAOBV);
        }
        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(OBV.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < OBV.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.00",OBV.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }

    public void drawEMV(KChartBean bean , String[] time){
        List<Entry> EMVData=new ArrayList<>();
        List<Entry> MAEMVData=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcEMV(bean.getK(),EMVData,MAEMVData);
        //EMV
        LineDataSet lineEMV = new LineDataSet(EMVData, "EMVData");
        lineEMV.setHighLightColor(Color.WHITE);
        lineEMV.setHighlightEnabled(true);
        lineEMV.setDrawHorizontalHighlightIndicator(false);
        lineEMV.setColor(getResources().getColor(R.color.ma5));
        lineEMV.setLineWidth(0.5f);
        lineEMV.setValueTextSize(0f);
        lineEMV.setDrawCircles(false);
        lineSets.add(lineEMV);
        //MAEMV
        if (MAEMVData.size()>0){
            LineDataSet lineMAEMV = new LineDataSet(MAEMVData, "MAEMVData");
            lineMAEMV.setHighLightColor(Color.WHITE);
            lineMAEMV.setHighlightEnabled(false);
            lineMAEMV.setDrawHorizontalHighlightIndicator(false);
            lineMAEMV.setColor(getResources().getColor(R.color.ma10));
            lineMAEMV.setLineWidth(0.5f);
            lineMAEMV.setValueTextSize(0f);
            lineMAEMV.setDrawCircles(false);
            lineSets.add(lineMAEMV);
        }
        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(EMVData.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < EMVData.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.000",EMVData.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }

    public void drawRSI(KChartBean bean , String[] time){
        List<Entry> RSIData1=new ArrayList<>();
        List<Entry> RSIData2=new ArrayList<>();
        List<Entry> RSIData3=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcRSI(bean.getK(),RSIData1,RSIData2,RSIData3);
        //RSI1
        LineDataSet lineRSI1 = new LineDataSet(RSIData1, "RSI1");
        lineRSI1.setHighLightColor(Color.WHITE);
        lineRSI1.setHighlightEnabled(true);
        lineRSI1.setDrawHorizontalHighlightIndicator(false);
        lineRSI1.setColor(getResources().getColor(R.color.ma5));
        lineRSI1.setLineWidth(0.5f);
        lineRSI1.setValueTextSize(0f);
        lineRSI1.setDrawCircles(false);
        lineSets.add(lineRSI1);
        //RSI2
        LineDataSet lineRSI2 = new LineDataSet(RSIData2, "RSI2");
        lineRSI2.setHighLightColor(Color.WHITE);
        lineRSI2.setHighlightEnabled(false);
        lineRSI2.setDrawHorizontalHighlightIndicator(false);
        lineRSI2.setColor(getResources().getColor(R.color.ma10));
        lineRSI2.setLineWidth(0.5f);
        lineRSI2.setValueTextSize(0f);
        lineRSI2.setDrawCircles(false);
        lineSets.add(lineRSI2);
        //RSI2
        LineDataSet lineRSI3 = new LineDataSet(RSIData3, "RSI3");
        lineRSI3.setHighLightColor(Color.WHITE);
        lineRSI3.setHighlightEnabled(false);
        lineRSI3.setDrawHorizontalHighlightIndicator(false);
        lineRSI3.setColor(getResources().getColor(R.color.ma20));
        lineRSI3.setLineWidth(0.5f);
        lineRSI3.setValueTextSize(0f);
        lineRSI3.setDrawCircles(false);
        lineSets.add(lineRSI3);

        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(RSIData1.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < RSIData1.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.000",RSIData1.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }

    public void drawKDJ(KChartBean bean , String[] time){
        List<Entry> K=new ArrayList<>();
        List<Entry> D=new ArrayList<>();
        List<Entry> J=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcKDJ(bean.getK(),K,D,J);
        //RSI1
        LineDataSet lineK = new LineDataSet(K, "K");
        lineK.setHighLightColor(Color.WHITE);
        lineK.setHighlightEnabled(true);
        lineK.setDrawHorizontalHighlightIndicator(false);
        lineK.setColor(getResources().getColor(R.color.ma5));
        lineK.setLineWidth(0.5f);
        lineK.setValueTextSize(0f);
        lineK.setDrawCircles(false);
        lineSets.add(lineK);
        //RSI2
        LineDataSet lineD = new LineDataSet(D, "D");
        lineD.setHighLightColor(Color.WHITE);
        lineD.setHighlightEnabled(false);
        lineD.setDrawHorizontalHighlightIndicator(false);
        lineD.setColor(getResources().getColor(R.color.ma10));
        lineD.setLineWidth(0.5f);
        lineD.setValueTextSize(0f);
        lineD.setDrawCircles(false);
        lineSets.add(lineD);
        //RSI2
        LineDataSet lineJ = new LineDataSet(J, "J");
        lineJ.setHighLightColor(Color.WHITE);
        lineJ.setHighlightEnabled(false);
        lineJ.setDrawHorizontalHighlightIndicator(false);
        lineJ.setColor(getResources().getColor(R.color.ma20));
        lineJ.setLineWidth(0.5f);
        lineJ.setValueTextSize(0f);
        lineJ.setDrawCircles(false);
        lineSets.add(lineJ);

        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(K.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < K.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.000",K.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }

    public void drawCCI(KChartBean bean , String[] time){
        List<Entry> CCIData=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcCCI(bean.getK(),CCIData);
        //CCI
        LineDataSet lineCCI = new LineDataSet(CCIData, "J");
        lineCCI.setHighLightColor(Color.WHITE);
        lineCCI.setHighlightEnabled(true);
        lineCCI.setDrawHorizontalHighlightIndicator(false);
        lineCCI.setColor(getResources().getColor(R.color.ma5));
        lineCCI.setLineWidth(0.5f);
        lineCCI.setValueTextSize(0f);
        lineCCI.setDrawCircles(false);
        lineSets.add(lineCCI);

        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(CCIData.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < CCIData.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.00",CCIData.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }


    public void drawMTM(KChartBean bean , String[] time){
        List<Entry> MTMData=new ArrayList<>();
        List<Entry> MAMTMData=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcMTM(bean.getK(),MTMData,MAMTMData);
        //MTM
        LineDataSet lineMTM = new LineDataSet(MTMData, "MTMData");
        lineMTM.setHighLightColor(Color.WHITE);
        lineMTM.setHighlightEnabled(true);
        lineMTM.setDrawHorizontalHighlightIndicator(false);
        lineMTM.setColor(getResources().getColor(R.color.ma5));
        lineMTM.setLineWidth(0.5f);
        lineMTM.setValueTextSize(0f);
        lineMTM.setDrawCircles(false);
        lineSets.add(lineMTM);
        //MAMTM
        LineDataSet lineMAMTM = new LineDataSet(MAMTMData, "MAMTMData");
        lineMAMTM.setHighLightColor(Color.WHITE);
        lineMAMTM.setHighlightEnabled(false);
        lineMAMTM.setDrawHorizontalHighlightIndicator(false);
        lineMAMTM.setColor(getResources().getColor(R.color.ma10));
        lineMAMTM.setLineWidth(0.5f);
        lineMAMTM.setValueTextSize(0f);
        lineMAMTM.setDrawCircles(false);
        lineSets.add(lineMAMTM);
        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(MTMData.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < MTMData.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.00",MTMData.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }

    public void drawPSY(KChartBean bean , String[] time){
        List<Entry> PSYData=new ArrayList<>();
        List<Entry> MAPSYData=new ArrayList<>();
        ArrayList<ILineDataSet> lineSets = new ArrayList<>();
        mStockUtil.CalcPSY(bean.getK(),PSYData,MAPSYData);
        //PSY
        LineDataSet linePSY = new LineDataSet(PSYData, "PSYData");
        linePSY.setHighLightColor(Color.WHITE);
        linePSY.setHighlightEnabled(true);
        linePSY.setDrawHorizontalHighlightIndicator(false);
        linePSY.setColor(getResources().getColor(R.color.ma5));
        linePSY.setLineWidth(0.5f);
        linePSY.setValueTextSize(0f);
        linePSY.setDrawCircles(false);
        lineSets.add(linePSY);
        //MAPSY
        LineDataSet lineMAPSY = new LineDataSet(MAPSYData, "MAMTMData");
        lineMAPSY.setHighLightColor(Color.WHITE);
        lineMAPSY.setHighlightEnabled(false);
        lineMAPSY.setDrawHorizontalHighlightIndicator(false);
        lineMAPSY.setColor(getResources().getColor(R.color.ma10));
        lineMAPSY.setLineWidth(0.5f);
        lineMAPSY.setValueTextSize(0f);
        lineMAPSY.setDrawCircles(false);
        lineSets.add(lineMAPSY);
        CombinedData combineData=new CombinedData();
        combineData.setData(new LineData(lineSets));
        bottomchart.setData(combineData);
        //设置X轴
        formatter=new MyXFormatter(time);
        bAxis.setValueFormatter(formatter);
        bAxis.setAxisMaximum(PSYData.size()-0.3f);
        bAxis.setAxisMinimum(-0.4f);
        //设置提示
        MyMarrkerViewLeft bright=new MyMarrkerViewLeft(QuotationActivity.this,R.layout.mymarkerview);
        MyMarrkerViewBottom bottom=new MyMarrkerViewBottom(QuotationActivity.this,R.layout.mymarkerview);
        List<String> ydata=new ArrayList<>();
        for (int i = 0; i < PSYData.size(); i++) {
            ydata.add(BaseUtil.getDecimalNum("#0.00",PSYData.get(i).getY()));
        }
        List<String> xdata=new ArrayList<>();
        for (int i = 0; i < bean.getK().size(); i++) {
            xdata.add(time[i]);
        }
        bottomchart.setMarrkerView(bright,bottom,ydata,xdata);
    }


    public void Lastinvalidate(KChartBean bean){
        //恢复到原来的矩阵大小
        Matrix TopMatrix = combinedchart.getTT();
        Matrix BottomMatrix = bottomchart.getTT();
        if (isMove){
            TopMatrix.postScale(4, 1f);
            BottomMatrix.postScale(4, 1f);
            combinedchart.moveViewToX(bean.getK().size()-1);
            bottomchart.moveViewToX(bean.getK().size()-1);
            isMove =false;
        }

        combinedchart.getViewPortHandler().refresh(TopMatrix, combinedchart, false);//将图表动画显示之前进行缩放
        bottomchart.getViewPortHandler().refresh(BottomMatrix, bottomchart, false);//将图表动画显示之前进行缩放

        //调整显示比例

        if (bean.getK().size()<15){
            bAxis.setLabelCount(1,true);
            bAxis.setLabelOnlyOne(true);
        }else {
            bAxis.setLabelCount(5,true);
            bAxis.setLabelOnlyOne(false);
        }
        combinedchart.getViewPortHandler().setMaximumScaleX(culcMaxscale(bean.getK().size()));
        bottomchart.getViewPortHandler().setMaximumScaleX(culcMaxscale(bean.getK().size()));
        if (isTest){
            setOffset();
            isTest=false;
        }

        combinedchart.invalidate();
        bottomchart.invalidate();
    }
    boolean isTest=true;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_k_time:
                Intent intent=new Intent(QuotationActivity.this,TimeActivity.class);
                intent.putExtra("goodsCode",goodsCode);
                intent.putExtra("goodsId",goodsId);
                intent.putExtra("goodsName",goodsName);
                startActivity(intent);
                isToLink =false;
                if (isFirst){
                    isFirst=false;
                }else {
                    isFirst=true;
                }
                Log.e("@#","CLICK");
                break;
            case R.id.tv_k_m1:
                setTopTvBg();
                stateTime=1;
                isToLink =false;
                mf="2";
                isMove=true;
                tv_m1.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                break;
            case R.id.tv_k_m5:
                setTopTvBg();
                stateTime=5;
                isToLink =false;
                mf="2";
                isMove=true;
                tv_m5.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                break;
            case R.id.tv_k_m15:
                setTopTvBg();
                stateTime=15;
                isToLink =false;
                mf="2";
                isMove=true;
                tv_m15.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                break;
            case R.id.tv_k_m30:
                setTopTvBg();
                stateTime=30;
                isToLink =false;
                mf="2";
                isMove=true;
                tv_m30.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                break;
            case R.id.tv_k_d1:
                setTopTvBg();
                stateTime=1;
                isToLink =false;
                mf="1";
                isMove=true;
                tv_d1.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                break;
            case R.id.tv_k_d5:
                setTopTvBg();
                stateTime=5;
                isToLink =false;
                mf="1";
                isMove=true;
                tv_d5.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                break;
            case R.id.tv_k_d20:
                setTopTvBg();
                stateTime=20;
                isToLink =false;
                mf="1";
                isMove=true;
                tv_d20.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                break;
            case R.id.tv_k_d60:
                setTopTvBg();
                stateTime=60;
                isToLink =false;
                mf="1";
                isMove=true;
                tv_d60.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                break;
            case R.id.tv_k_d250:
                setTopTvBg();
                stateTime=250;
                isToLink =false;
                mf="1";
                isMove=true;
                tv_d250.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                break;
            case R.id.tv_k_macd:
                if (Mode==0){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_macd.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_MACD;
                break;
            case R.id.tv_k_trix:
                if (Mode==1){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_trix.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_TRIX;
                break;
            case R.id.tv_k_brar:
                if (Mode==2){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_brar.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_BRAR;
                break;
            case R.id.tv_k_cr:
                if (Mode==3){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_cr.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_CR;
                break;
            case R.id.tv_k_vr:
                if (Mode==4){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_vr.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_VR;
                break;
            case R.id.tv_k_obv:
                if (Mode==5){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_obv.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_OBV;
                break;
            case R.id.tv_k_emv:
                if (Mode==6){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_emv.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_EMV;
                break;
            case R.id.tv_k_vol:
                if (Mode==7){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_vol.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_VOL;
                break;
            case R.id.tv_k_rsi:
                if (Mode==8){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_rsi.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_RSI;
                break;
            case R.id.tv_k_kdj:
                if (Mode==9){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_kdj.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_KDJ;
                break;
            case R.id.tv_k_cci:
                if (Mode==10){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_cci.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_CCI;
                break;
            case R.id.tv_k_mtm:
                if (Mode==11){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_mtm.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_MTM;
                break;
            case R.id.tv_k_psy:
                if (Mode==12){
                    return;
                }
                isToLink =false;
                setBottomTvBg();
                tv_psy.setBackground(getResources().getDrawable(R.drawable.bg_kchart_a));
                Mode=Const.MODE_PSY;
                break;
            case R.id.iv_k_back:
                finish();
                break;

        }
    }

    private void setTopTvBg(){
        isRefresh=true;
        tv_m1.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_m5.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_m15.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_m30.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_d1.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_d5.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_d20.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_d60.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_d250.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
    }

    private void setBottomTvBg(){
        isRefresh=true;
        tv_macd.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_trix.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_brar.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_cr.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_vr.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_obv.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_emv.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_vol.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_rsi.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_kdj.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_cci.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_mtm.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
        tv_psy.setBackground(getResources().getDrawable(R.drawable.bg_kchart_b));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask!=null){
            IsNoConnect=true;
            ThreadManager.getThreadPool().cancel(mTask);
        }
    }

    /*设置量表对齐*/
    private float transRightD;
    private float transRightY;
//    private void setOffset() {
//        float lineLeft = combinedchart.getViewPortHandler().offsetLeft();
//        float barLeft = bottomchart.getViewPortHandler().offsetLeft();
//        float lineRight = combinedchart.getViewPortHandler().offsetRight();
//        float barRight = bottomchart.getViewPortHandler().offsetRight();
//        float barBottom = bottomchart.getViewPortHandler().offsetBottom();
//        float offsetLeft, offsetRight;
//        float transLeft = 0, transRight = 0;
//        Log.e("@#","lineLeft="+lineLeft);
//        Log.e("@#","lineRight="+lineRight);
//        Log.e("@#","barLeft="+barLeft);
//        Log.e("@#","barRight="+barRight);
// /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
//        if (barLeft < lineLeft) {
//            //offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
//            // barChart.setExtraLeftOffset(offsetLeft);
//            transLeft = lineLeft;
//
//        } else {
//            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
//            combinedchart.setExtraLeftOffset(offsetLeft);
//            transLeft = barLeft;
//        }
//
//  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
//        if (barRight < lineRight) {
//            //offsetRight = Utils.convertPixelsToDp(lineRight);
//            //barChart.setExtraRightOffset(offsetRight);
//            transRight = lineRight;
//        } else {
//            offsetRight = Utils.convertPixelsToDp(barRight);
//            if (isMax){
//                combinedchart.setExtraRightOffset(0);
//            }else {
//                combinedchart.setExtraRightOffset(10);
//            }
//            transRight = barRight;
//        }
//        if ("1".equals(mf)){
//
//            if (transRightY==0.0f){
//                transRightY=transRight;
//            }
//            Log.e("@#","transRightY"+transRightY);
//            bottomchart.setExtraRightOffset(35);
//            bottomchart.setViewPortOffsets(transLeft, 5, transRightY, barBottom);
//        }else {
//            if (transRightD==0.0f){
//                transRightD=transRight;
//            }
//            Log.e("@#","transRightD"+transRightD);
//            bottomchart.setExtraRightOffset(30);
//            bottomchart.setViewPortOffsets(transLeft, 5, transRightD-28, barBottom);
//        }
//
//    }

    private void setOffset() {
        float lineLeft = combinedchart.getViewPortHandler().offsetLeft();
        float lineTop = combinedchart.getViewPortHandler().offsetTop();
        float lineRight = combinedchart.getViewPortHandler().offsetRight();
        float lineBottom = combinedchart.getViewPortHandler().offsetBottom();
        float barLeft = bottomchart.getViewPortHandler().offsetLeft();
        float barTop = bottomchart.getViewPortHandler().offsetTop();
        float barRight = bottomchart.getViewPortHandler().offsetRight();
        float barBottom = bottomchart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
           /* offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            barBottom.setExtraLeftOffset(offsetLeft);*/
            transLeft = lineLeft;
        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            combinedchart.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }
  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
          /*  offsetRight = Utils.convertPixelsToDp(lineRight);
            barChart.setExtraRightOffset(offsetRight);*/
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            combinedchart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        combinedchart.setViewPortOffsets(transLeft+16, lineTop, transRight-13, lineBottom);
        bottomchart.setViewPortOffsets(transLeft+16, barTop-35, transRight-13, barBottom);
    }




}
