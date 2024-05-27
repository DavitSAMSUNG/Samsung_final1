package com.example.finalproject2_0.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.Models.User;
import com.example.finalproject2_0.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game_RequestAdapter extends RecyclerView.Adapter<Game_RequestAdapter.MyViewHolder> {
    private Context context;
    private FirebaseFirestore mStore;
    private GameModel gamemodel;
    private List<String> userNamesPending;
    private List<String> userNamesAccepted;
    private List<String> requestorIDsOnlyAccepted;
    private List<String> requestorIDsOnlyPending;
    private List<String> userNames;
    private List<String> requestorIDs;
    private Dialog myDialog;
    private ArrayList<User> users;
    String currentPendingUserName;

    public Game_RequestAdapter(Context context, GameModel gamemodel, ArrayList<User> users,
                               List<String> userNamesPending, List<String> userNamesAccepted,
                               List<String> requestorIDsOnlyPending, List<String> requestorIDsOnlyAccepted) {
        this.mStore = FirebaseFirestore.getInstance();
        this.context = context;
        this.gamemodel = gamemodel;
        this.userNamesPending = userNamesPending;
        this.userNamesAccepted = userNamesAccepted;
        this.requestorIDsOnlyPending = requestorIDsOnlyPending;
        this.requestorIDsOnlyAccepted = requestorIDsOnlyAccepted;
        this.users = users;
    }

    @NonNull
    @Override
    public Game_RequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row_request, parent, false);
        return new Game_RequestAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Game_RequestAdapter.MyViewHolder holder, int position) {
        requestorIDs = new ArrayList<>();
        requestorIDs.addAll(requestorIDsOnlyAccepted);
        requestorIDs.addAll(requestorIDsOnlyPending);


        userNames = new ArrayList<>();
        userNames.addAll(userNamesAccepted);
        userNames.addAll(userNamesPending);

        String currentUserName = userNames.get(position);

        if(position < userNamesPending.size()){
            currentPendingUserName = userNamesPending.get(position);
        }


        // Use position directly

        Log.d("Game_RequestAdapter", "Binding user: " + currentUserName + " at position: " + position);
        Log.d("Game_RequestAdapter", "Accepted users: " + userNamesAccepted);
        Log.d("Game_RequestAdapter", "Pending users: " + userNamesPending);

        if (userNamesAccepted.contains(currentUserName)) {
            Log.d("Game_RequestAdapter", currentUserName + " is in accepted list.");
            holder.acceptbtn.setVisibility(View.GONE);
            holder.rejectbtn.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.username.getLayoutParams();
            layoutParams.horizontalBias = 0.5f;
            holder.username.setLayoutParams(layoutParams);

            String updatedUsername = String.valueOf(position + 1) + "." + currentUserName;
            holder.username.setText(updatedUsername);
        } else {
            Log.d("Game_RequestAdapter", currentUserName + " is in pending list.");
            holder.acceptbtn.setVisibility(View.VISIBLE);
            holder.rejectbtn.setVisibility(View.VISIBLE);
            holder.username.setText(currentUserName);

            holder.acceptbtn.setOnClickListener(v -> {
                if (position != RecyclerView.NO_POSITION) {
                    holder.acceptbtn.setVisibility(View.GONE);
                    holder.rejectbtn.setVisibility(View.GONE);

                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.username.getLayoutParams();
                    layoutParams.horizontalBias = 0.5f;
                    holder.username.setLayoutParams(layoutParams);

                    String updatedUsername = String.valueOf(position + 1) + "." + currentUserName;
                    holder.username.setText(updatedUsername);

                    updateRequestStatus(position, "accepted", holder);
                }
            });

            holder.rejectbtn.setOnClickListener(v -> {
                int removedIndex = userNamesPending.indexOf(currentPendingUserName);
                if (removedIndex != -1 && removedIndex < userNamesPending.size()) {
                    userNamesPending.remove(removedIndex);
                    notifyItemRemoved(removedIndex);

                    updateRequestStatus(position, "rejected", holder);
                }
            });

        }

        holder.username.setOnClickListener(v -> showUserProfileDialog(position));
    }


    @Override
    public int getItemCount() {
        return userNamesPending.size() + userNamesAccepted.size();
    }

    private void updateRequestStatus(int position, String status, MyViewHolder holder) {
        mStore.collection("Games").document(gamemodel.documentID)
                .get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> requestors = (List<Map<String, Object>>) documentSnapshot.get("requestorIds");
                        if (requestors != null) {
                            String requestorId = requestorIDs.get(position);
                            for (Map<String, Object> requestor : requestors) {
                                String existingRequestorId = (String) requestor.get("requestorId");
                                if (existingRequestorId != null && existingRequestorId.equals(requestorId)) {
                                    requestor.put("status", status);
                                    break;
                                }
                            }
                            mStore.collection("Games").document(gamemodel.documentID).update("requestorIds", requestors);
                        }
                    }
                });
    }

    private void showUserProfileDialog(int position) {
        myDialog = new Dialog(context);
        TextView name, age, hobbies, avdays;

        myDialog.setContentView(R.layout.popup_profile);

        name = myDialog.findViewById(R.id.name);
        age = myDialog.findViewById(R.id.age);
        hobbies = myDialog.findViewById(R.id.hobbies);
        avdays = myDialog.findViewById(R.id.avdays);

        User user = users.get(position);
        name.setText(user.getName());
        age.setText(user.getAge());
        hobbies.setText(user.getHobbies());
        avdays.setText(user.getAvailable());

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        Button acceptbtn, rejectbtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            acceptbtn = itemView.findViewById(R.id.accept);
            rejectbtn = itemView.findViewById(R.id.reject);
        }
    }
}




