package com.example.zhangzhao.mweibo.service;

import com.example.zhangzhao.mweibo.Constants;
import com.example.zhangzhao.mweibo.model.CommentsWrapper;
import com.example.zhangzhao.mweibo.model.User;
import com.example.zhangzhao.mweibo.model.UsersWrapper;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by zhangzhao on 2015/8/10.
 */
public interface UserService {
    @GET(Constants.URL_FOLLOWERS_FRAG)
    void getFollowers(@Query("token") String token, Callback<UsersWrapper> callback);
    @GET(Constants.URL_FRIENDS_FRAG)
    void getFriends(@Query("token") String token, Callback<UsersWrapper> callback);
    @GET(Constants.URL_SEARCH_ACTIVITY)
    void searchUsers(@Query("access_token") String token,@Query("q") String q,Callback<List<User>> callback);
}
