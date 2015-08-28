package com.example.zhangzhao.mweibo;


import android.util.Log;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by zhangzhao on 2015/8/28.
 */
public class RestErrorHandler implements ErrorHandler{

    @Override
    public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        Log.i("RestErrorHandler","haha");
        Log.i("RestErrorHandler",cause.getMessage());
        Log.i("RestErrorHandler",cause.getUrl());
        if (r != null && (r.getStatus() == 403||r.getStatus()==401)) {
            return new AuthException(cause);//待改进
        }
        return cause;
    }

}
