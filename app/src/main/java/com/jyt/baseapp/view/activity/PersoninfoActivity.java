package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.PersonInfoBean;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;

public class PersoninfoActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.iv_person_back)
    ImageView mIvPersonBack;
    @BindView(R.id.civ_person_headpic)
    CircleImageView mCivPersonHeadpic;
    @BindView(R.id.tv_person_balance)
    TextView mTvPersonBalance;
    @BindView(R.id.tv_person_price)
    TextView mTvPersonPrice;
    @BindView(R.id.tv_person_assets)
    TextView mTvPersonAssets;
    @BindView(R.id.tv_person_deposit)
    TextView mTvPersonDeposit;
    @BindView(R.id.tv_person_transferEarn)
    TextView mTvPersonTransferEarn;
    @BindView(R.id.tv_person_trueEarn)
    TextView mTvPersonTrueEarn;
    @BindView(R.id.tv_person_bank)
    TextView mTvPersonBank;
    @BindView(R.id.rl_person_binding)
    RelativeLayout mRlPersonBinding;
    @BindView(R.id.tv_person_banknum)
    TextView mTvPersonBanknum;
    @BindView(R.id.civ_person_banklogo)
    CircleImageView mCivLogo;
    @BindView(R.id.fl_person_banknum)
    RelativeLayout mFlPersonBanknum;
    @BindView(R.id.tv_person_time)
    TextView mTvPersonTime;
    private PersonModel mModel;
    private File pictureFile;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personinfo;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new PersonModel();
        initFile();
        initSetting();
        initListener();
    }

    /**
     * 创建pic文件
     */
    private void initFile(){
        pictureFile =new File(getCacheDir(),"zdlwlw.jpg");
        Log.e("ZDLW",pictureFile.getAbsolutePath());
        if (!pictureFile.exists()){
            try {
                pictureFile.getParentFile().mkdirs();
                pictureFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initSetting() {
        PersonInfoBean infoData= (PersonInfoBean) getIntent().getSerializableExtra("infoData");
        if (infoData!=null){
            mTvPersonBalance.setText(infoData.balance);
            mTvPersonPrice.setText(infoData.procedurePrice);
            mTvPersonAssets.setText(infoData.totalAssets);
            mTvPersonDeposit.setText(infoData.depositCash);
            mTvPersonTransferEarn.setText(infoData.transferEarn);
            mTvPersonTrueEarn.setText(infoData.trueEarn);
            mTvPersonBank.setText(infoData.bankName);
            mTvPersonBanknum.setText(infoData.bankNum);
            mTvPersonTime.setText("注册时间："+infoData.createdTime);
            Log.e("ZDLW","IMg:"+infoData.userImg);
            Glide.with(PersoninfoActivity.this)
                    .load(infoData.userImg)
                    .error(R.mipmap.icon_head)
                    .into(mCivPersonHeadpic);
            //银行logo
            Glide.with(PersoninfoActivity.this)
                    .load(infoData.logo)
                    .into(mCivLogo);


        }

    }

    private void initListener(){
        mIvPersonBack.setOnClickListener(this);
        mCivPersonHeadpic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_person_back:
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
                break;

            case R.id.civ_person_headpic:
                gallery();
                break;

            default:
                break;
        }
    }

    /**
     * 按下头像的操作
     */
    public void gallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PHOTO_REQUEST_GALLERY);
    }

    /**
     * 从相册返回图片的相关处理
     */
    private void Logigallery(Intent data){
        if (data!=null){
            Uri uri=data.getData();
            crop(uri);

        }
    }



    /*
    * 剪切图片
    */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PHOTO_REQUEST_GALLERY:
                Logigallery(data);
                break;
            case PHOTO_REQUEST_CUT:
                if (data!=null){
                    Bitmap bitmap=data.getParcelableExtra("data");
                    savePicture(bitmap,pictureFile);
                    mModel.ModifyPic(PersoninfoActivity.this, pictureFile, new PersonModel.ResultModifyPicListener() {
                        @Override
                        public void ResultData(boolean isSuccess, String url) {
                            if (isSuccess){
                                Glide.with(PersoninfoActivity.this)
                                        .load(url)
                                        .into(mCivPersonHeadpic);
                                Const.isChangePic=true;//通知主界面访问网络更新头像
                                BaseUtil.setSpString(Const.PicHead,url);
                            }
                        }
                    });

                }
                break;
        }
    }



    //保存图片到本地,以便今后显示头像
    public void savePicture(Bitmap bitmap,File picture){
        if (picture.exists()){
            //删除上一张头像
            picture.delete();
        }
        BufferedOutputStream ops=null;
        try {
            ops=new BufferedOutputStream(new FileOutputStream(picture));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,ops);
            ops.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ops!=null){
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
