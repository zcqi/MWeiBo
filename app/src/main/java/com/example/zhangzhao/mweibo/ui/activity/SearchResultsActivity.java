package com.example.zhangzhao.mweibo.ui.activity;

import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.zhangzhao.mweibo.Constants;
import com.example.zhangzhao.mweibo.MWeiBoModule;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.authenticator.ApiKeyProvider;
import com.example.zhangzhao.mweibo.model.CommentsWrapper;
import com.example.zhangzhao.mweibo.model.User;
import com.example.zhangzhao.mweibo.service.MWeiBoService;
import com.example.zhangzhao.mweibo.ui.adapter.CommentsAdapter;
import com.example.zhangzhao.mweibo.ui.adapter.UsersAdapter;
import com.tumblr.bookends.Bookends;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by zhangzhao on 2015/8/16.
 */
public class SearchResultsActivity extends AppCompatActivity {
    private String query;
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    @Inject
    protected MWeiBoService mWeiBoService;
    @Inject
    protected ApiKeyProvider apiKeyProvider;
    @Inject
    protected AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recyclerview);
        ObjectGraph.create(MWeiBoModule.class).inject(this);
        usersAdapter = new UsersAdapter(null, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Log.i("SearchResultsActivity", "query: " + query);
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String token = null;
                    try {
                        token = apiKeyProvider.getAuthKey(SearchResultsActivity.this);
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
                    Log.i("SearchResultsActivity", "获取users");
                    Log.i("SearchResultsActivity", "token:  " + token);
                    mWeiBoService.getUserService().searchUsers(token, query, new Callback<List<User>>() {
                        @Override
                        public void success(List<User> users, Response response) {

                            usersAdapter.setUsers(users);
                            recyclerView.setAdapter(usersAdapter);
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
    }

}
