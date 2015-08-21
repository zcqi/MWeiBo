package com.example.zhangzhao.mweibo.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.databinding.ItemUserBinding;
import com.example.zhangzhao.mweibo.model.User;

import java.util.List;

/**
 * Created by zhangzhao on 2015/8/16.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyHolder>{
    private Context context;
    private List<User> users;
    public UsersAdapter(@Nullable List<User> users,Context context){
        this.users=users;
        this.context=context;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemUserBinding binding=  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_user, parent, false);
        MyHolder myHolder = new MyHolder(binding.getRoot());
        myHolder.setBinding(binding);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        User user=users.get(position);
        holder.binding.setVariable(com.example.zhangzhao.mweibo.BR.user, user);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ItemUserBinding binding;
        public MyHolder(View itemView) {
            super(itemView);
        }

        public ItemUserBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemUserBinding binding) {
            this.binding = binding;
        }
    }
}
