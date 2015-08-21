package com.example.zhangzhao.mweibo;

/**
 * Created by zhangzhao on 2015/8/10.
 */
public class Constants {

    public static final String APP_KEY ="2396797774" ;

    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String ACCOUNT_TYPE ="com.zhangzhao";
    public static final String AUTHTOKEN_TYPE =ACCOUNT_TYPE ;
    public static final String URL_BASE="https://api.weibo.com/2";
    public static final String URL_MAIN_FRAG="/statuses/friends_timeline.json";
    public static final String URL_AT_FRAG="/statuses/mentions.json";
    public static final String URL_COMMENT_FRAG="/comments/to_me.json";
    public static final String URL_FOLLOWERS_FRAG="/friendships/followers.json";
    public static final String URL_FRIENDS_FRAG="/friendships/friends.json";
    public static final String URL_STATUS_FRAG="/comments/show.json";
    public static final String URL_USER_FRAG="/statuses/user_timeline.json";
    public static final String URL_NEW_STATUS_ACTIVITY="/statuses/update.json";
    public static final String URL_SEARCH_ACTIVITY="/search/suggestions/users.json";
    public static final String URL_NEW_COMMENT_ACTIVITY="/comments/create.json";

}
