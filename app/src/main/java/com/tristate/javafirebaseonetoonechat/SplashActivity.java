package com.tristate.javafirebaseonetoonechat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (currentUser != null) {
                    intent = new Intent(SplashActivity.this, ChatUserListActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 500);
    }
}
