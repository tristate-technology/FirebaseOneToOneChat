package com.tristate.firebasechat.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tristate.firebasechat.R;
import com.tristate.firebasechat.activity.ChatConverstion;
import com.tristate.firebasechat.activity.UserList;
import com.tristate.firebasechat.custome_view.BadgeView;
import com.tristate.firebasechat.model.UserModel;

/**
 * Created by tristate-android1 on 17/6/16.
 */
public class FirebaseUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    View mView;
    Context mContext;
    UserModel userModel;

    public FirebaseUserViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindUser(UserModel userModel) {
        this.userModel = userModel;
        ImageView imgUser = (ImageView) mView.findViewById(R.id.imgUser);
        TextView tvName = (TextView) mView.findViewById(R.id.tvName);
        TextView tvStatus = (TextView) mView.findViewById(R.id.tvStatus);
        BadgeView badgeChat = (BadgeView) mView.findViewById(R.id.badgeChat);
        badgeChat.setVisibility(View.GONE);
        Picasso.with(mContext)
                .load(userModel.getProfileImageUri())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(imgUser);
        tvName.setText(userModel.getFirstName());
        tvStatus.setText(userModel.getStatus());
        badgeChat.setText(""+userModel.getBadge());
    }

    @Override
    public void onClick(View view) {
        if (UserList.from == 1) {
            Intent intent = new Intent(mContext, ChatConverstion.class);
            intent.putExtra("reciverUserName", userModel.getFirstName());
            intent.putExtra("reciverUid", userModel.getUserId());
            intent.putExtra("reciverProfilePic", userModel.getProfileImageUri());
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        }

    }

}
