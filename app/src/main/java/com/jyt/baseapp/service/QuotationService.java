package com.jyt.baseapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.QuotationBean;
import com.jyt.baseapp.model.QuotationModel;

import java.util.List;
import java.util.Timer;

/**
 * @author LinWei on 2017/9/4 17:42
 */
public class QuotationService extends Service {
    private Timer timer;
    private TimeTask task;
    private Handler mHandler;
    private String goodsType="1";
    private QuotationModel mModel;
    private Messenger mServiceMessenger;
    private Messenger mActivityMessenger;
    private ThreadQuotation Mythread;
    private boolean isStop;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mModel=new QuotationModel(this);
        HandlerThread handlerThread=new HandlerThread("serciceMsg");
        handlerThread.start();
        mHandler=new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                try {
                    if (msg.what==0x00){
                        goodsType="0";
                        isRefresh=true;
                    }else if (msg.what==0X01){
                        goodsType="1";
                        isRefresh=true;
                    }else if (msg.what==0X02){
                        goodsType="2";
                        isRefresh=true;
                    }else if (msg.what==0X03){
                        if (mActivityMessenger==null){
                            mActivityMessenger=msg.replyTo;
                        }
                        goodsType="0";
                        Mythread = new ThreadQuotation();
                        task= new TimeTask();
                        Mythread.start();
                        task.start();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        mServiceMessenger=new Messenger(mHandler);

    }

    private boolean isRefresh=true;
    class ThreadQuotation extends Thread{
        @Override
        public void run() {
            super.run();
            while (!isStop){
                if (isRefresh){
                    mModel.getQuotationData(goodsType, new QuotationModel.ResultQuotationDataListener() {
                        @Override
                        public void ResultData(boolean isSuccess, final List<QuotationBean> data) {
                            if (isSuccess){
                                Message message=new Message();
                                message.what= Const.STATE_DATA_SUCCESS;
                                message.obj=data;
                                try {
                                    mActivityMessenger.send(message);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    isRefresh=false;
                }



            }
        }
    }

    class TimeTask extends Thread{
        @Override
        public void run() {
            super.run();
            while (!isStop){
                SystemClock.sleep(2000);
                isRefresh=true;
            }
        }
    }


//    private void initTimeTask(final String type){
//        if (timer!=null){
//            timer.cancel();
//        }
//        if (task!=null){
//            task.cancel();
//        }
//        timer=new Timer();
//        task=new TimerTask() {
//            @Override
//            public void run() {
//                mModel.getQuotationData(type, new QuotationModel.ResultQuotationDataListener() {
//                    @Override
//                    public void ResultData(boolean isSuccess, final List<QuotationBean> data) {
//                        if (isSuccess){
//                            Message message=new Message();
//                            message.what= Const.STATE_DATA_SUCCESS;
//                            message.obj=data;
//                            try {
//                                mActivityMessenger.send(message);
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//            }
//        };
//        timer.schedule(task,100,3000);
//
//    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop =true;
    }

}
