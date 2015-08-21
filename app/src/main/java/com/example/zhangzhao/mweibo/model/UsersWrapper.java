package com.example.zhangzhao.mweibo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangzhao on 2015/8/11.
 */
public class UsersWrapper implements Serializable {
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
