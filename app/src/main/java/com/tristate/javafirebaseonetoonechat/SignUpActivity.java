package com.tristate.javafirebaseonetoonechat;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.tristate.javafirebaseonetoonechat.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    private ActivitySignUpBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        setTitle(R.string.signup_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnSignUp.setOnClickListener(this);

        binding.progress.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                if (TextUtils.isEmpty(binding.etName.getText().toString().trim())) {
                    Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etEmailId.getText().toString().trim())) {
                    Toast.makeText(this, "Please enter email id", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim())) {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else if (binding.etPassword.getText().toString().trim().length() < 8) {
                    Toast.makeText(this, "Minimum 8 character required for password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etConfirmPassword.getText().toString().trim())) {
                    Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
                } else if (!binding.etPassword.getText().toString().trim().equals(binding.etConfirmPassword.getText().toString().trim())) {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    signUp();
                }
                break;
        }
    }

    private void signUp() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.btnSignUp.setVisibility(View.GONE);
        firebaseAuth.createUserWithEmailAndPassword(binding.etEmailId.getText().toString(), binding.etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(binding.etName.getText().toString().trim())
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed. Please try again", Toast.LENGTH_SHORT).show();
                            binding.progress.setVisibility(View.GONE);
                            binding.btnSignUp.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }
}
