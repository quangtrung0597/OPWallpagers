package com.example.trung.onepiecewallpagers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.security.MessageDigest;

public class ShareFb extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private Button btnShare;
    private ImageView imView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_share_fb);

        toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imView = findViewById(R.id.imViewShare);
        Intent intent = getIntent();
        String imgSmall = intent.getStringExtra("imgS");
        final String imgUrl = intent.getStringExtra("img");

        Glide.with(getApplicationContext()).load(imgSmall)
                .into(imView);
        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.connectWithFbButton);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });

        btnShare = findViewById(R.id.btnShareFB);
        shareDialog = new ShareDialog(ShareFb.this);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(imgUrl))
                        .build();

                ShareDialog shareDialog = new ShareDialog(ShareFb.this);
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
