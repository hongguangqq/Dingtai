package com.jyt.baseapp.view.fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.QuotationAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.QuotationBean;
import com.jyt.baseapp.model.QuotationModel;
import com.jyt.baseapp.service.QuotationService;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.StringUtils;
import com.jyt.baseapp.view.activity.QuotationActivity;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author LinWei on 2017/8/31 14:44
 */
public class QuotationFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.rv_quotation_container)
    RecyclerView mRv_Container;
    QuotationAdapter mAdapter;
    private List<QuotationBean> mDatas;
    Unbinder unbinder;
    private boolean isReady;
    //Service端的Messenger对象
    private Messenger mServiceMessenger;
    //Fragment端的Messenger对象
    private Messenger mActivityMessenger;

    private long lastTime;
    private QuotationModel mModel;
    private boolean isCloseted=false;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.STATE_DATA_SUCCESS:
                    mDatas= (List<QuotationBean>) msg.obj;
                    mAdapter.notifyData(mDatas);

                    break;

                default:
                    break;
            }
        }
    };
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger=new Messenger(service);
            Message message=new Message();
            message.what=0X03;
            message.replyTo=mActivityMessenger;
            try {
                mServiceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Intent mIntent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quotation, container, false);
        init(rootView);
        initSetting();
        initService();
        return rootView;
    }

    private void init(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        mModel=new QuotationModel(getActivity());
        mDatas=new ArrayList<>();
        mActivityMessenger=new Messenger(mHandler);
        mAdapter=new QuotationAdapter(mDatas,getActivity());


    }

    private void initSetting(){
        mRv_Container.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mRv_Container.setAdapter(mAdapter);
        mAdapter.setonViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener<QuotationBean>() {
            @Override
            public void onClick(BaseViewHolder holder, QuotationBean data, int position) {
                if (StringUtils.isEmpty(data.goodsId) || StringUtils.isEmpty(data.goodsCode) || StringUtils.isEmpty(data.goodsName)){
                    return;
                }
                Intent intent=new Intent(getActivity(), QuotationActivity.class);
                intent.putExtra("goodsName",data.goodsName);
                intent.putExtra("goodsCode",data.goodsCode);
                intent.putExtra("goodsId",data.goodsId);
                startActivity(intent);
            }
        });
    }

    private void initService() {
        mIntent = new Intent(getActivity(),QuotationService.class);
        isCloseted=BaseUtil.getContext().bindService(mIntent,connection, Service.BIND_AUTO_CREATE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isCloseted){
            BaseUtil.getContext().unbindService(connection);
            BaseUtil.getContext().stopService(mIntent);
        }
        unbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void SwitchGoodType(int type){
        Message message=new Message();
        if (type==0){
            message.what=0X00;
        }else if (type==1){
            message.what=0X01;
        }else if (type==2){
            message.what=0X02;
        }
        try {
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
