package com.example.finalproject2_0.Adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Chat;
import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.Models.User;
import com.example.finalproject2_0.R;
import com.example.finalproject2_0.Interfaces.UserCallback;
import com.example.finalproject2_0.Interfaces.UserNameCallback;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Game_RecyclerViewAdapter_MyGames extends RecyclerView.Adapter<Game_RecyclerViewAdapter_MyGames.MyViewHolder> {
    Context context;
    public ArrayList<GameModel> gameModels;
    ArrayList<User> users;
    ArrayList<User> usersAccepted;
    ArrayList<User> usersPending;
    int size;
    List<String> userNamesPending;
    List<String> userNamesAccepted;

    Game_RequestAdapter gamerequestAdapter;
    FirebaseFirestore mStore;

    public Game_RecyclerViewAdapter_MyGames(Context context, ArrayList<GameModel> gameModels){
        this.mStore = FirebaseFirestore.getInstance();
        this.context = context;
        this.gameModels = gameModels;
        this.userNamesPending = new ArrayList<>();
        this.userNamesAccepted = new ArrayList<>();
        this.usersAccepted = new ArrayList<>();
        this.usersPending = new ArrayList<>();
        this.users = new ArrayList<>();
//        gamerequestAdapter = new Game_RequestAdapter(context, gameModels);
    }
    @NonNull
    @Override
    public Game_RecyclerViewAdapter_MyGames.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recyclerview_row_mygames, parent,false);
        return new Game_RecyclerViewAdapter_MyGames.MyViewHolder(view);

    }

    private void fetchUserNames(List<String> requestorIDsPending, List<String> requestorIDsAccepted, UserNameCallback callback, UserCallback userCallback, Runnable onComplete) {
        int totalRequests = requestorIDsPending.size() + requestorIDsAccepted.size();
        AtomicInteger completedRequests = new AtomicInteger(0);
        for (String requestorid : requestorIDsAccepted){
            mStore.collection("users").document(requestorid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userNameAccepted = documentSnapshot.getString("name");
                            User userAccepted = documentSnapshot.toObject(User.class);
                            if (userAccepted != null) {
                                userCallback.onUserFetched(userAccepted, true);
                                callback.onUserNameFetched(userNameAccepted, true);
                            }
                        }
                        checkIfAllRequestsCompleted(totalRequests, completedRequests,onComplete);
                    });
        }
        for (String requestorid : requestorIDsPending) {
            mStore.collection("users").document(requestorid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userNamePending = documentSnapshot.getString("name");
                            User userPending = documentSnapshot.toObject(User.class);
                            if (userPending != null) {
                                userCallback.onUserFetched(documentSnapshot.toObject(User.class),false);
                                callback.onUserNameFetched(userNamePending,false);
                            }
                        }
                        checkIfAllRequestsCompleted(totalRequests, completedRequests,onComplete);
                    });
        }


    }

    private void checkIfAllRequestsCompleted(int totalRequests, AtomicInteger completedRequests, Runnable onComplete) {
        if (completedRequests.incrementAndGet() == totalRequests) {
            // All requests are completed, execute the onComplete runnable
            onComplete.run();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Game_RecyclerViewAdapter_MyGames.MyViewHolder holder, int position) throws Resources.NotFoundException {
        userNamesPending.clear();
        userNamesAccepted.clear();
        usersAccepted.clear();
        usersPending.clear();

        GameModel gamemodel = gameModels.get(position);
        holder.gname.setText(gamemodel.getGamename());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        holder.datetext.setText(dateFormat.format(gamemodel.getDate()));
        holder.timetext.setText(gamemodel.getTime());
        holder.gdesc.setText(gamemodel.getGamedescription());
        holder.chatButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, Chat.class);
                 intent.putExtra("gameID", gamemodel.documentID);
                 intent.putExtra("gameName", gamemodel.getGamename());
                 intent.putExtra("ownerID",gamemodel.getOwneruserid());
                 context.startActivity(intent);
             }
        });

        holder.agerestext.setText(String.valueOf(gamemodel.getAgeRes()));

        UserNameCallback callback = (userName, isAccepted) -> {
            if (isAccepted) {
                userNamesAccepted.add(userName);
            } else {
                userNamesPending.add(userName);
            }
        };

        UserCallback userCallback = (user, isAccepted) -> {
            if (isAccepted) {
                usersAccepted.add(user);
            } else {
                usersPending.add(user);
            }
        };

        boolean isVisible = gamemodel.visibility;
        holder.expanded_layout_1.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        holder.expanded_layout_2.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        String documentId = gamemodel.getDocumentID();

        mStore.collection("Games").document(documentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.get("requestorIds") instanceof List) {
                            List<Map<String, Object>> tempRequestorData = (List<Map<String, Object>>) documentSnapshot.get("requestorIds");
                            List<String> requestorIDsOnlyPending = new ArrayList<>();
                            List<String> requestorIDsOnlyAccepted = new ArrayList<>();
                            if (!tempRequestorData.isEmpty()) {
                                for (Map<String, Object> requestorObject : tempRequestorData) {
                                    String requestorId = (String) requestorObject.get("requestorId");
                                    String status = (String) requestorObject.get("status");
                                    if ("pending".equals(status)) {
                                        requestorIDsOnlyPending.add(requestorId);
                                    }
                                    if ("accepted".equals(status)) {
                                        requestorIDsOnlyAccepted.add(requestorId);
                                    }
                                }

                                Runnable onComplete = () -> {


                                    ArrayList<User> users = new ArrayList<>();
                                    users.addAll(usersAccepted);
                                    users.addAll(usersPending);

                                    ArrayList<String> userNames = new ArrayList<>();
                                    userNames.addAll(userNamesAccepted);
                                    userNames.addAll(userNamesPending);



                                    gamerequestAdapter = new Game_RequestAdapter(context, gamemodel, users, userNamesPending, userNamesAccepted, requestorIDsOnlyPending, requestorIDsOnlyAccepted);
                                    holder.Request.setAdapter(gamerequestAdapter);
                                    gamerequestAdapter.notifyDataSetChanged();
                                };

                                fetchUserNames(requestorIDsOnlyPending, requestorIDsOnlyAccepted, callback, userCallback, onComplete);

                                holder.numofpl.setText(String.valueOf(requestorIDsOnlyAccepted.size()+"/"+gamemodel.getNumofplayers()));

                                if (requestorIDsOnlyPending.size() != 0) {
                                    holder.nmbofreqlayout.setVisibility(View.VISIBLE);
                                    holder.nmbofrequests.setText(String.valueOf(requestorIDsOnlyPending.size()));
                                } else if (requestorIDsOnlyPending.size() == 0) {
                                    holder.nmbofreqlayout.setVisibility(View.GONE);
                                }

                                ViewCompat.setNestedScrollingEnabled(holder.Request, false);
                                holder.Request.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            }
                        }
                    }
                });




        userNamesPending.clear();
        userNamesAccepted.clear();
        usersAccepted.clear();
        usersPending.clear();
    }

    @Override
    public int getItemCount() {
        return gameModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView gname,gdesc,numofpl,datetext,timetext,agerestext,nmbofrequests;
        public RecyclerView Request;
        ConstraintLayout irritating, expanded_layout_1, expanded_layout_2;
        FrameLayout nmbofreqlayout;
        ImageButton chatButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gname = itemView.findViewById(R.id.GameName);
            gdesc = itemView.findViewById(R.id.description);
            numofpl = itemView.findViewById(R.id.NumberOfPlayers);
            datetext = itemView.findViewById(R.id.showndate);
            timetext = itemView.findViewById(R.id.timerange);
            agerestext = itemView.findViewById(R.id.agerestrictions);
            nmbofrequests = itemView.findViewById(R.id.numberofrequests);
            expanded_layout_1 = itemView.findViewById(R.id.expanded_layout1);
            irritating = itemView.findViewById(R.id.irritating);
            expanded_layout_2 = itemView.findViewById(R.id.expanded_layout2);
            nmbofreqlayout = itemView.findViewById(R.id.layoutofnmbofrequests);
            Request = itemView.findViewById(R.id.request_recyclerview);
            chatButton = itemView.findViewById(R.id.chatButton);

            irritating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameModel gamemodels = gameModels.get(getAdapterPosition());
                    gamemodels.setvisibility(!gamemodels.isVisibility());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}


