package com.jyt.baseapp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FragmentViewPagerAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.PersonInfoBean;
import com.jyt.baseapp.model.ContentModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.fragment.BaseFragment;
import com.jyt.baseapp.view.fragment.InformationFragment;
import com.jyt.baseapp.view.fragment.QuotationFragment;
import com.jyt.baseapp.view.fragment.TransactionFragment;
import com.jyt.baseapp.view.widget.CircleImageView;
import com.jyt.baseapp.view.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;

public class ContentActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.civ_menu_headpic)
    CircleImageView mCivMenuHeadpic;
    @BindView(R.id.tv_menu_name)
    TextView mTvMenuName;
    @BindView(R.id.tv_menu_account)
    TextView mTvMenuAccount;
    @BindView(R.id.ll_menu_info)
    LinearLayout mLlMenuInfo;
    @BindView(R.id.ll_menu_rpsw)
    LinearLayout mLlMenuRpsw;
    @BindView(R.id.ll_menu_about)
    LinearLayout mLlMenuAbout;
    @BindView(R.id.ll_menu_unlogin)
    LinearLayout mLlMenuUnlogin;
    @BindView(R.id.btn_menu_login)
    Button mBtnMenuLogin;
    @BindView(R.id.civ_content_headpic)
    CircleImageView mCivContentHeadpic;
    @BindView(R.id.tv_content_title)
    TextView mTvContentTitle;
    @BindView(R.id.tv_content_hint)
    TextView mTvContentHint;
    @BindView(R.id.iv_content_menu)
    ImageView mIvContentMenu;
    @BindView(R.id.vp_content_vp)
    NoScrollViewPager mVpContentVp;
    @BindView(R.id.iv_content_quotation)
    ImageView mIvContentQuotation;
    @BindView(R.id.tv_content_quotation)
    TextView mTvContentQuotation;
    @BindView(R.id.iv_content_transaction)
    ImageView mIvContentTransaction;
    @BindView(R.id.tv_content_transaction)
    TextView mTvContentTransaction;
    @BindView(R.id.iv_content_information)
    ImageView mIvContentInformation;
    @BindView(R.id.tv_content_information)
    TextView mTvContentInformation;
    @BindView(R.id.drawer)
    DuoDrawerLayout mDrawer;
    @BindView(R.id.ll_content_quotation)
    LinearLayout mllContentQuotation;
    @BindView(R.id.ll_content_transaction)
    LinearLayout mllContentTransaction;
    @BindView(R.id.ll_content_information)
    LinearLayout mllContentInformation;
    private ContentModel mModel;
    private int mindex;//记录滑动的index
    private List<BaseFragment> mFragmentList;
    private FragmentViewPagerAdapter mAdapter;
    private PopupWindow mPopupWindow;
    private QuotationFragment mQuotationFragment;
    private TransactionFragment mTransactionFragment;
    private InformationFragment mInformationFragment;
    private AlertDialog mDialog;
    private PersonInfoBean infoData;//用于传输数据个个人信息界面
    private TextView mTv_pppall;
    private TextView mTv_pppmorning;
    private TextView mTv_pppnight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_content;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initVp();
        initHint();
        initInfo();
        initPop();
        initListener();
        initDialog();

    }

    private void init(){
        mModel=new ContentModel();
        mFragmentList=new ArrayList<>();
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
    }

    private void initVp(){
        mQuotationFragment = new QuotationFragment();
        mTransactionFragment = new TransactionFragment();
        mInformationFragment = new InformationFragment();
        mFragmentList.add(mQuotationFragment);
        mFragmentList.add(mTransactionFragment);
        mFragmentList.add(mInformationFragment);
        mAdapter.setFragments(mFragmentList);
        mVpContentVp.setAdapter(mAdapter);
        mVpContentVp.setOffscreenPageLimit(3);
    }

    /**
     * 加载温馨提示
     */
    public void initHint(){
        mModel.getHint(this, "1", new ContentModel.ResultHintListener() {
            @Override
            public void ResultData(boolean isSuccess, final String warnTips, String dealRisk, String notification, String aboutUs) {
                if (isSuccess){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mTvContentHint.setText(warnTips);
                        }
                    });
                }
            }
        });
    }


    /**
     * 获取个人用户账号信息
     */
    private void initInfo() {
       if (BaseUtil.getSpBoolean(Const.IsLogin)){
           mModel.getPersonInfo(this, new ContentModel.ResultPersonInfoListener() {
               @Override
               public void ResultData(boolean isSuccess, final PersonInfoBean data) {
                   if (isSuccess){
                       infoData=data;
                       new Handler().post(new Runnable() {
                           @Override
                           public void run() {
                               mTvMenuName.setText(data.name);//用户姓名
                               mTvMenuAccount.setText("账号："+data.username);//账户名
                               Glide.with(ContentActivity.this)//用户主菜单头像
                                       .load(data.userImg)
                                       .error(R.mipmap.icon_head)
                                       .into(mCivContentHeadpic);
                               Glide.with(ContentActivity.this)//用户侧菜单头像
                                       .load(data.userImg)
                                       .error(R.mipmap.icon_head)
                                       .into(mCivMenuHeadpic);
                               BaseUtil.setSpString(Const.PicHead,data.userImg);//保存头像图片url

                           }
                       });
                   }else {
                       //当无法获取数据时，需要上次记录的用户名和姓名
                       mTvMenuName.setText(BaseUtil.getSpString(Const.Name));//用户姓名
                       mTvMenuAccount.setText("账号："+BaseUtil.getSpString(Const.Username));//账户名
                   }
               }
           });
       }else {
           mTvMenuName.setText("未登录");//用户姓名
           mTvMenuAccount.setText("账号：");//账户名
           mBtnMenuLogin.setVisibility(View.VISIBLE);
       }
    }

    private void initListener(){
        mllContentQuotation.setOnClickListener(this);
        mllContentTransaction.setOnClickListener(this);
        mllContentInformation.setOnClickListener(this);
        mCivContentHeadpic.setOnClickListener(this);
        mIvContentMenu.setOnClickListener(this);
        mLlMenuUnlogin.setOnClickListener(this);
        mLlMenuInfo.setOnClickListener(this);
        mLlMenuRpsw.setOnClickListener(this);
        mLlMenuAbout.setOnClickListener(this);
        mLlMenuUnlogin.setOnClickListener(this);
        mBtnMenuLogin.setOnClickListener(this);
        mTv_pppall.setOnClickListener(this);
        mTv_pppmorning.setOnClickListener(this);
        mTv_pppnight.setOnClickListener(this);
    }

    private void initPop(){
        View popView=getLayoutInflater().inflate(R.layout.pop_type,null);
        mTv_pppall= (TextView) popView.findViewById(R.id.tv_all);
        mTv_pppmorning= (TextView) popView.findViewById(R.id.tv_morning);
        mTv_pppnight= (TextView) popView.findViewById(R.id.tv_night);
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 退出登录
     */
    private void initDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.customDialog);
        View dialogView=View.inflate(this,R.layout.dialog_alert,null);
        builder.setView(dialogView);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                dialog.dismiss();
                return true;
            }
        });
        builder.setCancelable(false);
        dialogView.findViewById(R.id.btn_alert_submit).setOnClickListener(this);
        dialogView.findViewById(R.id.btn_alert_cancel).setOnClickListener(this);

        mDialog=builder.create();


