package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.CaseBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

import java.util.List;

/**
 * @author LinWei on 2017/9/11 17:21
 */
public class PositionModel {

    /**
     * 得到用户的订单数据
     * @param context
     * @param listener
     */
    public void getPositionData(Context context, final getCaseDataListener listener){
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
                        listener.ResultData(false,null);
                        BaseUtil.makeText("数据获取失败，"+response.forUser);
                    }
                }else {
                    if (!response.token){
                        listener.LoseLogin();
                    }
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
}
