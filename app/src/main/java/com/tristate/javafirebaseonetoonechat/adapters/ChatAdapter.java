package com.tristate.javafirebaseonetoonechat.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tristate.javafirebaseonetoonechat.R;
import com.tristate.javafirebaseonetoonechat.databinding.RowMyMessageBinding;
import com.tristate.javafirebaseonetoonechat.databinding.RowOthersMessageBinding;
import com.tristate.javafirebaseonetoonechat.databinding.RowUserListBinding;
import com.tristate.javafirebaseonetoonechat.listeners.OnItemClickListener;
import com.tristate.javafirebaseonetoonechat.models.Message;
import com.tristate.javafirebaseonetoonechat.models.User;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Message> list;
    private OnItemClickListener listener;
    private String loggedUId;

    public ChatAdapter(Context context, ArrayList<Message> list, String loggedUId) {
        this.context = context;
        this.list = list;
        this.loggedUId = loggedUId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_others_message, viewGroup, false), i);
        } else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_my_message, viewGroup, false), i);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Message message = list.get(i);
        if (getItemViewType(i) == 1) {
            viewHolder.bindingOther.tvMessage.setText(message.getMessage());
            viewHolder.bindingOther.tvTimeDate.setText(message.getTimestamp() + "");
        } else {
            viewHolder.bindingMy.tvMessage.setText(message.getMessage());
            viewHolder.bindingMy.tvTimeDate.setText(message.getTimestamp() + "");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (loggedUId.equals(list.get(position).getSenderId())) {
            return 0;
        } else {
            return 1;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RowOthersMessageBinding bindingOther;
        RowMyMessageBinding bindingMy;

        public ViewHolder(@NonNull View itemView, int type) {
            super(itemView);
            if (type == 1) {
                bindingOther = DataBindingUtil.bind(itemView);
            } else {
                bindingMy = DataBindingUtil.bind(itemView);
            }

        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
