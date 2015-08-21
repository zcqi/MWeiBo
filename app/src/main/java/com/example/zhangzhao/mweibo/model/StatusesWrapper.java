package com.example.zhangzhao.mweibo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangzhao on 2015/8/11.
 */
public class StatusesWrapper implements Serializable {
    private List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