//        final WindowManager.LayoutParams attrs = mDialog.getWindow().getAttributes();
//        attrs.width = BaseUtil.dip2px(175);
//        attrs.height = BaseUtil.dip2px(97);
//        if (mDialog != null) {
//            mDialog.onWindowAttributesChanged(attrs);
//        }
//
    }

    private void selectorIndex(int index){
        if (index==mindex){
            return;
        }
        mIvContentQuotation.setImageDrawable(getResources().getDrawable(R.mipmap.quotation_off));
        mTvContentQuotation.setTextColor(getResources().getColor(R.color.color_b1));
        mIvContentTransaction.setImageDrawable(getResources().getDrawable(R.mipmap.transaction_off));
        mTvContentTransaction.setTextColor(getResources().getColor(R.color.color_b1));
        mIvContentInformation.setImageDrawable(getResources().getDrawable(R.mipmap.information_off));
        mTvContentInformation.setTextColor(getResources().getColor(R.color.color_b1));
        if (index==0){
            mIvContentQuotation.setImageDrawable(getResources().getDrawable(R.mipmap.quotation_on));
            mTvContentQuotation.setTextColor(getResources().getColor(R.color.color_o1));
            mTvContentTitle.setVisibility(View.VISIBLE);
            mTvContentHint.setVisibility(View.VISIBLE);
            mIvContentMenu.setVisibility(View.VISIBLE);
        }else if (index==1){
            mIvContentTransaction.setImageDrawable(getResources().getDrawable(R.mipmap.transaction_on));
            mTvContentTransaction.setTextColor(getResources().getColor(R.color.color_o1));
            mTvContentTitle.setVisibility(View.GONE);
            mTvContentHint.setVisibility(View.GONE);
            mIvContentMenu.setVisibility(View.GONE);
        }else {
            mIvContentInformation.setImageDrawable(getResources().getDrawable(R.mipmap.information_on));
            mTvContentInformation.setTextColor(getResources().getColor(R.color.color_o1));
            mTvContentTitle.setVisibility(View.GONE);
            mTvContentHint.setVisibility(View.GONE);
            mIvContentMenu.setVisibility(View.GONE);
        }
        mindex=index;
        mVpContentVp.setCurrentItem(index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_content_headpic:
                if (mDrawer.isDrawerOpen()){
                    mDrawer.closeDrawer();
                }else {
                    mDrawer.openDrawer();
                }
                break;
            case R.id.ll_content_quotation:
                selectorIndex(0);
                break;
            case R.id.ll_content_transaction:
                if (BaseUtil.getSpBoolean(Const.IsLogin)){
                    selectorIndex(1);
                }else {
                    BaseUtil.makeText("请登录");
                }

                break;
            case R.id.ll_content_information:

                if (BaseUtil.getSpBoolean(Const.IsLogin)){
                    selectorIndex(2);
                }else {
                    BaseUtil.makeText("请登录");
                }
                break;

            case R.id.iv_content_menu:
                mPopupWindow.showAsDropDown(mIvContentMenu,0,0);
                break;
            case R.id.tv_all:
                mQuotationFragment.SwitchGoodType(0);
                setPopBg(0);
//                BaseUtil.makeText("当前显示为全盘");
                break;
            case R.id.tv_morning:
                mQuotationFragment.SwitchGoodType(1);
                setPopBg(1);
//                BaseUtil.makeText("当前显示为白盘");
                break;
            case R.id.tv_night:
                mQuotationFragment.SwitchGoodType(2);
                setPopBg(2);
//                BaseUtil.makeText("当前显示为晚盘");
                break;
            case R.id.ll_menu_info:
                if (BaseUtil.getSpBoolean(Const.IsLogin)){
                    Intent intent = new Intent(ContentActivity.this,PersoninfoActivity.class);
                    intent.putExtra("infoData",infoData);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                }else {
                    BaseUtil.makeText("请登录");
                }
                break;
            case R.id.ll_menu_rpsw:
                if (BaseUtil.getSpBoolean(Const.IsLogin)){
                    startActivity(new Intent(ContentActivity.this,ModifyActivity.class));
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                }else {
                    BaseUtil.makeText("请登录");
                }
                break;
            case R.id.ll_menu_about:
                startActivity(new Intent(ContentActivity.this,AboutActivity.class));
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                break;
            case R.id.ll_menu_unlogin:
                if (BaseUtil.getSpBoolean(Const.IsLogin)){
                    mDrawer.closeDrawer();//关闭侧滑栏
                    mDialog.show();
                    mDialog.getWindow().setLayout(BaseUtil.dip2px(200),BaseUtil.dip2px(200));
                }

                break;
            case R.id.btn_alert_submit:
                Const.LoseLogin(ContentActivity.this);
                finish();//
                break;
            case R.id.btn_alert_cancel:
                mDialog.dismiss();
                break;
            case R.id.btn_menu_login:
                startActivity(new Intent(ContentActivity.this,LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    private void setPopBg(int select){
        mTv_pppall.setBackgroundColor(getResources().getColor(R.color.color_b4));
        mTv_pppmorning.setBackgroundColor(getResources().getColor(R.color.color_b4));
        mTv_pppnight.setBackgroundColor(getResources().getColor(R.color.color_b4));
        switch (select) {
            case 0:
                mTv_pppall.setBackgroundColor(getResources().getColor(R.color.color_b2));
                break;
            case 1:
                mTv_pppmorning.setBackgroundColor(getResources().getColor(R.color.color_b2));
                break;
            case 2:
                mTv_pppnight.setBackgroundColor(getResources().getColor(R.color.color_b2));
                break;
            default:
                break;
        }
        mPopupWindow.dismiss();
    }


    /**
     * 双击退出
     */
    private long mPressedTime = 0;
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }
        else{//退出程序
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Const.isChangePic){
            Log.e("ZDLW","改变了");
            initInfo();
            Const.isChangePic=false;
        }
    }
}
