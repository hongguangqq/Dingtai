package com.jyt.baseapp.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.CaseAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.CaseBean;
import com.jyt.baseapp.model.TransferModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.StringUtils;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.widget.QuotationDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**可转让的Fragment
 * @author LinWei on 2017/9/9 15:57
 */
public class TransferFragment  extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.rv_transfer_container)
    RecyclerView mRv_Container;
    Unbinder unbinder;

    private List<CaseBean> mData;
    private TransferModel mModel;

    private CaseAdapter mAdapter;
    private QuotationDialog mDialog;
    private CardView mCv_contianer;
    private TextView mTv_goodsName;//产品名称
    private TextView mTv_goodsType;//品种
    private TextView mTv_goodsID;//代码
    private TextView mTv_transferPrice;//转让价
    private TextView mTv_num;//数量
    private TextView mTv_totalPrice;//总贷值
    private TextView mTv_flowPrice;//浮动盈亏
    private TextView mTv_price;//订立价
    private TextView mTv_createdTime;//订立日期
    private TextView mTv_effectiveTime;//生效日期
    private TextView mTv_goodsCode;//订单编号
    private TextView mTv_dealType;//类型
    private TextView mTv_reckon;//盈亏估算
    private EditText mEt_sellprice;//转让价格
    private TextView mTv_referPrice;//参考价格
    private TextView mTv_sellnum;//数量
    private Button mBtn_submit;//提交按钮
    //AlertDialog
    private AlertDialog mAlertDialog;
    private TextView mTv_title;
    private Button mBtn_alert_submit;
    private Button mBtn_alert_cancel;

    private String price;//输入的转让价格
    private String orderNo;//转让订单的编号ID

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_DATA_SUCCESS:
                    mData= (List<CaseBean>) msg.obj;
                    if (mData!=null){
                        mAdapter.notifyData(mData);
                    }
                    break;
                case Const.STATE_TRANSFER_SUCCESS:
                    BaseUtil.makeText("转让成功");
                    Intent intent=new Intent();
                    intent.setAction(Const.Action_TRANSFERING);
                    getActivity().sendBroadcast(intent);
                    initData();//刷新界面
                    break;

                default:
                    break;
            }
        }
    };
    private TransferReceiver mReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transfer, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        initRecycle();
        initData();
        initDialog();
        initListener();
        return rootView;
    }

    private void init() {
        mModel=new TransferModel();
        mData=new ArrayList<>();
        mAdapter = new CaseAdapter(mData,getActivity());
        mReceiver = new TransferReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Const.Action_TRANSFER);
        getActivity().registerReceiver(mReceiver,filter);
    }

    private void initRecycle(){
        mRv_Container.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRv_Container.setAdapter(mAdapter);
    }

    private void  initData(){
        mModel.getTransferData(getContext(), new TransferModel.getCaseDataListener() {
            @Override
            public void ResultData(boolean isSuccess, List<CaseBean> data) {
                if (isSuccess){
                    Message message=new Message();
                    message.what=Const.STATE_DATA_SUCCESS;
                    message.obj=data;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void LoseLogin() {
                Const.LoseLogin(getActivity());
            }
        });
    }

    private void initDialog() {
        //一级——转让dialog
        mDialog = new QuotationDialog(getActivity(), R.layout.dialog_transfer);
        View dialogView= mDialog.getView();
        mCv_contianer = (CardView) dialogView.findViewById(R.id.cv_transfer_container);
        mTv_goodsName = (TextView) dialogView.findViewById(R.id.tv_goodsName);
        mTv_goodsType = (TextView) dialogView.findViewById(R.id.tv_goodsType);
        mTv_goodsID = (TextView) dialogView.findViewById(R.id.tv_goodsID);
        mTv_transferPrice = (TextView) dialogView.findViewById(R.id.tv_transferPrice);
        mTv_num = (TextView) dialogView.findViewById(R.id.tv_num);
        mTv_totalPrice = (TextView) dialogView.findViewById(R.id.tv_totalPrice);
        mTv_flowPrice = (TextView) dialogView.findViewById(R.id.tv_flowPrice);
        mTv_price = (TextView) dialogView.findViewById(R.id.tv_price);
        mTv_createdTime = (TextView) dialogView.findViewById(R.id.tv_createdTime);
        mTv_effectiveTime = (TextView) dialogView.findViewById(R.id.tv_effectiveTime);
        mTv_goodsCode = (TextView) dialogView.findViewById(R.id.tv_goodsCode);
        mTv_dealType = (TextView) dialogView.findViewById(R.id.tv_dealType);
        mTv_reckon = (TextView) dialogView.findViewById(R.id.tv_reckon);
        mEt_sellprice = (EditText) dialogView.findViewById(R.id.et_sellprice);
        mTv_referPrice = (TextView) dialogView.findViewById(R.id.tv_referPrice);
        mTv_sellnum = (TextView) dialogView.findViewById(R.id.tv_sellnum);
        mBtn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        //限制输入，只允许数字
        mEt_sellprice.setKeyListener(DigitsKeyListener.getInstance("1234567890."));
        mEt_sellprice.addTextChangedListener(new TextWatcher() {
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
        //确定取消的dialog
        View alertView=View.inflate(getActivity(),R.layout.dialog_alert,null);
        mTv_title = (TextView) alertView.findViewById(R.id.tv_alert_title);
        mBtn_alert_submit = (Button) alertView.findViewById(R.id.btn_alert_submit);
        mBtn_alert_cancel = (Button) alertView.findViewById(R.id.btn_alert_cancel);
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity(),R.style.customDialog);
        builder.setView(alertView);
        builder.setCancelable(false);
        mAlertDialog=builder.create();
        mTv_title.setText("转让挂单");


    }

    private void initListener() {
        mBtn_submit.setOnClickListener(this);
        mBtn_alert_submit.setOnClickListener(this);
        mBtn_alert_cancel.setOnClickListener(this);
        //点击条目，展现Dialog
        mAdapter.setonViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener<CaseBean>() {
            @Override
            public void onClick(BaseViewHolder holder, CaseBean data, int position) {
                orderNo=data.orderNo;
                BaseUtil.controlKeyboardLayout(mCv_contianer,mBtn_submit);
                mEt_sellprice.setText("");
                mTv_goodsName.setText(data.goodsName);
                if ("1".equals(data.goodsType)){
                    mTv_goodsType.setText("白盘");
                }else {
                    mTv_goodsType.setText("晚盘");
                }
                mTv_transferPrice.setText(data.transferPrice);
                mTv_goodsID.setText(data.orderNo);
                mTv_num.setText(data.num);
                mTv_totalPrice.setText(data.totalPrice);
                mTv_flowPrice.setText(data.flowPrice);
                mTv_price.setText(data.price);
                mTv_createdTime.setText(data.createdTime);
                mTv_effectiveTime.setText(data.effectiveTime);
                mTv_goodsCode.setText(data.goodsCode);
                if ("1".equals(data.dealType)){
                    mTv_dealType.setText("买入");
                }else {
                    mTv_dealType.setText("卖出");
                }
                mDialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //验证输入，符合规范的话，弹出Dialog做最终确认
            case R.id.btn_submit:
                price = mEt_sellprice.getText().toString().trim();
                if (StringUtils.isEmpty(price)){
                    BaseUtil.makeText("请输入转让价格");
                    return;
                }
                mDialog.dismiss();
                mAlertDialog.show();
                mAlertDialog.getWindow().setLayout(BaseUtil.dip2px(200),BaseUtil.dip2px(200));
                break;
            case R.id.btn_alert_submit:
                mModel.TransferCase(getActivity(), orderNo, price, new TransferModel.ResultTransferListener() {
                    @Override
                    public void ResultData(boolean isSuccess) {
                        if (isSuccess){
                            Message message=new Message();
                            message.what=Const.STATE_TRANSFER_SUCCESS;
                            mHandler.sendMessage(message);
                        }
                    }
                });
                mAlertDialog.dismiss();
                break;
            case R.id.btn_alert_cancel:
                mAlertDialog.dismiss();
                break;
            default:
                break;
        }
    }

    //用于撤销转让后，可转让界面的刷新
    public class TransferReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Const.Action_TRANSFER.equals(intent.getAction())){
                initData();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(mReceiver);
    }
}
