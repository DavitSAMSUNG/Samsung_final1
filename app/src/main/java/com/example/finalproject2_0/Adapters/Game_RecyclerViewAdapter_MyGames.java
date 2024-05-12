package com.example.finalproject2_0.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.Models.User;
import com.example.finalproject2_0.R;
import com.example.finalproject2_0.RequestorClickListener;
import com.example.finalproject2_0.UserNameCallback;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Game_RecyclerViewAdapter_MyGames extends RecyclerView.Adapter<Game_RecyclerViewAdapter_MyGames.MyViewHolder> {
    Context context;
    public ArrayList<GameModel> gameModels;
    ArrayList<User> users;
    int size;
    List<String> userNames;
    Game_RequestAdapter gamerequestAdapter;
    FirebaseFirestore mStore;

    public Game_RecyclerViewAdapter_MyGames(Context context, ArrayList<GameModel> gameModels){
        this.mStore = FirebaseFirestore.getInstance();
        this.context = context;
        this.gameModels = gameModels;
        this.userNames = new ArrayList<>();
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

    private void fetchUserNames(List<String> requestorIDs, UserNameCallback callback) {
        for (String requestorid : requestorIDs) {
            mStore.collection("users").document(requestorid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            users.add(documentSnapshot.toObject(User.class));
                            String userName = documentSnapshot.getString("name");
                            callback.onUserNameFetched(userName);
                        }
                    });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Game_RecyclerViewAdapter_MyGames.MyViewHolder holder, int position) throws Resources.NotFoundException {
        userNames.clear();
        GameModel gamemodel = gameModels.get(position);
        holder.gname.setText(gameModels.get(position).getGamename());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        holder.datetext.setText(dateFormat.format(gamemodel.getDate()));
        holder.timetext.setText(gameModels.get(position).getTime());
        holder.gdesc.setText(gameModels.get(position).getGamedescription());
        holder.numofpl.setText(String.valueOf(gameModels.get(position).getNumofplayers()));
        holder.agerestext.setText(String.valueOf(gameModels.get(position).getAgeRes()));



        boolean isVisible = gamemodel.visibility;
        holder.expanded_layout_1.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        holder.expanded_layout_2.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        String documentId = gamemodel.getDocumentID();

        mStore.collection("Games").document(documentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.get("requestorIds") instanceof List){
                            List<Map<String, Object>> tempRequestorData = (List<Map<String, Object>>) documentSnapshot.get("requestorIds");
                            List<String> requestorIDsOnly = new ArrayList<>();
                            if (requestorIDsOnly != null && !tempRequestorData.isEmpty()) {
                                for (Map<String, Object> requestorObject : tempRequestorData) {
                                    String requestorId = (String) requestorObject.get("requestorId");
                                    String status = (String) requestorObject.get("status");
                                    if ("pending".equals(status)) {
                                        requestorIDsOnly.add(requestorId);
                                    }

                                }

                                fetchUserNames(requestorIDsOnly, userName -> {
                                    userNames.add(userName);
                                    //holder.username.setText(userNames.get(position));
                                    gamerequestAdapter = new Game_RequestAdapter(context, gamemodel,users, userNames, requestorIDsOnly);
                                    if(requestorIDsOnly.size()!=0){
                                        holder.nmbofreqlayout.setVisibility(View.VISIBLE);
                                        holder.nmbofrequests.setText(String.valueOf(requestorIDsOnly.size()));
                                    }

                                    //Scrolling issue

                                    ViewCompat.setNestedScrollingEnabled(holder.Request, false);
                                    holder.Request.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                    holder.Request.setAdapter(gamerequestAdapter);
                                });
                            }
                        }

                    }
                });


        userNames.clear();

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


