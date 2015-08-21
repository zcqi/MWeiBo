package com.example.zhangzhao.mweibo.model;

import java.io.Serializable;

/**
 * Created by zhangzhao on 2015/8/11.
 */
public class Image implements Serializable {
    private String thumbnail_pic;

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }
    public String getMiddle_pic(){
        String left=thumbnail_pic.substring(0,22);
        String right=thumbnail_pic.substring(31,thumbnail_pic.length());
        return left+"bmiddle"+right;
    }
    public String getLarge_pic(){
        String left=thumbnail_pic.substring(0,22);
        String right=thumbnail_pic.substring(31,thumbnail_pic.length());
        return left+"large"+right;
    }
    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }
}
