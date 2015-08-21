package com.example.zhangzhao.mweibo.ui.fragment;

import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhangzhao.mweibo.Constants;
import com.example.zhangzhao.mweibo.MWeiBoModule;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.authenticator.ApiKeyProvider;
import com.example.zhangzhao.mweibo.model.Status;
import com.example.zhangzhao.mweibo.model.StatusesWrapper;
import com.example.zhangzhao.mweibo.model.User;
import com.example.zhangzhao.mweibo.service.MWeiBoService;
import com.example.zhangzhao.mweibo.ui.activity.StatusActivity;
import com.example.zhangzhao.mweibo.ui.adapter.MainAdapter;

import java.io.IOException;

import javax.inject.Inject;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by zhangzhao on 2015/8/10.
 */
public class UserTimelineFragment extends Fragment implements MainAdapter.MyItemClickListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainAdapter mainAdapter;
    private StatusesWrapper statusWrapper;
    private User user;
    @Inject
    protected MWeiBoService mWeiBoService;
    @Inject
    protected ApiKeyProvider apiKeyProvider;
    @Inject
    protected AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectGraph.create(MWeiBoModule.class).inject(this);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("user")) {
            user = (User) bundle.get("user");
            Log.i("UserTiemlineFragment",user.getScreen_name()+user.getId());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mainAdapter = new MainAdapter(null, getActivity());
        mainAdapter.setItemListener(this);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    token = apiKeyProvider.getAuthKey(getActivity());
                } catch (AccountsException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return token;
            }

            @Override
            protected void onPostExecute(final String token) {
                super.onPostExecute(token);
                Log.i("MainFragment", "获取ta的timeline");
                Log.i("Mainfragment", "token:  " + token);
                mWeiBoService.getStatusService().getUserTimeline(token,user.getId(), new Callback<StatusesWrapper>() {
                    @Override
                    public void success(StatusesWrapper statusesWrapper, Response response) {
                        statusWrapper = statusesWrapper;
                        mainAdapter.setStatuses(statusWrapper.getStatuses());
                        recyclerView.setAdapter(mainAdapter);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        if (error.getMessage().equals("403 Forbidden")||error.getMessage().equals("400 Bad Request")){
                            Log.i("MainFragment","token失效，清空本地token");
                            accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE, token);
                        }
                    }
                });
            }
        }.execute();

    }

    @Override
    public void onItemClick(int postion) {
        Status status = statusWrapper.getStatuses().get(postion);

        Intent intent=new Intent(getActivity(), StatusActivity.class);
        intent.putExtra("status", status);
        startActivity(intent);

    }
}
