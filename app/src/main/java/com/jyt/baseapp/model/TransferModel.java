package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.CaseBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

import java.util.List;

/**
 * @author LinWei on 2017/9/11 11:12
 */
public class TransferModel {
    /**
     * 获取可转让的订单数据
     * @param context
     * @param listener
     */
    public void getTransferData(Context context, final getCaseDataListener listener){
        HttpUtil.getCaseData(context, "2", new HttpUtil.OnHttpListener<List<CaseBean>>() {
            @Override
            public void onResponse(BaseBean<List<CaseBean>> response, boolean isSuccess, Exception e) {
                if (listener==null){
                    return;
                }
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);
                    }else {
                        if (!response.token){
                            listener.LoseLogin();
                        }
                        listener.ResultData(false,null);
                        BaseUtil.makeText("数据获取失败，"+response.forUser);
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("数据获取失败");
                }
            }
        });
    }

    /**
     * 获取转让中的订单数据
     * @param context
     * @param listener
     */
    public void getTransferingData(Context context, final getCaseDataListener listener){
        HttpUtil.getCaseData(context, "3", new HttpUtil.OnHttpListener<List<CaseBean>>() {
            @Override
            public void onResponse(BaseBean<List<CaseBean>> response, boolean isSuccess, Exception e) {
                if (listener==null){
                    return;
                }
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);
                    }else {
                        if (!response.token){
                            listener.LoseLogin();
                        }
                        listener.ResultData(false,null);
                        BaseUtil.makeText("数据获取失败，"+response.forUser);
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("数据获取失败");
                }
            }
        });
    }

    public interface getCaseDataListener{
        void ResultData(boolean isSuccess,List<CaseBean> data);
        void LoseLogin();
    }



    /**
     * 转让订单
     * @param context
     * @param orderNo
     * @param sellPrice
     * @param listener
     */
    public void TransferCase(Context context, String orderNo, String sellPrice, final ResultTransferListener listener){
        HttpUtil.TransferCase(context, orderNo, sellPrice, new HttpUtil.OnHttpListener<Boolean>() {
            @Override
            public void onResponse(BaseBean<Boolean> response, boolean isSuccess, Exception e) {
                if (listener==null){
                    return;
                }
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true);
                    }else {
                        listener.ResultData(false);
                        BaseUtil.makeText("转让订单失败，"+response.forUser);
                    }
                }else {
                    listener.ResultData(false);
                    BaseUtil.makeText("转让订单失败，请重试");
                }
            }
        });
    }

    /**
     * 撤销订单
     * @param context
     * @param orderNo
     * @param listener
     */
    public void RevokeCase(Context context,String orderNo,final  ResultTransferListener listener){
        HttpUtil.RevokeCase(context, "2", orderNo, new HttpUtil.OnHttpListener<Boolean>() {
            @Override
            public void onResponse(BaseBean<Boolean> response, boolean isSuccess, Exception e) {
                if (listener==null){
                    return;
                }
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true);
                    }else {
                        listener.ResultData(false);
                        BaseUtil.makeText("撤销转让订单失败，"+response.forUser);
                    }
                }else {
                    listener.ResultData(false);
                    BaseUtil.makeText("撤销转让订单失败，请重试");
                }
            }
        });
    }

    public interface ResultTransferListener{
        void ResultData(boolean isSuccess);
    }
}
