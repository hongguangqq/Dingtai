package com.jyt.baseapp.api;

import android.app.Activity;
import android.content.Intent;

import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.activity.LoginActivity;

/**
 * @author LinWei on 2017/9/1 11:16
 */
public class Const {
    public static final int STATE_DATA_SUCCESS=0X01;//获取数据成功
    public static final int STATE_DELETE_SUCCESS=0X02;//删除条目成功
    public static final int STATE_UPLOAD_SUCCESS=0X03;//上传条目成功
    public static final int STATE_CODE_SUCCESS=0X04;//上传条目成功
    public static final int STATE_TRANSFER_SUCCESS=0X05;//转让订单成功
    public static final int STATE_REVOKE_SUCCESS=0X06;//撤销转让订单成功
    public static final int STATE_ADDDATA_SUCCESS=0X07;//增加金额数据成功
    public static final int STATE_TODAY_SUCCESS=0X08;//获取当日数据成功
    public static boolean isChangePic;//是否修改了头像-用于修改头像后，主界面同步更新

    public static final String Username="username";
    public static final String Name="name";
    public static final String Pwd="username";
    public static final String TokenSession="tokenSession";
    public static final String CaseType="type";
    public static final String IsLogin="islogin";//登录状态
    public static final String PicHead="pichead";//头像
    public static final String Action_TRANSFER="action_transfer";//可转让的Action标识
    public static final String Action_TRANSFERING="action_transfering";//转让中的Action标识
    //K图的十二种模式
    public static final int MODE_MACD=0;
    public static final int MODE_TRIX=1;
    public static final int MODE_BRAR=2;
    public static final int MODE_CR=3;
    public static final int MODE_VR=4;
    public static final int MODE_OBV=5;
    public static final int MODE_EMV=6;
    public static final int MODE_VOL=7;
    public static final int MODE_RSI=8;
    public static final int MODE_KDJ=9;
    public static final int MODE_CCI=10;
    public static final int MODE_MTM=11;
    public static final int MODE_PSY=12;

    /**
     * 撤销登录的操作,消去登录信息并跳转到登录界面
     */
    public static void LoseLogin(Activity activity){
        BaseUtil.setSpString(Const.Pwd,"");//消去密码
        BaseUtil.setSpString(Const.TokenSession,"");//消去标识符
        BaseUtil.setSpString(Const.Username,"");//消去用户名
        BaseUtil.setSpString(Const.Name,"");//消去姓名
        BaseUtil.setSpBoolean(Const.IsLogin,false);//登录状态置为false
        activity.startActivity(new Intent(activity,LoginActivity.class));//跳转到登录界面
        activity.finish();
    }
}
