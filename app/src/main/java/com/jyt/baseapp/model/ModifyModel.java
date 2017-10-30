package com.jyt.baseapp.model;

import android.content.Context;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HttpUtil;

/**
 * @author LinWei on 2017/9/13 14:33
 */
public class ModifyModel {

    public void ModifyPassWord(Context context, String oldPwd, String pwd, final String repwd, final ResultModifyListener listener ){
        HttpUtil.ModifyPassWord(context, oldPwd, pwd, repwd, new HttpUtil.OnHttpListener<Boolean>() {
            @Override
            public void onResponse(BaseBean<Boolean> response, boolean isSuccess, Exception e) {
                if (isSuccess){
                    if (response.ret){
                        listener.ResultData(true);
                        BaseUtil.setSpString(Const.Pwd,"");//消去密码
                        BaseUtil.setSpBoolean(Const.IsLogin,false);//登录状态置为false
                        BaseUtil.setSpString(Const.TokenSession,"");//消去标识符
                        BaseUtil.setSpString(Const.Username,"");//消去用户名
                        BaseUtil.setSpString(Const.Name,"");//消去姓名
                    }else {
                        listener.ResultData(false);
                        BaseUtil.makeText("修改密码失败，"+response.forUser);
                    }
                }else {
                    listener.ResultData(false);
                    BaseUtil.makeText("修改密码失败");
                }
            }
        });
    }

    public interface ResultModifyListener{
        void ResultData(boolean isSuccess);
    }
}
