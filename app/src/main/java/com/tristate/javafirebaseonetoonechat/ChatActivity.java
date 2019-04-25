package com.tristate.javafirebaseonetoonechat;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tristate.javafirebaseonetoonechat.adapters.ChatAdapter;
import com.tristate.javafirebaseonetoonechat.databinding.ActivityChatBinding;
import com.tristate.javafirebaseonetoonechat.models.Message;
import com.tristate.javafirebaseonetoonechat.models.User;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    public static void startActivity(Context context, User user, String loggedUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("loggedUserId", loggedUserId);
        context.startActivity(intent);
    }

    private User user;
    private String loggedUserId;
    private ArrayList<Message> messageList = new ArrayList<>();
    private FirebaseDatabase database;
    private ActivityChatBinding binding;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        user = getIntent().getParcelableExtra("user");
        loggedUserId = getIntent().getStringExtra("loggedUserId");

        setTitle(user.getDisplayname());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        database = FirebaseDatabase.getInstance();

        binding.btnSend.setOnClickListener(this);

        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                if (!TextUtils.isEmpty(binding.etMessage.getText().toString().trim())) {
                    sendMessage();
                } else {
                    Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {

        DatabaseReference query = database.getReference().child("message").child(getMessageId(loggedUserId, user.getUid()));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.progress.setVisibility(View.GONE);
                messageList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = new Message(String.valueOf(data.child("message").getValue()),
                            String.valueOf(data.child("senderId").getValue()),
                            String.valueOf(data.child("receiverId").getValue()),
                            Long.parseLong(String.valueOf(data.child("timestamp").getValue())));

                    messageList.add(message);
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.progress.setVisibility(View.GONE);
            }
        });
    }

    private String getMessageId(String uid1, String uid2) {
        if (uid1.hashCode() > uid2.hashCode()) {
            return uid1 + uid2;
        } else {
            return uid2 + uid1;
        }
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new ChatAdapter(this, messageList, loggedUserId);
            binding.rvMessage.setLayoutManager(new LinearLayoutManager(this));
            binding.rvMessage.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        binding.rvMessage.scrollToPosition(messageList.size() - 1);
    }

    private void sendMessage() {
        String key = database.getReference().child("message").push().getKey();
        DatabaseReference firebase =
                database.getReference().child("message").child(getMessageId(loggedUserId, user.getUid())).child(key);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Message message = new Message(binding.etMessage.getText().toString().trim(), loggedUserId, user.getUid(), System.currentTimeMillis());
        firebase.setValue(message);

        binding.etMessage.setText("");
    }
}
