package com.example.zhangzhao.mweibo.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.zhangzhao.mweibo.BR;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.databinding.ItemCommentBinding;
import com.example.zhangzhao.mweibo.databinding.ItemStatusBinding;
import com.example.zhangzhao.mweibo.model.Comment;
import com.example.zhangzhao.mweibo.service.CommentService;

import java.util.List;

/**
 * Created by zhangzhao on 2015/8/13.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyHolder> {
    private List<Comment> comments;
    private Context context;
    public CommentsAdapter(@Nullable List<Comment> comments,Context context){
        this.comments=comments;
        this.context=context;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentsAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCommentBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_comment, parent, false);
        MyHolder myHolder=new MyHolder(binding.getRoot());
        myHolder.setBinding(binding);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(CommentsAdapter.MyHolder holder, int position) {
         Comment comment=comments.get(position);
        Log.i("CommentsAdapter",comment.getText());
        Glide.with(context).load(comment.getUser().getAvatar_large()).into(holder.binding.avatar);
        holder.binding.setVariable(BR.comment, comment);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static  class MyHolder extends RecyclerView.ViewHolder {
        private ItemCommentBinding binding;

        public ItemCommentBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemCommentBinding binding) {
            this.binding = binding;
        }

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
