package com.example.zhangzhao.mweibo.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.databinding.ItemStatusBinding;
import com.example.zhangzhao.mweibo.model.Status;

import java.util.List;

/**
 * Created by zhangzhao on 2015/8/11.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyHolder> {


    private List<Status> statuses;
    private Context context;
    private MyItemClickListener imageListener;
    private MyItemClickListener itemListener;

    public interface MyItemClickListener {
        public void onItemClick(int postion);
    }

    public MainAdapter(@Nullable List<Status> statuses, Context context) {
        this.statuses = statuses;
        this.context = context;
    }
    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    @Override
    public MainAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemStatusBinding binding=  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_status, parent, false);

        MyHolder myHolder = new MyHolder(binding.getRoot());
        myHolder.setBinding(binding);
        return myHolder;
    }

    public void setItemListener(MyItemClickListener itemListener) {
        this.itemListener = itemListener;
    }



    public void setImageListener(MyItemClickListener imageListener) {
        this.imageListener = imageListener;
    }


    @Override
    public void onBindViewHolder(MainAdapter.MyHolder holder, final int position) {
        Status status = statuses.get(position);
        Glide.with(context).load(status.getUser().getAvatar_large()).into(holder.binding.avatar);
        int size=status.getPic_urls().size();
        if (size ==1) {
            Glide.with(context).load(status.getPic_urls().get(0).getMiddle_pic()).into(holder.binding.image);
        }
        if (size>1){
            for (int i=0;i<size;i++){
                ImageView imageView = (ImageView)holder.binding.weiboimageMuti.getChildAt(i);
                Glide.with(context).load(status.getPic_urls().get(i).getMiddle_pic()).into(imageView);
            }
        }

        holder.binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageListener.onItemClick(position);
            }
        });
        holder.binding.weiboimageMuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageListener.onItemClick(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onItemClick(position);
                }
            }
        });

        holder.binding.setVariable(com.example.zhangzhao.mweibo.BR.status, status);
        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        private ItemStatusBinding binding;





        public ItemStatusBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemStatusBinding binding) {
            this.binding = binding;
        }

        public MyHolder(View itemView) {
            super(itemView);



        }


    }
}
