package com.example.zhangzhao.mweibo.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.model.User;
import com.example.zhangzhao.mweibo.ui.fragment.UserPhotosFragment;
import com.example.zhangzhao.mweibo.ui.fragment.UserTimelineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhao on 2015/8/15.
 */
public class UserActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = (User) getIntent().getSerializableExtra("user");
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(user.getScreen_name());
        ImageView ivImage = (ImageView) findViewById(R.id.ivImage);
        Glide.with(ivImage.getContext())
                .load(user.getAvatar_large())
                .fitCenter()
                .into(ivImage);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        UserTimelineFragment fragment1=new UserTimelineFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("user", user);
        fragment1.setArguments(bundle);
        UserPhotosFragment fragment2=new UserPhotosFragment();
        fragment2.setArguments(bundle);
        adapter.addFragment(fragment1,"微博");
        adapter.addFragment(fragment2, "相册");
        mViewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("微博"));
        tabLayout.addTab(tabLayout.newTab().setText("相册"));
        tabLayout.setupWithViewPager(mViewPager);

    }
    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


}
