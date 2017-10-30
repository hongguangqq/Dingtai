package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.GoldBean;
import com.jyt.baseapp.bean.IntoGlodeBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

import java.util.List;

/**
 * @author LinWei on 2017/9/12 14:43
 */
public class GoldModel {
    /**
     * 搜索15条入金数据
     * @param context
     * @param lastId
     */
    public void getIntoGoldData(Context context,String lastId , final ResultGoldDataListener listener){
        HttpUtil.getGoldData(context, "1", lastId, "0", new HttpUtil.OnHttpListener<List<GoldBean>>() {
            @Override
            public void onResponse(BaseBean<List<GoldBean>> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);
                    }else {
                        if (!response.token){
                            listener.LoseLogin();
                        }
                        listener.ResultData(false,null);
                        BaseUtil.makeText("数据获取失败");
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("数据获取失败");
                }
            }
        });
    }

    /**
     * 搜索15条出金数据
     * @param context
     * @param lastId
     */
    public void getOutGoldData(Context context,String lastId , final ResultGoldDataListener listener){
        HttpUtil.getGoldData(context, "2", lastId, "0", new HttpUtil.OnHttpListener<List<GoldBean>>() {
            @Override
            public void onResponse(BaseBean<List<GoldBean>> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);
                    }else {
                        if (!response.token){
                            listener.LoseLogin();
                        }
                        listener.ResultData(false,null);
                        BaseUtil.makeText("数据获取失败");
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("数据获取失败");
                }
            }
        });
    }

    /**
     * 搜索全部入金数据
     * @param context
     */
    public void getIntoAllGoldData(Context context , final ResultGoldDataListener listener){
        HttpUtil.getGoldData(context, "1", "0", "1", new HttpUtil.OnHttpListener<List<GoldBean>>() {
            @Override
            public void onResponse(BaseBean<List<GoldBean>> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);
                    }else {
                        if (!response.token){
                            listener.LoseLogin();
                        }
                        listener.ResultData(false,null);
                        BaseUtil.makeText("数据获取失败");
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("数据获取失败");
                }
            }
        });
    }

    /**
     * 搜索全部出金数据
     * @param context
     */
    public void getOutAllGoldData(Context context , final ResultGoldDataListener listener){
        HttpUtil.getGoldData(context, "2", "0", "1", new HttpUtil.OnHttpListener<List<GoldBean>>() {
            @Override
            public void onResponse(BaseBean<List<GoldBean>> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data);
                    }else {
                        if (!response.token){
                            listener.LoseLogin();
                        }
                        listener.ResultData(false,null);
                        BaseUtil.makeText("数据获取失败");
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("数据获取失败");
                }
            }
        });
    }

    public interface ResultGoldDataListener{
        void ResultData(boolean isSuccess,List<GoldBean> data);
        void LoseLogin();
    }

    /**
     * 入金操作
     * @param context
     * @param cash
     * @param bankCode
     * @param pwd
     * @param listener
     */
    public void IntoGlodePost(Context context, String cash, String bankCode, String pwd, final ResultIntoGlodeListener listener){
        HttpUtil.IntoGlodePost(context, cash, bankCode, pwd, new HttpUtil.OnHttpListener<IntoGlodeBean>() {
            @Override
            public void onResponse(BaseBean<IntoGlodeBean> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true,response.data.cashId);
                        BaseUtil.makeText("入金成功");
                    }else {
                        listener.ResultData(false,null);
                        BaseUtil.makeText("入金失败");
                    }
                }else {
                    listener.ResultData(false,null);
                    BaseUtil.makeText("入金失败");
                }
            }
        });
    }

    public interface ResultIntoGlodeListener {
        void ResultData(boolean isSuccess,String cashId);
    }

    /**
     * 出金操作
     * @param context
     * @param cash
     * @param pwd
     * @param listener
     */
    public void OutGlodePost(Context context,String cash,String pwd, final ResultOutGlodeListener listener){
        HttpUtil.OutGlodePost(context, cash, pwd, new HttpUtil.OnHttpListener<Boolean>() {
            @Override
            public void onResponse(BaseBean<Boolean> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true);
                        BaseUtil.makeText("出金成功");
                    }else {
                        listener.ResultData(false);
                        BaseUtil.makeText("出金失败");
                    }
                }else {
                    listener.ResultData(false);
                    BaseUtil.makeText("出金失败");
                }
            }
        });
    }

    public interface ResultOutGlodeListener {
        void ResultData(boolean isSuccess);

    }























}
