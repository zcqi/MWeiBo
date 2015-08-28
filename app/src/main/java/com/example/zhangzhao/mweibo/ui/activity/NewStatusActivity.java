package com.example.zhangzhao.mweibo.ui.activity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.zhangzhao.mweibo.AuthException;
import com.example.zhangzhao.mweibo.MWeiBoModule;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.service.LoginService;
import com.example.zhangzhao.mweibo.service.MWeiBoService;

import javax.inject.Inject;

import dagger.ObjectGraph;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by zhangzhao on 2015/8/15.
 */
public class NewStatusActivity extends AppCompatActivity implements TextWatcher,View.OnClickListener{
    private EditText editText;
    private ImageButton imageButton;
    @Inject protected MWeiBoService mWeiBoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_status);
        ObjectGraph.create(MWeiBoModule.class).inject(this);
        editText = (EditText) findViewById(R.id.edittext);
        imageButton = (ImageButton) findViewById(R.id.btn_send);
        imageButton.setEnabled(false);
        imageButton.setOnClickListener(this);
        editText.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            Log.i("NewStatusActivity","输入变化了");
            imageButton.setEnabled(true);
        } else {
            imageButton.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        Log.i("NewStatusActivity", "点击发送了");
        mWeiBoService.getStatusService().postStatus( editText.getText().toString()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                status -> Toast.makeText(getApplicationContext(), "发表微博成功！", Toast.LENGTH_SHORT).show(),
                throwable -> handleError(throwable)
        );
    }
    private void handleError(Throwable throwable) {

        if (throwable instanceof AuthException) {
            Toast.makeText(this.getApplicationContext(), "需要重新登录", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences =this.getSharedPreferences("mweibo", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();
            LoginService.handleLogin(this);
        }else {
            Toast.makeText(this.getApplicationContext(),"发表微博失败，请检查网络",Toast.LENGTH_SHORT).show();
        }
    }
}
