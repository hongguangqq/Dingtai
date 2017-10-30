package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FragmentViewPagerAdapter;
import com.jyt.baseapp.view.fragment.BaseFragment;
import com.jyt.baseapp.view.fragment.TransferFragment;
import com.jyt.baseapp.view.fragment.TransferingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author LinWei on 2017/9/9 15:52
 */
public class TransferActivity extends BaseActivity {
    @BindView(R.id.iv_transfer_back)
    ImageView mIv_Back;
    @BindView(R.id.tab_transfer_tab)
    TabLayout mTab_Tab;
    @BindView(R.id.vp_transfer_container)
    ViewPager mVp_Container;

    private List<String> tabTitle;
    private List<BaseFragment> flist;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer;
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
        initListener();
    }

    private void init() {
        tabTitle=new ArrayList<>();
        flist=new ArrayList<>();
        tabTitle.add("可转让");
        tabTitle.add("转让中");
        TransferFragment transferFragment=new TransferFragment();
        TransferingFragment transferingFragment=new TransferingFragment();
        flist.add(transferFragment);
        flist.add(transferingFragment);


    }

    private void initVp(){
        mTab_Tab.setTabMode(TabLayout.MODE_FIXED);
        mTab_Tab.addTab(mTab_Tab.newTab().setText(tabTitle.get(0)));//加标题
        mTab_Tab.addTab(mTab_Tab.newTab().setText(tabTitle.get(1)));//加标题
        FragmentViewPagerAdapter adapter=new FragmentViewPagerAdapter(getSupportFragmentManager());
        adapter.setFragments(flist);
        adapter.setTitles(tabTitle);
        mVp_Container.setAdapter(adapter);
        mTab_Tab.setupWithViewPager(mVp_Container);
        mTab_Tab.setTabsFromPagerAdapter(adapter);
    }
    private void initListener() {
        mIv_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_back_in,R.anim.slide_back_out);
            }
        });
    }
}
