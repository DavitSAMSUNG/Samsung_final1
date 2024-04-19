package com.example.finalproject2_0.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.GameModel;
import com.example.finalproject2_0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game_RecyclerViewAdapter_Discover extends RecyclerView.Adapter<Game_RecyclerViewAdapter_Discover.MyViewHolder> {
    Context context;
    public ArrayList<GameModel> gameModels;
    FirebaseFirestore mStore;

    public Game_RecyclerViewAdapter_Discover(Context context, ArrayList<GameModel> gameModels){
        this.mStore = FirebaseFirestore.getInstance();
        this.context = context;
        this.gameModels = gameModels;
    }
    @NonNull
    @Override
    public Game_RecyclerViewAdapter_Discover.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater. inflate (R.layout.recyclerview_row_discover, parent,false);
        return new Game_RecyclerViewAdapter_Discover.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Game_RecyclerViewAdapter_Discover.MyViewHolder holder, int position) throws Resources.NotFoundException {
        GameModel gamemodels = gameModels.get(position);
        holder.gname.setText(gameModels.get(position).getGamename());
        holder.datetext.setText(gameModels.get(position).getDate());
        holder.timetext.setText(gameModels.get(position).getTime());
        holder.gdesc.setText(gameModels.get(position).getGamedescription());
        holder.numofpl.setText(String.valueOf(gameModels.get(position).getNumofplayers()));
        holder.agerestext.setText(String.valueOf(gameModels.get(position).getAgeRes()));

        boolean isVisible = gamemodels.visibility;
        holder.constraintLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        System.out.println(gameModels.get(position));

    }

    @Override
    public int getItemCount() {
        return gameModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView gname,gdesc,numofpl,addtext,datetext,timetext,agerestext;
        public Button addbutton;
        ConstraintLayout constraintLayout,irritating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gname = itemView.findViewById(R.id.GameName);
            gdesc = itemView.findViewById(R.id.description);
            numofpl = itemView.findViewById(R.id.NumberOfPlayers);
            datetext = itemView.findViewById(R.id.showndate);
            timetext = itemView.findViewById(R.id.timerange);
            agerestext = itemView.findViewById(R.id.agerestrictions);

            addbutton = itemView.findViewById(R.id.add);

            constraintLayout = itemView.findViewById(R.id.expanded_layout);
            irritating = itemView.findViewById(R.id.irritating);

            addbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Request sent!", Toast. LENGTH_SHORT) . show();
                    //v.getId();
                    Map<String, Object> requestuserid = new HashMap<>();
                    requestuserid.put("requestorUserId", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    mStore.collection("Games").add(requestuserid);
                }
            });

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


