package com.example.finalproject2_0.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Models.Message;
import com.example.finalproject2_0.Models.User;
import com.example.finalproject2_0.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messages;
    private String ownerID;
    private Dialog myDialog;
    private Context context;

    public MessageAdapter(Context context, List<Message> messages, String ownerID) {
        this.context = context;
        this.messages = messages;
        this.ownerID = ownerID;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.message.setText(message.getMessage());

        if (message.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.linearLayout.setGravity(Gravity.START);
        } else {
            holder.linearLayout.setGravity(Gravity.END);
        }

        holder.username.setVisibility(View.VISIBLE);
        holder.username.setText(message.getUserName());
        if (position != 0 && Objects.equals(messages.get(position - 1).getUserName(), messages.get(position).getUserName())) {
            holder.username.setVisibility(View.GONE);
        }

        if (message.getUserId().equals(ownerID)) {
            holder.username.setText(message.getUserName() + " (owner)");
        }

        holder.username.setOnClickListener(v -> showUserProfileDialog(position));
    }

    private void showUserProfileDialog(int position) {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.popup_profile);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        User user = messages.get(position).getUser();
        if (user != null) {
            ((TextView) myDialog.findViewById(R.id.name)).setText(user.getName());
            ((TextView) myDialog.findViewById(R.id.age)).setText(user.getAge());
            ((TextView) myDialog.findViewById(R.id.hobbies)).setText(user.getHobbies());
            ((TextView) myDialog.findViewById(R.id.avdays)).setText(user.getAvailable());
        }

        myDialog.show();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView message, username;
        LinearLayout linearLayout;

        public MessageViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.textViewMessage);
            linearLayout = view.findViewById(R.id.linear);
            username = view.findViewById(R.id.username);
        }
    }
}
