package com.example.finalproject2_0.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.R;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Game_RecyclerViewAdapter_Discover extends RecyclerView.Adapter<Game_RecyclerViewAdapter_Discover.MyViewHolder> {
    Context context;
    public ArrayList<GameModel> gameModels;
    private List<GameModel> filteredGamemodelslist;
    FirebaseFirestore mStore;
    int acceptedCount;

    public Game_RecyclerViewAdapter_Discover(Context context, ArrayList<GameModel> gameModels) {
        this.mStore = FirebaseFirestore.getInstance();
        this.context = context;
        this.gameModels = gameModels;
        this.filteredGamemodelslist = new ArrayList<>(gameModels);
    }

    public void setFilteredGameModels(List<GameModel> filteredGameModels){
        this.filteredGamemodelslist = new ArrayList<>(filteredGameModels);
        notifyDataSetChanged();
    }

    public void filterByProvince(String province) {
        Log.d("hello", "filterByProvince: ");
        List<GameModel> tempFilteredList = new ArrayList<>();
        if (province.equals("All")) {
            tempFilteredList.addAll(gameModels);
        } else {
            for (GameModel gamemodel : gameModels) {
                if (gamemodel.getLocation() != null && gamemodel.getLocation().equals(province)) {
                    tempFilteredList.add(gamemodel);
                }
            }
        }
        setFilteredGameModels(tempFilteredList);
    }

    @NonNull
    @Override
    public Game_RecyclerViewAdapter_Discover.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row_discover, parent, false);
        return new Game_RecyclerViewAdapter_Discover.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Game_RecyclerViewAdapter_Discover.MyViewHolder holder, int position) throws Resources.NotFoundException {
        GameModel gamemodel = filteredGamemodelslist.get(position);
        holder.gname.setText(gamemodel.getGamename());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        holder.datetext.setText(dateFormat.format(gamemodel.getDate()));
        holder.timetext.setText(gamemodel.getTime());
        holder.gdesc.setText(gamemodel.getGamedescription());

        acceptedCount = 0;
        if (gamemodel.getRequestorIds() != null){
            for (Map<String, Object> hashMap : gamemodel.getRequestorIds()) {
                if ("accepted".equals(hashMap.get("status"))) {
                    acceptedCount++;
                }
            }
        }

        holder.numofpl.setText(acceptedCount+"/"+gamemodel.getNumofplayers());
        holder.agerestext.setText(String.valueOf(gamemodel.getAgeRes()));
        Button addbutton = holder.itemView.findViewById(R.id.add);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    mStore.collection("Games").document(gamemodel.getDocumentID()).get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot gameDocument = task1.getResult();
                                            boolean userAlreadyRequested = false;
                                            if (gameDocument.exists() && gameDocument.get("requestorIds") instanceof List) {
                                                List<Map<String, Object>> existingRequestors = (List<Map<String, Object>>) gameDocument.get("requestorIds");
                                                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                for (Map<String, Object> requestor : existingRequestors) {
                                                    String requestorId = (String) requestor.get("requestorId");
                                                    if (currentUserId.equals(requestorId)) {
                                                        userAlreadyRequested = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (userAlreadyRequested) {
                                                Toast.makeText(context, "You've already requested this game", Toast.LENGTH_SHORT).show();
                                            } else if(acceptedCount>=Integer.parseInt(gamemodel.getNumofplayers())) {
                                                Toast.makeText(context, "No spots available", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Request sent!", Toast.LENGTH_SHORT).show();
                                                Map<String, Object> requestorObject = new HashMap<>();
                                                requestorObject.put("requestorId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                requestorObject.put("status", "pending");
                                                mStore.collection("Games").document(gamemodel.getDocumentID())
                                                        .update("requestorIds", FieldValue.arrayUnion(requestorObject));
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(context, "Incomplete Profile", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        boolean isVisible = gamemodel.visibility;
        holder.constraintLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return filteredGamemodelslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView gname, gdesc, numofpl, datetext, timetext, agerestext;
        CardView cardViewdiscover;
        ConstraintLayout constraintLayout, irritating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gname = itemView.findViewById(R.id.GameName);
            gdesc = itemView.findViewById(R.id.description);
            numofpl = itemView.findViewById(R.id.NumberOfPlayers);
            datetext = itemView.findViewById(R.id.showndate);
            timetext = itemView.findViewById(R.id.timerange);
            agerestext = itemView.findViewById(R.id.agerestrictions);
            cardViewdiscover = itemView.findViewById(R.id.cardview);
            itemView.setOnClickListener(this);
            constraintLayout = itemView.findViewById(R.id.expanded_layout);
            irritating = itemView.findViewById(R.id.irritating);
            irritating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameModel gamemodels = filteredGamemodelslist.get(getAdapterPosition());
                    gamemodels.setvisibility(!gamemodels.isVisibility());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
        }
    }
}
