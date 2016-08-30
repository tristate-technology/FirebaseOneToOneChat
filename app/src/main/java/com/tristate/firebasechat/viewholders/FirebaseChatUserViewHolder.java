package com.tristate.firebasechat.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tristate.firebasechat.R;
import com.tristate.firebasechat.activity.ChatConverstion;
import com.tristate.firebasechat.custome_view.BadgeView;
import com.tristate.firebasechat.model.ChatUserModel;

/**
 * Created by tristate-android1 on 17/6/16.
 */
public class FirebaseChatUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    View mView;
    Context mContext;
    ChatUserModel userModel;

    public FirebaseChatUserViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindUser(ChatUserModel userModel) {
        this.userModel = userModel;
        ImageView imgUser = (ImageView) mView.findViewById(R.id.imgUser);
        TextView tvName = (TextView) mView.findViewById(R.id.tvName);
        TextView tvStatus = (TextView) mView.findViewById(R.id.tvStatus);
        BadgeView badgeChat = (BadgeView) mView.findViewById(R.id.badgeChat);
        if (userModel.isGroup()) {
           // imgUser.setImageDrawable(mContext.getResources().getDrawable(R.drawable.create_group));
        } else {
            Picasso.with(mContext)
                    .load(userModel.getProfilePic())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(imgUser);
        }

        tvName.setText(userModel.getDisplayName());
        tvStatus.setText(userModel.getLatestactivity());
        if (userModel.getBadge() > 0) {
            badgeChat.setVisibility(View.VISIBLE);
            badgeChat.setText("" + userModel.getBadge());
        } else {
            badgeChat.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        if (!userModel.isGroup()) {
            Intent intent = new Intent(mContext, ChatConverstion.class);
            intent.putExtra("chat_id", "" + userModel.getChat_id());
            intent.putExtra("reciverUserName", "" + userModel.getDisplayName());
            intent.putExtra("reciverProfilePic", "" + userModel.getProfilePic());
            intent.putExtra("reciverUid", "" + userModel.getUser_id());
            mContext.startActivity(intent);
        }

       /* final ArrayList<Restaurant> restaurants = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(SyncStateContract.Constants.FIREBASE_CHILD_RESTAURANTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    restaurants.add(snapshot.getValue(Restaurant.class));
                }
                int itemPosition = getLayoutPosition();
                Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                intent.putExtra("position", itemPosition + "");
                intent.putExtra("restaurants", Parcels.wrap(restaurants));
                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
