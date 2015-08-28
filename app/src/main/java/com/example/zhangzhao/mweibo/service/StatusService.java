package com.example.zhangzhao.mweibo.service;

import com.example.zhangzhao.mweibo.Constants;
import com.example.zhangzhao.mweibo.model.Status;
import com.example.zhangzhao.mweibo.model.StatusesWrapper;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by zhangzhao on 2015/8/10.
 */
public interface StatusService {
    @GET(Constants.URL_MAIN_FRAG)
    Observable<StatusesWrapper> getFriendsTimeline(@Query("page") int page);
    @FormUrlEncoded
    @POST(Constants.URL_NEW_STATUS_ACTIVITY)
    Observable<Status> postStatus(@Field("status") String text);
}
