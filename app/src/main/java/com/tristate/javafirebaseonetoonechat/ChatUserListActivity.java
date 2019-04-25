package com.tristate.javafirebaseonetoonechat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tristate.javafirebaseonetoonechat.adapters.UserListAdapter;
import com.tristate.javafirebaseonetoonechat.databinding.ActivityChatUserListBinding;
import com.tristate.javafirebaseonetoonechat.listeners.OnItemClickListener;
import com.tristate.javafirebaseonetoonechat.models.User;

import java.util.ArrayList;

public class ChatUserListActivity extends AppCompatActivity implements OnItemClickListener {

    private FirebaseUser loggedUser;
    private ArrayList<User> userList = new ArrayList<>();
    private ActivityChatUserListBinding binding;
    private UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_user_list);

        setTitle(R.string.chat_user_title);

        loggedUser = FirebaseAuth.getInstance().getCurrentUser();

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "About us");
        menu.add(0, 1, 0, "Logout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            logOut();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new UserListAdapter(this, userList);
            adapter.setListener(this);
            binding.rvUserList.setLayoutManager(new LinearLayoutManager(this));
            binding.rvUserList.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void logOut() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference query = ref.child("users");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.progress.setVisibility(View.GONE);
                userList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!String.valueOf(data.child("uid").getValue()).equals(loggedUser.getUid())) {
                        User user = new User(String.valueOf(data.child("uid").getValue()),
                                String.valueOf(data.child("displayname").getValue()),
                                String.valueOf(data.child("email").getValue()));
                        userList.add(user);
                    }

                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.progress.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.llMain:
                ChatActivity.startActivity(this, userList.get(position), loggedUser.getUid());
                break;
        }
    }
}
