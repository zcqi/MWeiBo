package com.example.zhangzhao.mweibo.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zhangzhao.mweibo.AuthException;
import com.example.zhangzhao.mweibo.MWeiBoModule;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.model.Image;
import com.example.zhangzhao.mweibo.model.Status;
import com.example.zhangzhao.mweibo.model.User;
import com.example.zhangzhao.mweibo.service.LoginService;
import com.example.zhangzhao.mweibo.service.MWeiBoService;
import com.example.zhangzhao.mweibo.ui.activity.ImageActivity;
import com.example.zhangzhao.mweibo.ui.activity.NewStatusActivity;
import com.example.zhangzhao.mweibo.ui.activity.StatusActivity;
import com.example.zhangzhao.mweibo.ui.adapter.MainAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by zhangzhao on 2015/8/10.
 */
public class MainFragment extends Fragment implements View.OnClickListener, SwipyRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private SwipyRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private MainAdapter mainAdapter;
    private List<Status> statuses = new ArrayList<Status>();
    private int page = 2;
    @Inject
    protected MWeiBoService mWeiBoService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectGraph.create(MWeiBoModule.class).inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview_fab, container, false);
    }

    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionBar);
        floatingActionButton.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        mainAdapter = new MainAdapter(statuses, getActivity());
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.setItemListener(new MainAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                Status status = statuses.get(postion);
                Intent intent = new Intent(getActivity(), StatusActivity.class);
                intent.putExtra("status", status);
                startActivity(intent);

            }
        });

        mainAdapter.setImageListener(new MainAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                List<Image> images = statuses.get(postion).getPic_urls();
                Intent intent = new Intent(getActivity(), ImageActivity.class);
                intent.putExtra("images", (Serializable) images);
                startActivity(intent);

            }
        });

        mWeiBoService.getStatusService().getFriendsTimeline(1).observeOn(AndroidSchedulers.mainThread()).subscribe(statusesWrapper -> {
                    statuses = statusesWrapper.getStatuses();
                    mainAdapter.setStatuses(statuses);
                    mainAdapter.notifyDataSetChanged();
                },
                throwable -> {
                    handleError(throwable);
                });

    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof AuthException) {

            Toast.makeText(getActivity().getApplicationContext(),"需要重新登录",Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mweibo", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();
            LoginService.handleLogin(getActivity());

        }else {
            Toast.makeText(getActivity().getApplicationContext(),"微博加载失败，请检查网络",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), NewStatusActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    mWeiBoService.getStatusService().getFriendsTimeline(1).observeOn(AndroidSchedulers.mainThread()).subscribe(statusesWrapper -> {
                        statuses = statusesWrapper.getStatuses();
                        mainAdapter.setStatuses(statuses);
                        mainAdapter.notifyDataSetChanged();
                    }, throwable -> handleError(throwable));
                }
            }, 500);
        }
        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    mWeiBoService.getStatusService().getFriendsTimeline(page++).observeOn(AndroidSchedulers.mainThread()).subscribe(statusesWrapper -> {
                        statuses.addAll(statusesWrapper.getStatuses());
                        mainAdapter.setStatuses(statuses);
                        mainAdapter.notifyDataSetChanged();
                    }, throwable -> handleError(throwable));
                }
            }, 500);
        }
    }
}
