package com.example.zhangzhao.mweibo.ui.activity;

import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.zhangzhao.mweibo.Constants;
import com.example.zhangzhao.mweibo.MWeiBoModule;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.authenticator.ApiKeyProvider;
import com.example.zhangzhao.mweibo.model.Comment;
import com.example.zhangzhao.mweibo.model.Status;
import com.example.zhangzhao.mweibo.model.StatusesWrapper;
import com.example.zhangzhao.mweibo.service.MWeiBoService;

import java.io.IOException;

import javax.inject.Inject;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by zhangzhao on 2015/8/18.
 */
public class NewCommentActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private EditText editText;
    private ImageButton imageButton;
    private Status status;
    @Inject
    protected MWeiBoService mWeiBoService;
    @Inject protected ApiKeyProvider apiKeyProvider;
    @Inject protected AccountManager accountManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_status);
        ObjectGraph.create(MWeiBoModule.class).inject(this);
        findViewById(R.id.btnImage).setVisibility(View.GONE);
        editText = (EditText) findViewById(R.id.edittext);
        imageButton = (ImageButton) findViewById(R.id.btn_send);
        imageButton.setEnabled(false);
        imageButton.setOnClickListener(this);
        editText.addTextChangedListener(this);
        status= (Status) getIntent().getSerializableExtra("status");
    }

    @Override
    public void onClick(View v) {
        Log.i("NewCommentActivity", "点击发送了");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token=null;
                try {
                    token=apiKeyProvider.getAuthKey(NewCommentActivity.this);

                } catch (AccountsException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return token;
            }

            @Override
            protected void onPostExecute(final String token) {
                super.onPostExecute(token);
                Log.i("NewCommentActivity", "token:  " + token);
                long id=status.getId();
                mWeiBoService.getCommentService().postComment(token, editText.getText().toString(),id,new Callback<Comment>() {


                    @Override
                    public void success(Comment comment, Response response) {
                        Toast.makeText(getApplicationContext(), "发表评论成功！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        if (error.getMessage().equals("403 Forbidden")||error.getMessage().equals("400 Bad Request")){
                            Log.i("NewCommentActivity","token失效，清空本地token");
                            accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE, token);
                        }

                    }
                });
            }
        }.execute();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            Log.i("NewCommentActivity","输入变化了");
            imageButton.setEnabled(true);
        } else {
            imageButton.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
