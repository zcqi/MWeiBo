package com.example.zhangzhao.mweibo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.model.Status;
import com.example.zhangzhao.mweibo.ui.fragment.StatusFragment;

/**
 * Created by zhangzhao on 2015/8/16.
 */
public class StatusActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        StatusFragment statusFragment = new StatusFragment();
        Status status= (Status) getIntent().getSerializableExtra("status");
        Bundle bundle = new Bundle();
        bundle.putSerializable("status", status);
        statusFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content,statusFragment).commit();
    }
}
