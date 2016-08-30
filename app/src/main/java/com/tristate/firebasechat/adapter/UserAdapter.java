package com.tristate.firebasechat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.tristate.firebasechat.R;
import com.tristate.firebasechat.model.UserModel;

import java.util.ArrayList;

/**
 * Created by tristate-android1 on 15/6/16.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolders> {
    private ArrayList<UserModel> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolders extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;
        public TextView tvStatus;
        public ImageView imgUser;


        public ViewHolders(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvStatus = (TextView) v.findViewById(R.id.tvStatus);
            imgUser = (ImageView) v.findViewById(R.id.imgUser);
        }
    }

    public void add(int position, UserModel item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(ArrayList<UserModel> myDataset, Context context) {
        super();
        this.mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserAdapter.ViewHolders onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolders vh = new ViewHolders(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //final String name = mDataset.get(position);
        holder.tvName.setText("" + mDataset.get(position).getFirstName());
        holder.tvStatus.setText("" + mDataset.get(position).getStatus());
        if (mDataset.get(position).getProfileImageUri() != null) {
            Picasso.with(context).load("" + mDataset.get(position).getProfileImageUri()).into(holder.imgUser);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
