package com.example.zhangzhao.mweibo;

import android.accounts.AccountManager;
import android.content.Context;

import com.example.zhangzhao.mweibo.authenticator.ApiKeyProvider;
import com.example.zhangzhao.mweibo.service.MWeiBoService;
import com.example.zhangzhao.mweibo.ui.activity.NewCommentActivity;
import com.example.zhangzhao.mweibo.ui.activity.NewStatusActivity;
import com.example.zhangzhao.mweibo.ui.activity.SearchResultsActivity;
import com.example.zhangzhao.mweibo.ui.activity.UserActivity;
import com.example.zhangzhao.mweibo.ui.fragment.CommentFragment;
import com.example.zhangzhao.mweibo.ui.fragment.MainFragment;
import com.example.zhangzhao.mweibo.ui.fragment.StatusFragment;
import com.example.zhangzhao.mweibo.ui.fragment.UserPhotosFragment;
import com.example.zhangzhao.mweibo.ui.fragment.UserTimelineFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by zhangzhao on 2015/8/11.
 */
@Module(
        injects = {MainFragment.class, StatusFragment.class, NewStatusActivity.class, UserActivity.class, UserTimelineFragment.class, SearchResultsActivity.class, CommentFragment.class, NewCommentActivity.class}
)
public class MWeiBoModule {
    @Provides
    MWeiBoService provideMWeiBoService(RestAdapter restAdapter) {
        return new MWeiBoService(restAdapter);
    }

    @Provides
    RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder().setEndpoint(Constants.URL_BASE).build();

    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return MWeiBoApplication.getInstance().getApplicationContext();
    }

    @Provides
    AccountManager provideAccountManager(final Context context) {
        return AccountManager.get(context);
    }
}
