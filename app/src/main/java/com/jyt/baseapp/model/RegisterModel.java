package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.bean.BankBean;
import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.bean.ContractBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;
import com.jyt.baseapp.util.StringUtils;

import java.io.File;
import java.util.List;

/**
 * @author LinWei on 2017/9/1 10:11
 */
public class RegisterModel {
    /**
     * 获取银行数据
     * @param context
     * @param listener
     */
    public void GetBankData(Context context, final BankDataResultListener listener){
        HttpUtil.GetBankData(context, new HttpUtil.OnHttpListener<List<BankBean>>() {
            @Override
            public void onResponse(BaseBean<List<BankBean>> response, boolean isSuccess, Exception e) {
                if (listener!=null){
                    if (isSuccess){
                        if (response.ret){
                            listener.ResultData(response.data,true);
                        }else {
                            listener.ResultData(null,false);
                        }
                    }else {
                        listener.ResultData(null,false);
                    }
                }
            }
        });
    }
    public interface BankDataResultListener{
        void ResultData(List<BankBean> data,boolean isSuccess);
    }

    /**
     * 上传合同文件
     * @param context
     * @param file
     * @param listener
     */
    public void UploadContract(Context context, File file, final UpLoadResult listener){

        HttpUtil.UploadContract(context, file,false,new HttpUtil.OnHttpListener<ContractBean>() {
            @Override
            public void onResponse(BaseBean<ContractBean> response, boolean isSuccess, Exception e) {
                if (listener!=null){
                    if (isSuccess){
                        if (response.ret){
                            listener.ResultPath(response.data.url,true);
                        }else {
                            listener.ResultPath(null,false);
                        }

                    }else {
                        listener.ResultPath(null,false);
                    }
                }
            }
        });
    }

    public interface  UpLoadResult{
        void ResultPath(String url,boolean isSuccess);

    }

    /**
     * 注册新用户
     * @param context
     * @param user
     * @param pwd
     * @param repwd
     * @param name
     * @param mobile
     * @param agent
     * @param idCard
     * @param idCarAddress
     * @param bankName
     * @param bankBranch
     * @param bankNum
     * @param contract
     * @param sex
     */
    public void RegisterNewUser(Context context, String user
            , String pwd
            , final String repwd
            , String name
            , String mobile
            , String agent
            , String idCard
            , String idCarAddress
            , String bankName
            , String bankBranch
            , String bankNum
            , String contract
            , String sex, final RegisterResult listener){
        if (!CheckIsNull(user,pwd,repwd,name,mobile,agent,idCard,idCarAddress,bankName,bankBranch,bankNum,contract,sex)){
            if (!pwd.equals(repwd)){
                BaseUtil.makeText("两次密码不一致");
                return;
            }
            if (!BaseUtil.checkCellphone(mobile)){
                BaseUtil.makeText("手机号码格式错误");
                return;
            }
            HttpUtil.RegisterNewUser(context, user, pwd, name, mobile, agent, idCard, idCarAddress, bankName, bankBranch, bankNum, contract, sex, new HttpUtil.OnHttpListener<Boolean>() {
                @Override
                public void onResponse(BaseBean<Boolean> response, boolean isSuccess, Exception e) {
                    if (listener!=null){
                        if (isSuccess){
                            if (response.ret){
                                BaseUtil.makeText("注册成功");
                                listener.ResultData(true);
                            }else {
                                BaseUtil.makeText("注册失败，"+response.forUser);
                                listener.ResultData(false);
                            }
                        }else {
                            BaseUtil.makeText("注册失败，请重试");
                            listener.ResultData(false);
                        }
                    }
                }
            });

        }else {
            BaseUtil.makeText("您还有信息未填写");
            return;
        }

    }

    /**
     * 验证非空
     * @param values
     * @return
     */
    public boolean CheckIsNull(String... values){
        for (String value: values) {
            if (StringUtils.isEmpty(value)){
                return true;
            }
        }
        return false;
    }

    public interface RegisterResult{
        void ResultData(boolean isSuccess);
    }



}
