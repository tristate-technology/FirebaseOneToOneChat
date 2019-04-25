package com.tristate.javafirebaseonetoonechat.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tristate.javafirebaseonetoonechat.R;
import com.tristate.javafirebaseonetoonechat.databinding.RowUserListBinding;
import com.tristate.javafirebaseonetoonechat.listeners.OnItemClickListener;
import com.tristate.javafirebaseonetoonechat.models.User;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> list;
    private OnItemClickListener listener;

    public UserListAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_user_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        User user = list.get(i);
        viewHolder.binding.tvName.setText(user.getDisplayname());
        viewHolder.binding.tvEmailId.setText(user.getEmail());
        viewHolder.binding.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RowUserListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
