package com.example.zhangzhao.mweibo.model;

import org.antlr.v4.codegen.model.SrcOp;

import java.io.Serializable;

/**
 * Created by zhangzhao on 2015/8/10.
 */
public class Comment implements Serializable {
    private long id;



    private String created_at;
    private String idstr;
    private String text;
    private String source;
    private User user;
    private Status status;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
