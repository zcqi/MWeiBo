package com.example.zhangzhao.mweibo.service;

import com.example.zhangzhao.mweibo.Constants;
import com.example.zhangzhao.mweibo.model.Comment;
import com.example.zhangzhao.mweibo.model.CommentsWrapper;
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
public interface CommentService {
    @GET(Constants.URL_COMMENT_FRAG)
    void getCommentsToMe(@Query("access_token") String token,@Query("page") int page, Callback<CommentsWrapper> callback);
    @GET(Constants.URL_STATUS_FRAG)
    void getCommentsShow(@Query("access_token") String token, @Query("id") long id,Callback<CommentsWrapper> callback);
    @FormUrlEncoded
    @POST(Constants.URL_NEW_COMMENT_ACTIVITY)
    void postComment(@Field("access_token") String token, @Field("comment") String text,@Field("id") long id,Callback<Comment> callback);
}
