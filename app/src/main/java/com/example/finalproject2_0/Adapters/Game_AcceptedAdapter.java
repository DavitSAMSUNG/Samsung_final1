package com.example.finalproject2_0.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Chat;
import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class Game_AcceptedAdapter extends RecyclerView.Adapter<Game_AcceptedAdapter.MyViewHolder>{
    Context context;
    public ArrayList<GameModel> acceptedgameModels;
    FirebaseFirestore mStore;
    public Game_AcceptedAdapter(Context context, ArrayList<GameModel> acceptedgameModels) {
        this.mStore = FirebaseFirestore.getInstance();
        this.context = context;
        this.acceptedgameModels = acceptedgameModels;
    }

    @NonNull
    @Override
    public Game_AcceptedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recyclerview_row_accepted, parent, false);
        return new Game_AcceptedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Game_AcceptedAdapter.MyViewHolder holder, int position) throws Resources.NotFoundException {
        GameModel acceptedgamemodel = acceptedgameModels.get(position);

        holder.gname.setText(acceptedgamemodel.getGamename());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        holder.datetext.setText(dateFormat.format(acceptedgamemodel.getDate()));

        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("gameID", acceptedgamemodel.documentID);
                intent.putExtra("gameName", acceptedgamemodel.getGamename());
                context.startActivity(intent);
            }
        });


        int acceptedCount = 0;
        if (acceptedgamemodel.getRequestorIds() != null){
            for (Map<String, Object> hashMap : acceptedgamemodel.getRequestorIds()) {
                if ("accepted".equals(hashMap.get("status"))) {
                    acceptedCount++;
                }
            }
        }
        holder.numofpl.setText(acceptedCount+"/"+acceptedgamemodel.getNumofplayers());
        holder.timetext.setText(acceptedgamemodel.getTime());
        holder.gdesc.setText(acceptedgamemodel.getGamedescription());
        holder.agerestext.setText(String.valueOf(acceptedgamemodel.getAgeRes()));

        boolean isVisible = acceptedgamemodel.visibility;
        holder.constraintLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return acceptedgameModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView gname, gdesc, numofpl, datetext, timetext, agerestext;
        CardView cardViewdiscover;

        //public Button addbutton;
        ConstraintLayout constraintLayout, irritating;
        Button chatButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            gname = itemView.findViewById(R.id.GameName);
            gdesc = itemView.findViewById(R.id.description);
            numofpl = itemView.findViewById(R.id.NumberOfPlayers);
            datetext = itemView.findViewById(R.id.showndate);
            timetext = itemView.findViewById(R.id.timerange);
            agerestext = itemView.findViewById(R.id.agerestrictions);
            cardViewdiscover = itemView.findViewById(R.id.cardviewaccepted);
            chatButton = itemView.findViewById(R.id.chatButton);
            itemView.setOnClickListener(this);

            constraintLayout = itemView.findViewById(R.id.expanded_layout);
            irritating = itemView.findViewById(R.id.irritating);

            irritating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameModel gamemodel = acceptedgameModels.get(getAdapterPosition());
                    gamemodel.setvisibility(!gamemodel.isVisibility());
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
