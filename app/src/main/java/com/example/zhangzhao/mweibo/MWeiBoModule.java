package com.example.zhangzhao.mweibo;

import android.content.Context;

import com.example.zhangzhao.mweibo.service.MWeiBoService;
import com.example.zhangzhao.mweibo.ui.activity.NewStatusActivity;
import com.example.zhangzhao.mweibo.ui.fragment.MainFragment;
import com.example.zhangzhao.mweibo.ui.fragment.StatusFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by zhangzhao on 2015/8/11.
 */
@Module(
        injects = {MainFragment.class, StatusFragment.class, NewStatusActivity.class,
                RestAdapterRequestInterceptor.class
        }
)
public class MWeiBoModule {
    @Provides
    MWeiBoService provideMWeiBoService(RestAdapter restAdapter) {
        return new MWeiBoService(restAdapter);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor) {
        return new RestAdapter.Builder().setEndpoint(Constants.URL_BASE).setErrorHandler(restErrorHandler).setRequestInterceptor(restRequestInterceptor).build();

    }
    @Provides
    @Singleton
    Context provideAppContext() {
        return MWeiBoApplication.getInstance().getApplicationContext();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(){
        return new RestErrorHandler();
    }
    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(Context appContext){
        return new RestAdapterRequestInterceptor(appContext);
    }
}
