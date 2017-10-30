package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.CaseBean;
import com.jyt.baseapp.bean.MoneyBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

import java.util.List;

/**
 * @author LinWei on 2017/9/8 10:27
 */
public class CaseModel {
    /**
     * 得到用户的订单数据
     * @param context
     * @param listener
     */
    public void getCaseData(Context context, final getCaseDataListener listener){
        HttpUtil.getCaseData(context, "1", new HttpUtil.OnHttpListener<List<CaseBean>>() {
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
                        BaseUtil.makeText("订单数据获取失败，"+response.forUser);
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("订单数据获取失败");
                }
            }
        });
    }

    public interface getCaseDataListener{
        void ResultData(boolean isSuccess,List<CaseBean> data);
        void LoseLogin();
    }

    /**
     * 获取可用资金和商品编码列表
     * @param context
     * @param listener
     */
    public void getUseMoney(Context context, final getMoneyDataListener listener){
        HttpUtil.getUseMoney(context, new HttpUtil.OnHttpListener<MoneyBean>() {
            @Override
            public void onResponse(BaseBean<MoneyBean> response, boolean isSuccess, Exception e) {
                if (listener==null){
                    return;
                }
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);
                    }else {
                        listener.ResultData(false,null);

                    }
                }else {
                    listener.ResultData(false,null);

                }
            }
        });
    }

    public interface getMoneyDataListener{
        void ResultData(boolean isSuccess,MoneyBean data);
    }

    /**
     * 提交订单，成功返回true，失败返回false
     * @param context
     * @param goodsId
     * @param orderType
     * @param price
     * @param num
     * @param listener
     */
    public void CreateCaseData(Context context, String goodsId, String orderType, String price, final String num, final CreateCaseDataListener listener){
        HttpUtil.CreateCaseData(context, goodsId, orderType, price, num, new HttpUtil.OnHttpListener<Boolean>() {
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
                        BaseUtil.makeText("新增订单失败，"+response.forUser);
                    }
                }else {
                    listener.ResultData(false);
                    BaseUtil.makeText("新增订单失败，请重试");
                }
            }
        });
    }

    public interface CreateCaseDataListener{
        void ResultData(boolean isSuccess);
    }

    /**
     * 撤销订立订单
     * @param context
     * @param type
     * @param orderNo
     * @param listener
     */
    public void RevokeCase(Context context, String type, String orderNo, final RevokeCaseListener listener){
        HttpUtil.RevokeCase(context, type, orderNo, new HttpUtil.OnHttpListener<Boolean>() {
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
                        BaseUtil.makeText("撤销订单失败，"+response.forUser);
                    }
                }else {
                    listener.ResultData(false);
                    BaseUtil.makeText("撤销订单失败，请重试");
                }
            }
        });
    }

    public interface RevokeCaseListener{
        void ResultData(boolean isSuccess);
    }


}
