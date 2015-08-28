package com.example.zhangzhao.mweibo.service;

import android.accounts.AccountManagerFuture;
import android.os.Bundle;

import com.example.zhangzhao.mweibo.model.Comment;

import retrofit.RestAdapter;

/**
 * Created by zhangzhao on 2015/8/11.
 */
public class MWeiBoService {
    private RestAdapter restAdapter;
    public MWeiBoService(RestAdapter restAdapter){
        this.restAdapter=restAdapter;
    }
    public StatusService getStatusService(){
        return restAdapter.create(StatusService.class);
    }
    public CommentService getCommentService(){
        return restAdapter.create(CommentService.class);
    }
    public UserService getUserService(){

        return restAdapter.create(UserService.class);
    }

}
