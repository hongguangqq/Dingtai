package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.GoldAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.GoldBean;
import com.jyt.baseapp.model.GoldModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.StringUtils;
import com.jyt.baseapp.view.widget.QuotationDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author LinWei on 2017/9/12 14:01
 */
public class OutgoldActivity extends BaseActivity implements View.OnClickListener {
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

    private GoldModel mModel;
    private List<GoldBean> mData;
    private List<GoldBean> mMoreData;
    private GoldAdapter mAdapter;
    //一级dialog
    private QuotationDialog mDialog;
    private TextView mTv_balance;
    private EditText mEt_money;
    private EditText mEt_pwd;
    private Button mBtn_submit;
    //二级dialog
    private AlertDialog mAlertDialog;
    private TextView mTv_title;
    private Button mBtn_alert_submit;
    private Button mBtn_alert_cancel;

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
        initRecycle();
        initListener();
    }

    private void init() {
        mModel=new GoldModel();
        mData=new ArrayList<>();
        mMoreData=new ArrayList<>();
        mAdapter = new GoldAdapter(mData,this);
        mLlPrice.setVisibility(View.VISIBLE);
        mIv_Right.setImageDrawable(getResources().getDrawable(R.mipmap.icon_out));
        mIv_Right.setColorFilter(getResources().getColor(R.color.color_o1));
        mTv_AllTitle.setText("出金");
    }

    private void initdata(String lastID, final boolean isAll) {
        final Message message=new Message();
        if (!isAll){
            if ("0".equals(lastID)){
                mModel.getOutGoldData(this, lastID, new GoldModel.ResultGoldDataListener() {
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
                        Const.LoseLogin(OutgoldActivity.this);
                    }
                });
            }else {
                mModel.getOutGoldData(this, lastID, new GoldModel.ResultGoldDataListener() {
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
                        Const.LoseLogin(OutgoldActivity.this);
                    }
                });
            }
        }else {
            mModel.getOutAllGoldData(this, new GoldModel.ResultGoldDataListener() {
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
                    Const.LoseLogin(OutgoldActivity.this);
                }
            });
        }
    }

    private void initDialog() {
        mDialog = new QuotationDialog(this, R.layout.dialog_out);
        View dialogView= mDialog.getView();
        mTv_balance = (TextView) dialogView.findViewById(R.id.tv_balance);
        mEt_money = (EditText) dialogView.findViewById(R.id.et_intomoney);
        mEt_pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        mBtn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        //二级
        View alertView=View.inflate(OutgoldActivity.this,R.layout.dialog_alert,null);
        mTv_title = (TextView) alertView.findViewById(R.id.tv_alert_title);
        mTv_title.setText("是否出金");
        mBtn_alert_submit = (Button) alertView.findViewById(R.id.btn_alert_submit);
        mBtn_alert_cancel = (Button) alertView.findViewById(R.id.btn_alert_cancel);
        AlertDialog.Builder builder =new AlertDialog.Builder(OutgoldActivity.this,R.style.customDialog);
        builder.setView(alertView);
        builder.setCancelable(false);
        mAlertDialog=builder.create();
        mEt_money.setKeyListener(DigitsKeyListener.getInstance("1234567890."));
        mEt_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String txt=s.toString();
                if (txt.length()==1 && ".".equals(txt)){
                    s.clear();
                }
            }
        });
    }

    private void initRecycle() {
        mRv_Container.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRv_Container.setAdapter(mAdapter);
    }

    private void initListener() {
        mIv_Back.setOnClickListener(this);
        mIv_Right.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
        mBtn_alert_cancel.setOnClickListener(this);
        mBtn_alert_submit.setOnClickListener(this);
        mTvNewData.setOnClickListener(this);
        mTvMoreData.setOnClickListener(this);
        mTvAllData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_all_back:
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
                break;
            case R.id.iv_all_right:
                mEt_money.setText("");
                mEt_pwd.setText("");
                mDialog.show();
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
                mAlertDialog.dismiss();
                mModel.OutGlodePost(OutgoldActivity.this, cash, pwd, new GoldModel.ResultOutGlodeListener() {
                    @Override
                    public void ResultData(boolean isSuccess) {
                        if (isSuccess){
                            initdata("0",false);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private boolean CheckParams(){
        cash=mEt_money.getText().toString().trim();
        pwd=mEt_pwd.getText().toString().trim();
        int i=0;
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
