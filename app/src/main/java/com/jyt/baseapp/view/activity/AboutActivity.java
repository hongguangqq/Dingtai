package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jyt.baseapp.R;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.iv_about_back)
    ImageView mIvAboutBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIvAboutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
            }
        });
    }
}
