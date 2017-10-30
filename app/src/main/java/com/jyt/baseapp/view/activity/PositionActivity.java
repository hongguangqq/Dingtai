package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.CaseAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.CaseBean;
import com.jyt.baseapp.model.PositionModel;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.widget.QuotationDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**持仓
 * @author LinWei on 2017/9/11 17:17
 */
public class PositionActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_all_back)
    ImageView mIv_Back;
    @BindView(R.id.tv_all_title)
    TextView mTv_Title;
    @BindView(R.id.iv_all_right)
    ImageView mIv_Right;
    @BindView(R.id.rv_all_container)
    RecyclerView mRv_Container;

    private List<CaseBean> mData;
    private PositionModel mModel;
    private CaseAdapter mCaseAdapter;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_DATA_SUCCESS:
                    mData= (List<CaseBean>) msg.obj;
                    if (mData!=null){
                        mCaseAdapter.notifyData(mData);
                    }
                    break;

                default:
                    break;
            }
        }
    };

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
    private Button mBtn_submit;//提交按钮
    private LinearLayout mLl_vis;


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
        initRecycle();
        initData();
        initDialog();
        initListener();
    }

    private void init() {
        mModel = new PositionModel();
        mData=new ArrayList<>();
        mCaseAdapter = new CaseAdapter(mData,this);
        mIv_Right.setVisibility(View.GONE);
        mTv_Title.setText("持仓");

    }

    private void initRecycle() {
        mRv_Container.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRv_Container.setAdapter(mCaseAdapter);
    }

    private void initData(){
        mModel.getPositionData(this, new PositionModel.getCaseDataListener() {
            @Override
            public void ResultData(boolean isSuccess, List<CaseBean> data) {
                if (isSuccess){
                    Message message=new Message();
                    message.what= Const.STATE_DATA_SUCCESS;
                    message.obj=data;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void LoseLogin() {
                Const.LoseLogin(PositionActivity.this);
            }
        });
    }

    private void initDialog(){
        mDialog = new QuotationDialog(this, R.layout.dialog_transfer);
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
        mLl_vis = (LinearLayout) dialogView.findViewById(R.id.ll_transfer_transfering);
        mLl_vis.setVisibility(View.GONE);
        mBtn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        mBtn_submit.setVisibility(View.GONE);
    }

    private void initListener(){
        mIv_Back.setOnClickListener(this);
        mCaseAdapter.setonViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener<CaseBean>() {
            @Override
            public void onClick(BaseViewHolder holder, CaseBean data, int position) {
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
            case R.id.iv_all_back:
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
                break;

            default:
                break;
        }
    }
}
