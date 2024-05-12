package com.example.finalproject2_0.Adapters;

import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.Models.User;
import com.example.finalproject2_0.R;
import com.example.finalproject2_0.RequestorClickListener;
import com.example.finalproject2_0.UserNameCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game_RequestAdapter extends RecyclerView.Adapter<Game_RequestAdapter.MyViewHolder> {
    Context context;
    FirebaseFirestore mStore;
    GameModel gamemodel;
    List<String> userNames;
    List<String> requestorIDsOnly;
    Dialog myDialog;
    ArrayList<User> users = new ArrayList<>();
    private List<String> documentIDs;


    public Game_RequestAdapter(Context context, GameModel gamemodel,ArrayList<User> users, List<String> userNames,List<String> requestorIDsOnly) {
        this.mStore = FirebaseFirestore.getInstance();
        this.context = context;
        this.gamemodel = gamemodel;
        this.userNames = userNames;
        this.requestorIDsOnly = requestorIDsOnly;
        this.users = users;
    }

    @NonNull
    @Override
    public Game_RequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row_request, parent, false);
        return new Game_RequestAdapter.MyViewHolder(view);

    }

//    private void fetchUserNames(List<String> requestorIDs, UserNameCallback callback) {
//        for (String requestorid : requestorIDs) {
//            mStore.collection("users").document(requestorid).get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            String userName = documentSnapshot.getString("name");
//                            callback.onUserNameFetched(userName);
//                            System.out.println(userName);
//                        }
//                    });
//        }
//    }

    @Override
    public void onBindViewHolder(@NonNull Game_RequestAdapter.MyViewHolder holder, int position) {


//        String documentId = gamemodel.getDocumentID();
//
//
//        List<String> requestorIDs = new ArrayList<>();
//
//        mStore.collection("Games").document(documentId).get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        List<String> tempRequestorIDs = (List<String>) documentSnapshot.get("requestorIds");
//                        if (tempRequestorIDs != null) {
//                            fetchUserNames(tempRequestorIDs, userName -> {
//                                userNames.add(userName);
//                                //holder.username.setText(userNames.get(position));
//                            });
//                        }
//                    }
//                });


        holder.acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNames.remove(position);
                notifyItemRemoved(position);

                mStore.collection("Games").document(gamemodel.documentID)
                        .get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()){
                                List<Map<String, Object>> requestors = (List<Map<String, Object>>) documentSnapshot.get("requestorIds");
                                if (requestors != null) {
                                    for (Map<String, Object> requestor : requestors) {
                                        String existingRequestorId = (String) requestor.get("requestorId");
                                        if (existingRequestorId != null && existingRequestorId.equals(requestorIDsOnly.get(position))) {
                                            requestor.put("status", "accepted");
                                        }
                                    }
                                    mStore.collection("Games").document(gamemodel.documentID).update("requestorIds", requestors);
                                }
                            }
                        });
            }
        });

        holder.rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNames.remove(position);
            //  requestorIDsOnly.remove(position);
                notifyItemRemoved(position);

                mStore.collection("Games").document(gamemodel.documentID)
                        .get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()){
                                List<Map<String, Object>> requestors = (List<Map<String, Object>>) documentSnapshot.get("requestorIds");
                                if (requestors != null) {
                                    for (Map<String, Object> requestor : requestors) {
                                        String existingRequestorId = (String) requestor.get("requestorId");
                                        if (existingRequestorId != null && existingRequestorId.equals(requestorIDsOnly.get(position))) {
                                            requestor.put("status", "rejected");
                                        }
                                    }
                                    mStore.collection("Games").document(gamemodel.documentID).update("requestorIds", requestors);
                                }
                            }
                        });
            }
        });

        holder.username.setText(userNames.get(position));


        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new Dialog(context);
                TextView name,age,hobbies,avdays;

                myDialog.setContentView(R.layout.popup_profile);


                name = myDialog.findViewById(R.id.name);
                age = myDialog.findViewById(R.id.age);
                hobbies = myDialog.findViewById(R.id.hobbies);
                avdays = myDialog.findViewById(R.id.avdays);

                users.get(holder.getAdapterPosition()).getName();

//                                name.setText(documentSnapshot.getString("name"));
//                                age.setText(documentSnapshot.getString("age"));
//                                hobbies.setText(documentSnapshot.getString("hobbies"));
//                                avdays.setText(documentSnapshot.getString("available"));

                name.setText(users.get(holder.getAdapterPosition()).getName());
                age.setText(users.get(holder.getAdapterPosition()).getAge());
                hobbies.setText(users.get(holder.getAdapterPosition()).getHobbies());
                avdays.setText(users.get(holder.getAdapterPosition()).getAvailable());

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();

                //user = documentSnapshot.toObject(User.class);

            }
        });
    }


    @Override
    public int getItemCount() {
        return userNames.size();
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
