package com.example.zhangzhao.mweibo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangzhao on 2015/8/11.
 */
public class CommentsWrapper implements Serializable {
    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
