package com.example.finalproject2_0.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.R;

import java.util.ArrayList;

public class Game_RecyclerViewAdapter_MyGames extends RecyclerView.Adapter<Game_RecyclerViewAdapter_MyGames.MyViewHolder> {
    Context context;
    public ArrayList<GameModel> gameModels;

    public Game_RecyclerViewAdapter_MyGames(Context context, ArrayList<GameModel> gameModels){
        this.context = context;
        this.gameModels = gameModels;
    }
    @NonNull
    @Override
    public Game_RecyclerViewAdapter_MyGames.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater. inflate (R.layout.recyclerview_row_mygames, parent,false);
        return new Game_RecyclerViewAdapter_MyGames.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Game_RecyclerViewAdapter_MyGames.MyViewHolder holder, int position) throws Resources.NotFoundException {
        GameModel gamemodels = gameModels.get(position);
        holder.gname.setText(gameModels.get(position).getGamename());
        holder.datetext.setText(gameModels.get(position).getDate());
        holder.timetext.setText(gameModels.get(position).getTime());
        holder.gdesc.setText(gameModels.get(position).getGamedescription());
        holder.numofpl.setText(String.valueOf(gameModels.get(position).getNumofplayers()));
        holder.agerestext.setText(String.valueOf(gameModels.get(position).getAgeRes()));

        boolean isVisible = gamemodels.visibility;
        holder.expanded_layout_1.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        holder.expanded_layout_2.setVisibility(isVisible ? View.VISIBLE : View.GONE);


        System.out.println(gameModels.get(position));

    }

    @Override
    public int getItemCount() {
        return gameModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView gname,gdesc,numofpl,datetext,timetext,agerestext;
        ConstraintLayout irritating, expanded_layout_1, expanded_layout_2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gname = itemView.findViewById(R.id.GameName);
            gdesc = itemView.findViewById(R.id.description);
            numofpl = itemView.findViewById(R.id.NumberOfPlayers);
            datetext = itemView.findViewById(R.id.showndate);
            timetext = itemView.findViewById(R.id.timerange);
            agerestext = itemView.findViewById(R.id.agerestrictions);
            expanded_layout_1 = itemView.findViewById(R.id.expanded_layout1);
            irritating = itemView.findViewById(R.id.irritating);
            expanded_layout_2 = itemView.findViewById(R.id.expanded_layout2);

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


