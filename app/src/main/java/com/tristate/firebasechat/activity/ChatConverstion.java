package com.tristate.firebasechat.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tristate.firebasechat.R;

import com.tristate.firebasechat.viewholders.FirebaseMessageViewHolder;
import com.tristate.firebasechat.global.Constant;
import com.tristate.firebasechat.global.Utils;
import com.tristate.firebasechat.model.ChatUserModel;
import com.tristate.firebasechat.model.MessageModel;
import com.tristate.firebasechat.progressbares.CustomLoaderDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChatConverstion extends AppCompatActivity implements View.OnClickListener {


    private String reciverUserName, reciverUid, reciverProfilePic;

    private EditText edtMessage;
    private ImageView btnSend;
    private String senderId;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private String chat_id;
    private FirebaseRecyclerAdapter<MessageModel, FirebaseMessageViewHolder> mFirebaseAdapter;
    private RecyclerView reclerChat;
    private CustomLoaderDialog customeLoaderDialog;
    private int numberOfRecord = 300;

    private boolean isAlready = false;


    private RecyclerView.AdapterDataObserver adapterDataObserver;
    private boolean chatIdExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_converstion);
        initObjects();
        initListener();
    }

    private void initObjects() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        senderId = user.getUid();
        getDataFromBundle();
        database = FirebaseDatabase.getInstance();
        btnSend = (ImageView) findViewById(R.id.btnSend);

        edtMessage = (EditText) findViewById(R.id.edtMessage);
        reclerChat = (RecyclerView) findViewById(R.id.reclerChat);
        Constant.USER_ID = mAuth.getCurrentUser().getUid();
        if (chat_id == null) {
            checkStatus();
        } else {
            chatIdExist = true;
            setUpFirebaseAdapter();
        }

    }


    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            chat_id = bundle.getString("chat_id");
            reciverUserName = "" + bundle.getString("reciverUserName");
            reciverUid = "" + bundle.getString("reciverUid");
            reciverProfilePic = "" + bundle.getString("reciverProfilePic");
            getSupportActionBar().setTitle("" + reciverUserName);
        }
    }

    private void checkStatus() {
        if (customeLoaderDialog == null) {
            customeLoaderDialog = new CustomLoaderDialog(this);
            customeLoaderDialog.show(false);
        }
        DatabaseReference senderRefrence = database.getReference().child(Constant.CHAT_CONVERSATIONE_BADGE).child(senderId).child(reciverUid);
        senderRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customeLoaderDialog.hide();
                if (dataSnapshot.exists()) {
                    dataSnapshot.getValue();
                    ChatUserModel chatUserModel = dataSnapshot.getValue(ChatUserModel.class);
                    chat_id = chatUserModel.getChat_id();
                    Log.d("data hear -> ", "" + dataSnapshot);
                    setUpFirebaseAdapter();
                } else
                    isAlready = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initListener() {
        btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnSend && edtMessage.getText().toString().trim().length() > 0) {
            sendMsg(edtMessage.getText().toString(), 0);
        }
    }

    private void sendMsg(String message, int type) {
        if (chat_id != null) {
            DatabaseReference chatMessageRefrence = database.getReference().child(Constant.CHAT_MESSAGE_BADGE).child(chat_id).push();
            MessageModel messageModel = new MessageModel();
            messageModel.setSender(senderId);
            messageModel.setText(message);
            messageModel.setMsgType(type);
            messageModel.setTimestamp(Utils.getCurrentTimeStamp());
            chatMessageRefrence.setValue(messageModel);
            //Sender Refrence
            DatabaseReference senderRefrence = database.getReference().child(Constant.CHAT_CONVERSATIONE_BADGE).child(senderId).child(reciverUid);
            DatabaseReference lastActivity = senderRefrence.child("latestactivity");
            DatabaseReference timestamp = senderRefrence.child("timestamp");

            if (type == 2) {
                lastActivity.setValue("Image");
            } else {
                lastActivity.setValue(message);
            }

            timestamp.setValue(Utils.getCurrentTimeStamp());

            //Reciver Refrence
            DatabaseReference reciverRefrence = database.getReference().child(Constant.CHAT_CONVERSATIONE_BADGE).child(reciverUid).child(senderId);
            DatabaseReference lastActivityreciver = reciverRefrence.child("latestactivity");
            DatabaseReference timestampreciver = reciverRefrence.child("timestamp");
            if (type == 2) {
                lastActivityreciver.setValue("Image");
            } else {
                lastActivityreciver.setValue(message);
            }
            timestampreciver.setValue(Utils.getCurrentTimeStamp());
            DatabaseReference badge = reciverRefrence.child("badge");
            incrementCounter(badge);


        } else {
            DatabaseReference chatIdRefrence = database.getReference().child(Constant.CHAT_MESSAGE_BADGE).push();
            String chatId = chatIdRefrence.getKey();
            chat_id = chatId;

            DatabaseReference chatMessageRefrence = chatIdRefrence.push();
            MessageModel messageModel = new MessageModel();
            messageModel.setSender(senderId);
            messageModel.setMsgType(type);
            messageModel.setText(message);
            messageModel.setTimestamp(Utils.getCurrentTimeStamp());
            chatMessageRefrence.setValue(messageModel);

            //Sender Refrence
            ChatUserModel chatUserModel = new ChatUserModel();
            chatUserModel.setDisplayName("" + reciverUserName);
            chatUserModel.setBadge(0);
            chatUserModel.setChat_id(chatId);
            chatUserModel.setIsDelete("NO");
            if (type == 2) {
                chatUserModel.setLatestactivity("Image");
            } else {
                chatUserModel.setLatestactivity(message);
            }
            chatUserModel.setProfilePic(reciverProfilePic);
            chatUserModel.setTimestamp(Utils.getCurrentTimeStamp());
            chatUserModel.setUser_id(reciverUid);
            chatUserModel.setGroup(false);
            DatabaseReference senderRefrence = database.getReference().child(Constant.CHAT_CONVERSATIONE_BADGE).child(senderId).child(reciverUid);
            senderRefrence.setValue(chatUserModel);
            DatabaseReference reciverRefrence = database.getReference().child(Constant.CHAT_CONVERSATIONE_BADGE).child(reciverUid).child(senderId);
            chatUserModel.setUser_id(senderId);
            chatUserModel.setBadge(getBadgerCount(reciverRefrence));
            chatUserModel.setDisplayName("" + user.getDisplayName());
            chatUserModel.setProfilePic("" + user.getPhotoUrl());
            reciverRefrence.setValue(chatUserModel);
            if (!chatIdExist) {
                setUpFirebaseAdapter();
            }
            chatIdExist = true;
            /*setUpFirebaseAdapter();*/
        }
        edtMessage.setText("");
    }

    private long getBadgerCount(DatabaseReference reciverRefrence) {
        return 0;
    }

    private void setUpFirebaseAdapter() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        Query recentPostsQuery = database.getReference().child(Constant.CHAT_MESSAGE_BADGE).child(chat_id)
                .limitToLast(numberOfRecord);
        if (chat_id != null) {
            if (customeLoaderDialog == null)
                customeLoaderDialog = new CustomLoaderDialog(this);
            customeLoaderDialog.show(false);
            mFirebaseAdapter = new FirebaseRecyclerAdapter<MessageModel, FirebaseMessageViewHolder>
                    (MessageModel.class, R.layout.row_message_list, FirebaseMessageViewHolder.class, recentPostsQuery) {

                @Override
                protected void populateViewHolder(FirebaseMessageViewHolder viewHolder, MessageModel model, int position) {
                    customeLoaderDialog.hide();
                    viewHolder.bindUser(model);
                }
            };
            reclerChat.setHasFixedSize(true);
            linearLayoutManager.scrollToPositionWithOffset(2, 20);
            reclerChat.setLayoutManager(linearLayoutManager);
            reclerChat.setAdapter(mFirebaseAdapter);
            setUpRecylcerAdapterObserver();
            /* mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    DatabaseReference senderRefrence = database.getReference().child(Constant.CHAT_CONVERSATIONE_BADGE).child(senderId).child(reciverUid);
                    DatabaseReference senderBadge = senderRefrence.child("badge");
                    senderBadge.setValue(0);
                    reclerChat.scrollToPosition(reclerChat.getChildCount() - 1);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    DatabaseReference senderRefrence = database.getReference().child(Constant.CHAT_CONVERSATIONE_BADGE).child(senderId).child(reciverUid);
                    DatabaseReference senderBadge = senderRefrence.child("badge");
                    senderBadge.setValue(0);
                    reclerChat.scrollToPosition(positionStart);
                }
            });*/


        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapterDataObserver != null) {
            try {
                mFirebaseAdapter.unregisterAdapterDataObserver(adapterDataObserver);
            } catch (IllegalStateException i) {
                i.printStackTrace();
            }
        }
    }

   /* @Override
    protected void onStop() {
        super.onStop();
        if (adapterDataObserver != null && mFirebaseAdapter != null) {
            mFirebaseAdapter.unregisterAdapterDataObserver(adapterDataObserver);
        }
    }*/

    private void setUpRecylcerAdapterObserver() {

        adapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                DatabaseReference senderRefrence = database.getReference().child(Constant.CHAT_CONVERSATIONE_BADGE).child(senderId).child(reciverUid);
                DatabaseReference senderBadge = senderRefrence.child("badge");
                senderBadge.setValue(0);
                reclerChat.scrollToPosition(reclerChat.getChildCount() - 1);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                DatabaseReference senderRefrence = database.getReference().child(Constant.CHAT_CONVERSATIONE_BADGE).child(senderId).child(reciverUid);
                DatabaseReference senderBadge = senderRefrence.child("badge");
                senderBadge.setValue(0);
                reclerChat.scrollToPosition(positionStart);
            }
        };
        if (mFirebaseAdapter != null && adapterDataObserver != null) {
            mFirebaseAdapter.registerAdapterDataObserver(adapterDataObserver);
        }

    }
    public void incrementCounter(DatabaseReference databaseReference) {
        databaseReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError firebaseError, boolean committed, DataSnapshot currentData) {
                /*if (firebaseError != null) {
                    Log.d("Firebase counter increment failed.", "yes");
                } else {
                    Log.d("Firebase counter increment succeeded.", "yes");
                }*/
            }
        });
    }

}
