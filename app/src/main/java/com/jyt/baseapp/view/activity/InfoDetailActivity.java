package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.InfoBean;

import butterknife.BindView;

/**
 * @author LinWei on 2017/9/13 10:45
 */
public class InfoDetailActivity extends BaseActivity {
    @BindView(R.id.iv_infodetail_back)
    ImageView mIvBack;
    @BindView(R.id.tv_infodetail_goodsName)
    TextView mTvGoodsName;
    @BindView(R.id.tv_infodetail_goodsCode)
    TextView mTvGoodsCode;
    @BindView(R.id.tv_infodetail_updateTime)
    TextView mTvUpdateTime;
    @BindView(R.id.iv_infodetail_goodsImg)
    ImageView mIvGoodsImg;
    @BindView(R.id.tv_infodetail_goodsDesc)
    TextView mTvGoodsDesc;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_infodetail;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InfoBean data= (InfoBean) getIntent().getSerializableExtra("data");
        if (data!=null){
            mTvGoodsName.setText(data.goodsName);
            mTvGoodsCode.setText("(代码："+data.goodsCode+")");
            mTvUpdateTime.setText(data.updateTime);
            Glide.with(this)
                    .load(data.goodsImg)
                    .into(mIvGoodsImg);
            mTvGoodsDesc.setText(data.goodsDesc);

        }
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
            }
        });
    }
}
