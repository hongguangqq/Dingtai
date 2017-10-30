package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.GoldAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.bean.BankBean;
import com.jyt.baseapp.bean.GoldBean;
import com.jyt.baseapp.model.GoldModel;
import com.jyt.baseapp.model.RegisterModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.StringUtils;
import com.jyt.baseapp.view.widget.GuaItem;
import com.jyt.baseapp.view.widget.QuotationDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author LinWei on 2017/9/12 14:01
 */
public class IntogoldActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_all_back)
    ImageView mIv_Back;
    @BindView(R.id.tv_all_title)
    TextView mTv_AllTitle;
    @BindView(R.id.iv_all_right)
    ImageView mIv_Right;
    @BindView(R.id.rv_all_container)
    RecyclerView mRv_Container;
    @BindView(R.id.ll_price)
    LinearLayout mLlPrice;
    @BindView(R.id.tv_newdata)
    TextView mTvNewData;
    @BindView(R.id.tv_moredata)
    TextView mTvMoreData;
    @BindView(R.id.tv_alldata)
    TextView mTvAllData;

    private List<GoldBean> mData;
    private List<GoldBean> mMoreData;
    private GoldAdapter mAdapter;
    private GoldModel mModel;

    //一级dialog
    private QuotationDialog mDialog;
    private LinearLayout mLl_scroll;
    private GuaItem mItem_type;
    private GuaItem mItem_bank;
    private GuaItem mItem_currency;
    private GuaItem mItem_money;
    private EditText mEt_pwd;
    private Button mBtn_submit;
    //二级dialog
    private AlertDialog mAlertDialog;
    private TextView mTv_title;
    private Button mBtn_alert_submit;
    private Button mBtn_alert_cancel;
    //popup
    private PopupWindow mPopupWindow;
    private LinearLayout mLl_Popbank;
    private String bankCode;
    private String cash;
    private String pwd;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_DATA_SUCCESS:
                    mMoreData= (List<GoldBean>) msg.obj;
                    if (mMoreData!=null){
                        mData=mMoreData;
                        mAdapter.notifyData(mData);
                    }
                    break;
                case Const.STATE_ADDDATA_SUCCESS:
                    mMoreData= (List<GoldBean>) msg.obj;
                    if (mMoreData!=null){
                        mData.addAll(mMoreData);
                        mAdapter.notifyData(mData);
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initdata("0",false);
        initDialog();
        initPop();
        initRecycle();
        initListener();
    }

    private void init() {
        mModel = new GoldModel();
        mData=new ArrayList<>();
        mMoreData=new ArrayList<>();
        mAdapter = new GoldAdapter(mData,this);
        mLlPrice.setVisibility(View.VISIBLE);
        mTv_AllTitle.setText("入金");
        mIv_Right.setImageDrawable(getResources().getDrawable(R.mipmap.icon_into));
        mIv_Right.setColorFilter(getResources().getColor(R.color.color_o1));
    }

    /**
     * 用于网络请求，
     * @param lastID   从lastID开始请求15条数据，为0时，是刷新当前15条数据
     * @param isAll    true：无视lastID，请求全部数据；false：主要看lastID
     */
    private void initdata(String lastID, final boolean isAll) {
        final Message message=new Message();
        if (!isAll){
            if ("0".equals(lastID)){
                mModel.getIntoGoldData(this, lastID, new GoldModel.ResultGoldDataListener() {
                    @Override
                    public void ResultData(boolean isSuccess, List<GoldBean> data) {
                        if (isSuccess){
                            message.what= Const.STATE_DATA_SUCCESS;
                            message.obj=data;
                            mHandler.sendMessage(message);
                        }

                    }

                    @Override
                    public void LoseLogin() {
                        Const.LoseLogin(IntogoldActivity.this);
                    }
                });
            }else {
                mModel.getIntoGoldData(this, lastID, new GoldModel.ResultGoldDataListener() {
                    @Override
                    public void ResultData(boolean isSuccess, List<GoldBean> data) {
                        if (isSuccess){
                            message.what= Const.STATE_ADDDATA_SUCCESS;
                            message.obj=data;
                            mHandler.sendMessage(message);
                        }

                    }

                    @Override
                    public void LoseLogin() {
                        Const.LoseLogin(IntogoldActivity.this);
                    }
                });
            }
        }else {
            mModel.getIntoAllGoldData(this, new GoldModel.ResultGoldDataListener() {
                @Override
                public void ResultData(boolean isSuccess, List<GoldBean> data) {
                    if (isSuccess){
                        message.what=Const.STATE_DATA_SUCCESS;
                        message.obj=data;
                        mHandler.sendMessage(message);
                    }


                }

                @Override
                public void LoseLogin() {
                    Const.LoseLogin(IntogoldActivity.this);
                }
            });


        }
    }

    private void initPop(){
        RegisterModel registerModel=new RegisterModel();
        //获取银行数据,填入pop
        registerModel.GetBankData(this, new RegisterModel.BankDataResultListener() {
            @Override
            public void ResultData(final List<BankBean> data, final boolean isSuccess) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess){
                            for (int i = 0; i < data.size(); i++) {
                                final TextView bank=new TextView(BaseUtil.getContext());
                                final String code=data.get(i).bankCode;
                                bank.setText(data.get(i).bankName);
                                bank.setTextAppearance(BaseUtil.getContext(),R.style.bankText);
                                bank.setGravity(Gravity.CENTER_HORIZONTAL);
                                bank.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mItem_bank.SetMsg(bank.getText().toString());
                                        bankCode=code;
                                        if (mPopupWindow.isShowing()){
                                            mPopupWindow.dismiss();
                                        }
                                    }
                                });
                                mLl_Popbank.addView(bank);
                            }
                        }else {

                        }
                    }
                });

            }
        });
        View popView=getLayoutInflater().inflate(R.layout.pop_into,null);
        mLl_Popbank = (LinearLayout) popView.findViewById(R.id.ll_pop_bank);
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void initDialog() {
        mDialog = new QuotationDialog(this, R.layout.dialog_into);
        View dialogView= mDialog.getView();
        mLl_scroll = (LinearLayout) dialogView.findViewById(R.id.ll_into_scroll);
        mItem_type = (GuaItem) dialogView.findViewById(R.id.guaitem_type);
        mItem_bank = (GuaItem) dialogView.findViewById(R.id.guaitem_bank);
        mItem_currency = (GuaItem) dialogView.findViewById(R.id.guaitem_currency);
        mItem_money = (GuaItem) dialogView.findViewById(R.id.guaitem_money);
        mEt_pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        mBtn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        //二级
        View alertView=View.inflate(IntogoldActivity.this,R.layout.dialog_alert,null);
        mTv_title = (TextView) alertView.findViewById(R.id.tv_alert_title);
        mTv_title.setText("是否出金");
        mBtn_alert_submit = (Button) alertView.findViewById(R.id.btn_alert_submit);
        mBtn_alert_cancel = (Button) alertView.findViewById(R.id.btn_alert_cancel);
        AlertDialog.Builder builder =new AlertDialog.Builder(IntogoldActivity.this,R.style.customDialog);
        builder.setView(alertView);
        builder.setCancelable(false);
        mAlertDialog=builder.create();

    }

    private void initRecycle() {
        mRv_Container.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRv_Container.setAdapter(mAdapter);
    }

    private void initListener() {
        mIv_Back.setOnClickListener(this);
        mIv_Right.setOnClickListener(this);
        mTvNewData.setOnClickListener(this);
        mTvMoreData.setOnClickListener(this);
        mTvAllData.setOnClickListener(this);
        mItem_bank.setOnGuaPopClickListener(new GuaItem.OnGuaPopClickListener() {
            @Override
            public void OnClick(View v) {
                mPopupWindow.showAsDropDown(v,10,3);
            }
        });
        mBtn_submit.setOnClickListener(this);
        mBtn_alert_cancel.setOnClickListener(this);
        mBtn_alert_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_all_back:
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
                break;
            case R.id.iv_all_right:
                mEt_pwd.setText("");
                mItem_money.setInput("");
                mDialog.show();
                BaseUtil.controlKeyboardLayout(mLl_scroll,mBtn_submit);
                break;
            case R.id.tv_newdata:
                initdata("0",false);
                break;
            case R.id.tv_moredata:
                if (mData.size()>0){
                    initdata(mData.get(mData.size()-1).cashNo,false);
                }
                break;
            case R.id.tv_alldata:
                initdata("0",true);
                break;
            case R.id.btn_submit:
                if (CheckParams()){
                    mDialog.dismiss();
                    mAlertDialog.show();
                    mAlertDialog.getWindow().setLayout(BaseUtil.dip2px(200),BaseUtil.dip2px(200));
                }
                break;
            case R.id.btn_alert_cancel:
                mAlertDialog.dismiss();
                break;
            case R.id.btn_alert_submit:
                mModel.IntoGlodePost(IntogoldActivity.this, cash, bankCode, pwd, new GoldModel.ResultIntoGlodeListener() {
                    @Override
                    public void ResultData(final boolean isSuccess, final String cashId) {
                        if (isSuccess){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (isSuccess){
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse(Path.URLWebIntoGold+cashId);
                                        intent.setData(content_url);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
                mAlertDialog.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 检查提交的数据是否合法
     * @return
     */
    private boolean CheckParams(){
        cash=mItem_money.getInput();
        pwd=mEt_pwd.getText().toString().trim();
        int i=0;
        if (StringUtils.isEmpty(bankCode)){
            i++;
            BaseUtil.makeText("请选择银行");
        }
        if (StringUtils.isEmpty(cash)){
            i++;
            BaseUtil.makeText("请输入入金金额");
        }
        if (StringUtils.isEmpty(pwd)){
            i++;
            BaseUtil.makeText("请输入密码");
        }
        if (i!=0){
            return false;
        }else {

        }
        return true;
    }
}
