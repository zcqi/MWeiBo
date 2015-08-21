package com.example.zhangzhao.mweibo.ui.fragment;

import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.example.zhangzhao.mweibo.model.Comment;
import com.example.zhangzhao.mweibo.model.CommentsWrapper;
import com.example.zhangzhao.mweibo.model.Status;
import com.example.zhangzhao.mweibo.model.StatusesWrapper;
import com.example.zhangzhao.mweibo.service.MWeiBoService;
import com.example.zhangzhao.mweibo.ui.adapter.CommentsAdapter;
import com.example.zhangzhao.mweibo.ui.adapter.MainAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by zhangzhao on 2015/8/10.
 */
public class CommentFragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private SwipyRefreshLayout swipeRefreshLayout;
    private CommentsAdapter commentsAdapter;
    private List<Comment> comments=new ArrayList<Comment>();
    private int page=2;
    @Inject
    protected MWeiBoService mWeiBoService;
    @Inject protected ApiKeyProvider apiKeyProvider;
    @Inject protected AccountManager accountManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectGraph.create(MWeiBoModule.class).inject(this);    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout= (SwipyRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        commentsAdapter=new CommentsAdapter(comments,getActivity());
        recyclerView.setAdapter(commentsAdapter);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token=null;
                try {
                    token=apiKeyProvider.getAuthKey(getActivity());
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
                Log.i("CommentFragment", "获取评论我的");
                Log.i("CommentFragment", "token:  " + token);
                mWeiBoService.getCommentService().getCommentsToMe(token, 1, new Callback<CommentsWrapper>() {
                    @Override
                    public void success(CommentsWrapper commentsWrapper, Response response) {
                        comments = commentsWrapper.getComments();
                        commentsAdapter.setComments(comments);
                        commentsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Log.i("CommentFragment", "retrofit错误： " + error.getMessage());
                        if (error.getMessage().equals("403 Forbidden") || error.getMessage().equals("400 Bad Request")) {
                            Log.i("CommentFragment", "token失效，清空本地token");
                            accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE, token);
                        }
                    }
                });
            }
        }.execute();

    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction==SwipyRefreshLayoutDirection.TOP) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
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
                            Log.i("CommentFragment", "获取timeline");
                            Log.i("CommentFragment", "token:  " + token);
                            mWeiBoService.getCommentService().getCommentsToMe(token, 1, new Callback<CommentsWrapper>() {
                               @Override
                               public void success(CommentsWrapper commentsWrapper, Response response) {
                                   comments = commentsWrapper.getComments();
                                   commentsAdapter.setComments(comments);
                                   commentsAdapter.notifyDataSetChanged();
                               }

                               @Override
                               public void failure(RetrofitError error) {
                                   error.printStackTrace();
                                   Log.i("CommentFragment", "retrofit错误： " + error.getMessage());
                                   if (error.getMessage().equals("403 Forbidden") || error.getMessage().equals("400 Bad Request")) {
                                       Log.i("CommentFragment", "token失效，清空本地token");
                                       accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE, token);
                                   }
                               }
                           });
                        }
                    }.execute();
                }
            }, 500);
        }
        if (direction==SwipyRefreshLayoutDirection.BOTTOM){
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
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
                            Log.i("CommentFragment", "获取timeline");
                            Log.i("CommentFragment", "token:  " + token);
                            mWeiBoService.getCommentService().getCommentsToMe(token, page++, new Callback<CommentsWrapper>() {
                                @Override
                                public void success(CommentsWrapper commentsWrapper, Response response) {
                                    comments.addAll(commentsWrapper.getComments());
                                    commentsAdapter.setComments(comments);
                                    commentsAdapter.notifyDataSetChanged();
                                }

                                 @Override
                                 public void failure(RetrofitError error) {
                                     error.printStackTrace();
                                     Log.i("CommentFragment", "retrofit错误： " + error.getMessage());
                                     if (error.getMessage().equals("403 Forbidden") || error.getMessage().equals("400 Bad Request")) {
                                         Log.i("CommentFragment", "token失效，清空本地token");
                                         accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE, token);
                                     }
                                 }
                             });
                        }
                    }.execute();
                }
            }, 500);
        }
    }

}
