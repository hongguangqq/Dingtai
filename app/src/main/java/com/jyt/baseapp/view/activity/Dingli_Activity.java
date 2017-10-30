package com.jyt.baseapp.view.activity;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.CaseAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.CaseBean;
import com.jyt.baseapp.bean.MoneyBean;
import com.jyt.baseapp.model.CaseModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.StringUtils;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.widget.GuaItem;
import com.jyt.baseapp.view.widget.QuotationDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class Dingli_Activity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_all_back)
    ImageView mIvAllBack;
    @BindView(R.id.tv_all_title)
    TextView mTvAllTitle;
    @BindView(R.id.iv_all_right)
    ImageView mIvAllRight;
    @BindView(R.id.rv_all_container)
    RecyclerView mRv_Container;
    //提交订单的dialog
    private QuotationDialog mCreateDialog;//icon_add被点击后弹出
    private View mCreateView;
    private PopupWindow mPopupType;//订立类型被点击后弹出
    private PopupWindow mPopupCode;//商品代码被点击后弹出
    private GuaItem mItem_code;//商品代码
    private GuaItem mItem_type;//订立类型
    private GuaItem mItem_price;//交易价格
    private GuaItem mItem_money;//可用资金
    private GuaItem mItem_num;//交易数
    private TextView mTv_max;//最大交易数
    private Button mBtn_submit;//提交按钮
    private LinearLayout mLl_content;//dialog的外层View
    private LinearLayout mLl_code;
    //撤销订单的dialog
    private QuotationDialog mRevokeDialog;
    private View mRevokeView;
    private TextView mTv_goodsName;
    private TextView mTv_goodsType;
    private TextView mTv_goodsID;
    private TextView mTv_price;
    private TextView mTv_num;
    private TextView mTv_balance;
    private TextView mTv_deposit;
    private TextView mTv_createdTime;
    private TextView mTv_goodsCode;
    private TextView mTv_dealType;
    private Button mBtn_cancel;
    //最终确认的dialog
    private AlertDialog mAlertDialog;
    private TextView mTv_title;
    private Button mBtn_alert_submit;
    private Button mBtn_alert_cancel;
    private boolean isCreateState;//确定取消弹窗复用，true为创建订单，false为取消订单



    private CaseModel mModel;
    private List<CaseBean> mData;
    private CaseAdapter mCaseAdapter;
    private float usePrice;//可用资金
    private int maxnum;//最大交易数
    private float price;//交易的资金--提交
    private int num;//交易数--提交
    private String goodsId;//商品编码ID--提交
    private String dealType ="1";//订立类型--提交
    private String orderNo;//撤销订单编号-提交
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_DATA_SUCCESS:
                    mData= (List<CaseBean>) msg.obj;
                    if (mData!=null){
                        mCaseAdapter.notifyData(mData);
                    }
                    break;
                case Const.STATE_UPLOAD_SUCCESS:
                    //订立成功，清空输入框
                    mItem_price.setInput("");
                    mItem_num.setInput("");
                    BaseUtil.makeText("订立成功");
                    //订立成功，刷新界面
                    initData();
                    break;
                case Const.STATE_DELETE_SUCCESS:
                    mRevokeDialog.dismiss();
                    BaseUtil.makeText("撤销成功");
                    //撤销成功，刷新界面
                    initData();
                    break;
                //加载商品编码数据
                case Const.STATE_CODE_SUCCESS:
                    final MoneyBean bean= (MoneyBean) msg.obj;
                    if (bean==null){
                        return;
                    }
                    if (bean.balance==null){
                        usePrice=0;
                    }else {
                        usePrice=Float.valueOf(bean.balance);//赋值余额

                    }
                    mItem_money.setInput(usePrice+"");
                    mItem_code.SetMsg(bean.goodsCode.get(0).goodsCode);//默认第一项
                    goodsId=bean.goodsCode.get(0).goodsId;//默认第一项
                    for (int i = 0; i < bean.goodsCode.size(); i++) {
                        final TextView tv= (TextView) View.inflate(BaseUtil.getContext(),R.layout.txt_code,null);
                        tv.setText(bean.goodsCode.get(i).goodsCode);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mItem_code.SetMsg(tv.getText().toString());
                                for (int i = 0; i < bean.goodsCode.size(); i++) {
                                    if (tv.getText().toString().equals(bean.goodsCode.get(i).goodsCode)){
                                        goodsId=bean.goodsCode.get(i).goodsId;
                                        mPopupCode.dismiss();
                                    }
                                }
                            }
                        });
                        mLl_code.addView(tv);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.layout_twoall;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDialog();
        iniSetting();
        initpopup();
        initRecyclerView();
        initData();
        initListener();
    }

    private void initView() {
        mModel = new CaseModel();
        mData=new ArrayList<>();
        mCaseAdapter = new CaseAdapter(mData,this);
        mModel.getUseMoney(this, new CaseModel.getMoneyDataListener() {
            @Override
            public void ResultData(boolean isSuccess, MoneyBean data) {
                if (isSuccess){
                    Message message=new Message();
                    message.what=Const.STATE_CODE_SUCCESS;
                    message.obj=data;
                    mHandler.sendMessage(message);
                }
            }
        });
    }




    private void initDialog() {
        //一级——订立挂单的dialog
        mCreateDialog = new QuotationDialog(this,R.layout.dialog_case);
        mCreateView = mCreateDialog.getView();
        mLl_content = (LinearLayout) mCreateView.findViewById(R.id.ll_item);
        mItem_code = (GuaItem) mCreateView.findViewById(R.id.guaitem_code);
        mItem_type = (GuaItem) mCreateView.findViewById(R.id.guaitem_type);
        mItem_price = (GuaItem) mCreateView.findViewById(R.id.guaitem_price);
        mItem_money = (GuaItem) mCreateView.findViewById(R.id.guaitem_money);
        mItem_num = (GuaItem) mCreateView.findViewById(R.id.guaitem_num);
        mTv_max = (TextView) mCreateView.findViewById(R.id.tv_max);
        mBtn_submit = (Button) mCreateView.findViewById(R.id.btn_submit);
        mItem_money.setInputEnable(false);//资金由用户联网获得，无需填写
        //一级——撤销挂单的dialog
        mRevokeDialog = new QuotationDialog(this, R.layout.dialog_revoke);
        mRevokeView = mRevokeDialog.getView();
        mTv_goodsName = (TextView) mRevokeView.findViewById(R.id.tv_goodsName);
        mTv_goodsType = (TextView) mRevokeView.findViewById(R.id.tv_goodsType);
        mTv_goodsID = (TextView) mRevokeView.findViewById(R.id.tv_goodsID);
        mTv_price = (TextView) mRevokeView.findViewById(R.id.tv_price);
        mTv_num = (TextView) mRevokeView.findViewById(R.id.tv_num);
        mTv_balance = (TextView) mRevokeView.findViewById(R.id.tv_balance);
        mTv_deposit = (TextView) mRevokeView.findViewById(R.id.tv_deposit);
        mTv_createdTime = (TextView) mRevokeView.findViewById(R.id.tv_createdTime);
        mTv_goodsCode = (TextView) mRevokeView.findViewById(R.id.tv_goodsCode);
        mTv_dealType = (TextView) mRevokeView.findViewById(R.id.tv_dealType);
        mBtn_cancel = (Button) mRevokeView.findViewById(R.id.btn_cancel);
        //二级——确定取消的dialog
        View alertView=View.inflate(Dingli_Activity.this,R.layout.dialog_alert,null);
        mTv_title = (TextView) alertView.findViewById(R.id.tv_alert_title);
        mBtn_alert_submit = (Button) alertView.findViewById(R.id.btn_alert_submit);
        mBtn_alert_cancel = (Button) alertView.findViewById(R.id.btn_alert_cancel);
        AlertDialog.Builder builder =new AlertDialog.Builder(Dingli_Activity.this,R.style.customDialog);
        builder.setView(alertView);
        builder.setCancelable(false);
        mAlertDialog=builder.create();
    }

    private void iniSetting(){

        mTv_max.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//画下划线
        mTv_max.getPaint().setAntiAlias(true);//抗锯齿
        mItem_type.setOnGuaPopClickListener(new GuaItem.OnGuaPopClickListener() {
            @Override
            public void OnClick(View v) {
                mPopupType.showAsDropDown(v,0,0);
            }
        });
        mItem_code.setOnGuaPopClickListener(new GuaItem.OnGuaPopClickListener() {
            @Override
            public void OnClick(View v) {
                mPopupCode.showAsDropDown(v,0,0);
            }
        });
        //随着价格的输入，最大交易数随着变化
        mItem_price.getEt_input().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //不允许第一个字符为"."
                String txt=s.toString();
                if (txt.length()==1 && ".".equals(txt)){
                    s.clear();

                }
                if (StringUtils.isEmpty(txt)){
                    mTv_max.setText("最大交易数:"+"0");
                    maxnum=0;
                    price=0;
                    return;
                }
                price=Float.valueOf(txt);
                int i= (int) (price+0.5f);
                if (i!=0){
                    maxnum=(int)(usePrice/price);
                    mTv_max.setText("最大交易数:"+maxnum);
                }
            }
        });
        //交易数限制为整数
        mItem_num.getEt_input().setKeyListener(DigitsKeyListener.getInstance("1234567890"));

    }

    private void initpopup() {

        final View popView_code=View.inflate(this,R.layout.pop_code,null);
        mLl_code = (LinearLayout) popView_code.findViewById(R.id.ll_pop);
        final View popView_type=View.inflate(this,R.layout.view_ll,null);
        LinearLayout ll_type = (LinearLayout) popView_type.findViewById(R.id.ll_container);
        for (int i = 0; i < 2; i++) {
            final TextView tv= (TextView) View.inflate(this,R.layout.txt_type,null);
            if (i==0){
                tv.setText("买入订立(涨)");
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItem_type.SetMsg(tv.getText().toString());
                        dealType ="1";
                        mPopupType.dismiss();
                    }
                });

            }else {
                tv.setText("买入订立(跌)");
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItem_type.SetMsg(tv.getText().toString());
                        dealType ="2";
                        mPopupType.dismiss();
                    }
                });
            }


            ll_type.addView(tv);
        }
        mPopupType =CreatePopWindow(popView_type);
        mPopupCode = CreatePopWindow(popView_code);
    }

    /**
     * 生产PopupWindow
     * @param popView
     * @return
     */
    private PopupWindow CreatePopWindow(View popView) {
        final PopupWindow popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.getContentView().setFocusableInTouchMode(true);
        popupWindow.getContentView().setFocusable(true);
        popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        return popupWindow;
    }

    private void initRecyclerView() {

        mRv_Container.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRv_Container.setAdapter(mCaseAdapter);


    }

    /**
     * 联网获取数据
     */
    private void initData(){
        mModel.getCaseData(this, new CaseModel.getCaseDataListener() {
            @Override
            public void ResultData(boolean isSuccess, List<CaseBean> data) {
                if (isSuccess){
                    Message message=new Message();
                    message.obj=data;
                    message.what=Const.STATE_DATA_SUCCESS;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void LoseLogin() {
                Const.LoseLogin(Dingli_Activity.this);
            }
        });
    }

    private void initListener(){
        mIvAllBack.setOnClickListener(this);
        mIvAllRight.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
        mBtn_cancel.setOnClickListener(this);
        mBtn_alert_submit.setOnClickListener(this);
        mBtn_alert_cancel.setOnClickListener(this);
        mCaseAdapter.setonViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener<CaseBean>() {
            @Override
            public void onClick(BaseViewHolder holder, CaseBean data, int position) {
                isCreateState=false;
                mTv_goodsName.setText(data.goodsName);
                if ("1".equals(data.goodsType)){
                    mTv_goodsType.setText("白盘");
                }else {
                    mTv_goodsType.setText("晚盘");
                }

                mTv_goodsID.setText(data.orderNo);
                mTv_price.setText(data.price);
                mTv_balance.setText(data.totalPrice);
//                mTv_deposit.setText(data.);
                mTv_createdTime.setText(data.createdTime);
                mTv_goodsCode.setText(data.goodsCode);
                if ("1".equals(data.dealType)){
                    mTv_dealType.setText("买入");
                }else {
                    mTv_dealType.setText("卖出");
                }
                orderNo=data.orderNo;
                mRevokeDialog.show();
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_all_back:
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
                break;
            case R.id.iv_all_right:
                isCreateState=true;//置为true，弹窗更改为创建订立
                mCreateDialog.show();
                BaseUtil.controlKeyboardLayout(mLl_content, mBtn_submit);
                break;
            //提交订单
            case R.id.btn_submit:
                submitCase();
                break;
             //撤销订单
            case R.id.btn_cancel:
                mRevokeDialog.dismiss();
                mTv_title.setText("是否撤销挂单");//设置弹窗标题信息
                mAlertDialog.show();
                mAlertDialog.getWindow().setLayout(BaseUtil.dip2px(200),BaseUtil.dip2px(200));
                break;
            //alert的按钮操作
            case R.id.btn_alert_submit:
                if (isCreateState){
                    //提交订立订单
                    mModel.CreateCaseData(this, goodsId, dealType, price+"", num+"", new CaseModel.CreateCaseDataListener() {
                        @Override
                        public void ResultData(boolean isSuccess) {
                            if (isSuccess){
                                Message message=new Message();
                                message.what=Const.STATE_UPLOAD_SUCCESS;
                                mHandler.sendMessage(message);
                            }
                        }
                    });
                }else {
                    //提交撤销订单
                    mModel.RevokeCase(Dingli_Activity.this, "1", orderNo, new CaseModel.RevokeCaseListener() {
                        @Override
                        public void ResultData(boolean isSuccess) {
                            Message message=new Message();
                            message.what=Const.STATE_DELETE_SUCCESS;
                            mHandler.sendMessage(message);
                        }
                    });
                }
                mAlertDialog.dismiss();
                break;
            case R.id.btn_alert_cancel:
                mAlertDialog.dismiss();

                break;
            default:
                break;
        }
    }


    private void submitCase(){
        mTv_title.setText("是否买入订立");//设置弹窗标题信息
        if (!StringUtils.isEmpty(mItem_num.getInput())){
            num=Integer.valueOf(mItem_num.getInput());
        }else {
            num=0;
        }

        if (price==0 || num==0){

            if (price==0){
                BaseUtil.makeText("请输入交易价格");
            }
            if (num==0){
                BaseUtil.makeText("请设置交易数");
            }
        }else {
            if (num>maxnum ){
                BaseUtil.makeText("超出最大交易数");
            }else {
                //表单验证合法，弹出Alertdialog
                mCreateDialog.dismiss();
                mAlertDialog.show();
                mAlertDialog.getWindow().setLayout(BaseUtil.dip2px(200),BaseUtil.dip2px(200));

            }
        }
    }


}
