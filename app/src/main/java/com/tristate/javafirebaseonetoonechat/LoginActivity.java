package com.tristate.javafirebaseonetoonechat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tristate.javafirebaseonetoonechat.databinding.ActivityLoginBinding;
import com.tristate.javafirebaseonetoonechat.models.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        setTitle(R.string.login_title);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.btnLogin.setOnClickListener(this);
        binding.tvSignup.setOnClickListener(this);

        binding.progress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (TextUtils.isEmpty(binding.etEmailId.getText().toString().trim())) {
                    Toast.makeText(this, "Please enter email id", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim())) {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    checkLogin();
                }
                break;
            case R.id.tvSignup:
                SignUpActivity.startActivity(this);
                break;
        }
    }

    private void checkLogin() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.btnLogin.setVisibility(View.GONE);

        firebaseAuth.
                signInWithEmailAndPassword(binding.etEmailId.getText().toString(), binding.etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login successfully.", Toast.LENGTH_SHORT).show();
                            insertUpdateUser(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            binding.progress.setVisibility(View.GONE);
                            binding.btnLogin.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void insertUpdateUser(FirebaseUser currentUser) {
        DatabaseReference firebase = database.getReference().child("users").child(currentUser.getUid());
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                startChatUserListActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        User userData = new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());
        firebase.setValue(userData);
    }

    private void startChatUserListActivity() {
        Intent intent = new Intent(this, ChatUserListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
