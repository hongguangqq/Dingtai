package com.jyt.baseapp.view.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.TransactionAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.TransactionBean;
import com.jyt.baseapp.model.TransactionModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.widget.QuotationDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**交易流水
 * @author LinWei on 2017/9/11 17:57
 */
public class TransactionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_all_back)
    ImageView mIv_Back;
    @BindView(R.id.tv_all_title)
    TextView mTv_Title;
    @BindView(R.id.iv_all_right)
    ImageView mIv_Right;
    @BindView(R.id.rv_all_container)
    RecyclerView mRv_Container;
    @BindView(R.id.token_from)
    TextView mTokenFrom;
    @BindView(R.id.tv_from)
    TextView mTvFrom;
    @BindView(R.id.token_to)
    TextView mTokenTo;
    @BindView(R.id.tv_to)
    TextView mTvTo;
    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.rl_fw)
    RelativeLayout mRlFw;
    private TimePickerView mPvTime;
    private TransactionModel mModel;
    private List<TransactionBean> mData;
    private TransactionAdapter mAdapter;
    private boolean isTokenTo;//是否为结束时间的标识点击
    private String searchTime=BaseUtil.currentTime("yyyy/MM/dd");//起始时间(默认为当前时间)-提交
    private String endTime=BaseUtil.currentTime("yyyy/MM/dd");//结束时间(默认为当前时间)-提交
    //dialog
    private TextView mTv_goodsName;
    private TextView mTv_goodsType;
    private TextView mTv_goodsCode;
    private TextView mTv_dealPrice;
    private TextView mTv_num;
    private TextView mTv_procedurePrice;
    private TextView mTv_changePrice;
    private TextView mTv_price;
    private TextView mTv_createdTime;
    private TextView mTv_effectiveTime;
    private TextView mTv_goodsOrderNo;
    private TextView mTv_dealType;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_DATA_SUCCESS:
                    mData= (List<TransactionBean>) msg.obj;
                    if (mData!=null){
                    }else {
                        mData.clear();
                    }
                    mAdapter.notifyData(mData);
                    break;

                default:
                    break;
            }
        }
    };
    private QuotationDialog mDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_twoall;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initData(BaseUtil.currentTime("yyyy-MM-dd"), BaseUtil.currentTime("yyyy-MM-dd"));
        initRecycle();
        initDialog();
        initPicker();
        initListener();


    }

    private void init() {
        mModel = new TransactionModel();
        mData=new ArrayList<>();
        mTv_Title.setText("交易流水");
        mIv_Right.setVisibility(View.GONE);
        mRlFw.setVisibility(View.VISIBLE);
        mTvFrom.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//画下划线
        mTvFrom.getPaint().setAntiAlias(true);//抗锯齿
        mTvFrom.setText(BaseUtil.currentTime("yyyy/MM/dd"));
        mTvTo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//画下划线
        mTvTo.getPaint().setAntiAlias(true);//抗锯齿
        mTvTo.setText(BaseUtil.currentTime("yyyy/MM/dd"));
        mAdapter = new TransactionAdapter(mData,this);
    }


    private void initData(String starTime,String endTime) {
        mModel.getFlowingWaterData(this, starTime,endTime, new TransactionModel.ResultFwDataListener() {
            @Override
            public void ResultData(boolean isSuccess, List<TransactionBean> data) {
                if (isSuccess){
                    Message message=new Message();
                    message.what= Const.STATE_DATA_SUCCESS;
                    message.obj=data;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void LoseLogin() {
                Const.LoseLogin(TransactionActivity.this);
            }
        });
    }

    private void initRecycle() {
        mRv_Container.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRv_Container.setAdapter(mAdapter);
    }

    private void initDialog(){
        mDialog = new QuotationDialog(this, R.layout.dialog_transction);
        View dialogView= mDialog.getView();
        mTv_goodsName = (TextView) dialogView.findViewById(R.id.tv_goodsName);
        mTv_goodsType = (TextView) dialogView.findViewById(R.id.tv_goodsType);
        mTv_goodsCode = (TextView) dialogView.findViewById(R.id.tv_goodsID);
        mTv_dealPrice = (TextView) dialogView.findViewById(R.id.tv_dealPrice);
        mTv_num = (TextView) dialogView.findViewById(R.id.tv_num);
        mTv_procedurePrice = (TextView) dialogView.findViewById(R.id.tv_procedurePrice);
        mTv_changePrice = (TextView) dialogView.findViewById(R.id.tv_changePrice);
        mTv_price = (TextView) dialogView.findViewById(R.id.tv_price);
        mTv_createdTime = (TextView) dialogView.findViewById(R.id.tv_createdTime);
        mTv_goodsOrderNo = (TextView) dialogView.findViewById(R.id.tv_goodsCode);
        mTv_dealType = (TextView) dialogView.findViewById(R.id.tv_dealType);
    }

    private void initPicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);//设置起始时间
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030, 11, 28);//设置结束时间

        mPvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                if (isTokenTo){
                    endTime=BaseUtil.getTime(date,"yyyy-MM-dd");
                    mTvTo.setText(BaseUtil.getTime(date,"yyyy/MM//dd"));
                }else {
                    searchTime=BaseUtil.getTime(date,"yyyy-MM-dd");
                    mTvFrom.setText(BaseUtil.getTime(date,"yyyy/MM//dd"));
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})//默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(getResources().getColor(R.color.color_g2))//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.color_g2))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.color_g2))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.color_b3))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.color_b2))//滚轮背景颜色 Night mode
                .setTextColorCenter(getResources().getColor(R.color.color_g2))
                .setRangDate(startDate, endDate)
                .setDate(selectedDate)// 默认是系统时间*/
                .build();


    }

    private void initListener() {
        mTvFrom.setOnClickListener(this);
        mTvTo.setOnClickListener(this);
        mIv_Back.setOnClickListener(this);
        mBtnSearch.setOnClickListener(this);
        mAdapter.setonViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener<TransactionBean>() {
            @Override
            public void onClick(BaseViewHolder holder, TransactionBean data, int position) {
                mTv_goodsName.setText(data.goodsName);
                if ("1".equals(data.goodsType)){
                    mTv_goodsType.setText("白盘");
                }else {
                    mTv_goodsType.setText("晚盘");
                }
                mTv_goodsCode.setText(data.goodsCode);
                mTv_dealPrice.setText(data.dealPrice);
                mTv_num.setText(data.num);
                mTv_procedurePrice.setText(data.procedurePrice);
                mTv_changePrice.setText(data.changePrice);
                mTv_price.setText(data.price);
                mTv_createdTime.setText(data.createdTime);
                mTv_goodsOrderNo.setText(data.orderNo);
                if ("1".equals(data.dealType)){
                    mTv_dealType.setText("买入订立");
                }else {
                    mTv_dealType.setText("卖出订立");
                }
                mDialog.show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_all_back:
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
                break;
            case R.id.tv_from:
                isTokenTo=false;
                mPvTime.show();
                break;
            case R.id.tv_to:
                isTokenTo=true;
                mPvTime.show();
                break;
            case R.id.btn_search:
                initData(searchTime,endTime);
                break;
        }
    }
}
