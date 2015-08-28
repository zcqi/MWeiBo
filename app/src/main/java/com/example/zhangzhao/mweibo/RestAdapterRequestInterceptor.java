package com.example.zhangzhao.mweibo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.zhangzhao.mweibo.model.CommentsWrapper;
import com.example.zhangzhao.mweibo.model.StatusesWrapper;
import com.example.zhangzhao.mweibo.ui.fragment.MainFragment;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by zhangzhao on 2015/8/28.
 */

public class RestAdapterRequestInterceptor implements RequestInterceptor {
    Context context;
    public RestAdapterRequestInterceptor(Context context){
        this.context=context;
    }
    @Override
    public void intercept(RequestFacade request) {

        SharedPreferences sharedPreferences =context.getSharedPreferences("mweibo", Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token","hahaha");
        Log.i("intercept",token);
        request.addQueryParam("access_token", token);
    }
}