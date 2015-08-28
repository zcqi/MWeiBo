package com.example.zhangzhao.mweibo.ui.fragment;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhangzhao.mweibo.AuthException;
import com.example.zhangzhao.mweibo.MWeiBoModule;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.databinding.ItemStatusBinding;
import com.example.zhangzhao.mweibo.model.Status;
import com.example.zhangzhao.mweibo.service.LoginService;
import com.example.zhangzhao.mweibo.service.MWeiBoService;
import com.example.zhangzhao.mweibo.ui.adapter.CommentsAdapter;
import com.tumblr.bookends.Bookends;

import javax.inject.Inject;

import dagger.ObjectGraph;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by zhangzhao on 2015/8/10.
 */
public class StatusFragment extends Fragment {
    private RecyclerView recyclerView;
    private Status status;
    private View header;
    private ItemStatusBinding binding;
    @Inject
    protected MWeiBoService mWeiBoService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectGraph.create(MWeiBoModule.class).inject(this);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("status")) {
            status = (Status) bundle.get("status");

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        header = inflater.inflate(R.layout.item_status, container, false);

        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        ItemStatusBinding binding = ItemStatusBinding.bind(header);
        binding.setStatus(status);
        Glide.with(getActivity()).load(status.getUser().getAvatar_large()).into(binding.avatar);
        int size = status.getPic_urls().size();
        if (size == 1) {
            Glide.with(getActivity()).load(status.getPic_urls().get(0).getThumbnail_pic()).into(binding.image);
        }
        if (size > 1) {
            for (int i = 0; i < size; i++) {
                ImageView imageView = (ImageView) binding.weiboimageMuti.getChildAt(i);
                Glide.with(getActivity()).load(status.getPic_urls().get(i).getThumbnail_pic()).into(imageView);
            }
        }
        mWeiBoService.getCommentService().getCommentsShow(status.getId()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                commentsWrapper -> {
                    CommentsAdapter commentsAdapter = new CommentsAdapter(commentsWrapper.getComments(), getActivity());
                    Log.i("StatusFragment", "评论数： " + commentsWrapper.getComments().size() + "");
                    Bookends<CommentsAdapter> adapter = new Bookends<CommentsAdapter>(commentsAdapter);
                    adapter.addHeader(header);
                    recyclerView.setAdapter(adapter);
                },throwable -> handleError(throwable)
        );
    }
    private void handleError(Throwable throwable) {

        if (throwable instanceof AuthException) {
            Toast.makeText(getActivity().getApplicationContext(), "需要重新登录", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mweibo", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();
            LoginService.handleLogin(getActivity());
        }else {
            Toast.makeText(getActivity().getApplicationContext(),"评论加载失败，请检查网络",Toast.LENGTH_SHORT).show();
        }
    }
}
