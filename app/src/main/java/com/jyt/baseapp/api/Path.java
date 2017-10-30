package com.jyt.baseapp.api;

/**
 * @author LinWei on 2017/8/30 11:05
 */
public class Path {
    public static final String URL_Base="http://119.23.66.37/finance/api/";
    public static final String URL_Login=URL_Base+"login/do_login";//登录
    public static final String URL_Bank=URL_Base+"login/get_bank";//获取银行列表数据
    public static final String URL_UpdataCtract=URL_Base+"login/upload_contract";// 上传合同，并回传下载地址
    public static final String URL_Register=URL_Base+"login/register";//注册
    public static final String URL_Hint=URL_Base+"content/";//获取温馨提示
    public static final String URL_Quotation=URL_Base+"market/";//获取行情数据
    public static final String URL_PersonInfo=URL_Base+"user/";//获取个人信息
    public static final String URL_SwitchIMG=URL_Base+"user/change_user_img";//获取交易信息
    public static final String URL_CaseData=URL_Base+"user/get_order";//获取订单数据
    public static final String URL_UseMoney=URL_Base+"market/get_basic_buy";// 可用金额
    public static final String URL_CreateCase=URL_Base+"order/add";// 创立订单
    public static final String URL_RevokeCase=URL_Base+"order/remove";//撤销订单
    public static final String URL_TransferCase=URL_Base+"order/transfer";//转让订单
    public static final String URL_FwData=URL_Base+"user/order_water";//交易流水数据
    public static final String URL_GoldData=URL_Base+"user/cash_water";//出入金数据
    public static final String URL_InfoData=URL_Base+"market/goods_list";//商品资讯列表
    public static final String URL_MODIFYPWD=URL_Base+"user/reset_pwd";//修改密码
    public static final String URL_IntoGold=URL_Base+"user/in_money";//入金
    public static final String URLWebIntoGold=URL_Base+"user/pay?cashId=";//入金支付网页
    public static final String URL_OutGold=URL_Base+"user/out_money";//出金
    public static final String URL_KChart=URL_Base+"market/get_k";//K图数据
    public static final String URL_TodayData=URL_Base+"market/today";// 行情当日定理/转让详情
    public static final String URL_FenShiChart=URL_Base+"market/get_fenshi";// 分时图信息
}
