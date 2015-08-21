package com.example.zhangzhao.mweibo.ui.fragment;

import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import com.example.zhangzhao.mweibo.model.Image;
import com.example.zhangzhao.mweibo.model.Status;
import com.example.zhangzhao.mweibo.model.StatusesWrapper;
import com.example.zhangzhao.mweibo.model.User;
import com.example.zhangzhao.mweibo.service.MWeiBoService;
import com.example.zhangzhao.mweibo.ui.activity.ImageActivity;
import com.example.zhangzhao.mweibo.ui.activity.NewCommentActivity;
import com.example.zhangzhao.mweibo.ui.activity.NewStatusActivity;
import com.example.zhangzhao.mweibo.ui.activity.StatusActivity;
import com.example.zhangzhao.mweibo.ui.activity.UserActivity;
import com.example.zhangzhao.mweibo.ui.adapter.MainAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.IOException;
import java.io.Serializable;
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
public class MainFragment extends Fragment implements View.OnClickListener, SwipyRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private SwipyRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private MainAdapter mainAdapter;
    private List<Status> statuses = new ArrayList<Status>();
    private int page = 2;
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
        mainAdapter.setIconListener(new MainAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                User user = statuses.get(postion).getUser();
                intent.putExtra("user", user);
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
        mainAdapter.setButtonListener(new MainAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                Status status = statuses.get(postion);
                Intent intent = new Intent(getActivity(), NewCommentActivity.class);
                intent.putExtra("status", status);
                startActivity(intent);
            }
        });
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
            protected void onPostExecute(String token) {
                super.onPostExecute(token);
                Log.i("MainFragment", "获取timeline");
                Log.i("Mainfragment", "token:  " + token);
                mWeiBoService.getStatusService().getFriendsTimeline(token, 1, new Callback<StatusesWrapper>() {
                    @Override
                    public void success(StatusesWrapper statusesWrapper, Response response) {
                        statuses = statusesWrapper.getStatuses();
                        mainAdapter.setStatuses(statuses);
                        mainAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Log.i("MainFragment", "retrofit错误： " + error.getMessage());
                        if (error.getMessage().equals("403 Forbidden") || error.getMessage().equals("400 Bad Request")) {
                            Log.i("MainFragment", "token失效了！！！");
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
                                protected void onPostExecute( String token) {
                                    super.onPostExecute(token);
                                    accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE,token);

                                }
                            }.execute();


                        }
                    }
                });
            }
        }.execute();


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
                        protected void onPostExecute( String token) {
                            super.onPostExecute(token);
                            Log.i("MainFragment", "获取timeline");
                            Log.i("Mainfragment", "token:  " + token);
                            mWeiBoService.getStatusService().getFriendsTimeline(token, 1, new Callback<StatusesWrapper>() {
                                @Override
                                public void success(StatusesWrapper statusesWrapper, Response response) {
                                    statuses = statusesWrapper.getStatuses();
                                    mainAdapter.setStatuses(statuses);
                                    mainAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    error.printStackTrace();
                                    Log.i("MainFragment", "retrofit错误： " + error.getMessage());
                                    if (error.getMessage().equals("403 Forbidden") || error.getMessage().equals("400 Bad Request")) {
                                        Log.i("MainFragment", "token失效，清空本地token");
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
                                            protected void onPostExecute( String token) {
                                                super.onPostExecute(token);
                                                accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE,token);

                                            }
                                        }.execute();
                                    }
                                }
                            });
                        }
                    }.execute();
                }
            }, 500);
        }
        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
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
                        protected void onPostExecute( String token) {
                            super.onPostExecute(token);
                            Log.i("MainFragment", "获取timeline");
                            Log.i("Mainfragment", "token:  " + token);
                           mWeiBoService.getStatusService().getFriendsTimeline(token, page++, new Callback<StatusesWrapper>() {
                                @Override
                                public void success(StatusesWrapper statusesWrapper, Response response) {
                                    statuses.addAll(statusesWrapper.getStatuses());
                                    mainAdapter.setStatuses(statuses);
                                    mainAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    error.printStackTrace();
                                    Log.i("MainFragment", "retrofit错误： " + error.getMessage());
                                    if (error.getMessage().equals("403 Forbidden") || error.getMessage().equals("400 Bad Request")) {
                                        Log.i("MainFragment", "token失效，清空本地token");
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
                                            protected void onPostExecute( String token) {
                                                super.onPostExecute(token);
                                                accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE,token);

                                            }
                                        }.execute();
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
