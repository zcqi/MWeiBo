package com.example.zhangzhao.mweibo.service;

import com.example.zhangzhao.mweibo.Constants;
import com.example.zhangzhao.mweibo.model.StatusesWrapper;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by zhangzhao on 2015/8/10.
 */
public interface StatusService {
    @GET(Constants.URL_MAIN_FRAG)
    void getFriendsTimeline(@Query("access_token") String token,@Query("page") int page,Callback<StatusesWrapper> callback);
    @GET(Constants.URL_AT_FRAG)
    void getMentions(@Query("access_token") String token,Callback<StatusesWrapper> callback);
    @GET(Constants.URL_USER_FRAG)
    void getUserTimeline(@Query("access_token") String token,@Query("uid") long uid,Callback<StatusesWrapper> callback);
    @FormUrlEncoded
    @POST(Constants.URL_NEW_STATUS_ACTIVITY)
    void postStatus(@Field("access_token") String token, @Field("status") String text,Callback<StatusesWrapper> callback);
}
