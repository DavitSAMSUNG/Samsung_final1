package com.example.finalproject2_0;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Game_RecyclerViewAdapter extends RecyclerView.Adapter<Game_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    public ArrayList<GameModel> gameModels;

    public Game_RecyclerViewAdapter(Context context, ArrayList<GameModel> gameModels){
        this.context = context;
        this.gameModels = gameModels;
    }
    @NonNull
    @Override
    public Game_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater. inflate (R. layout.recycler_view_row, parent,false);
        return new Game_RecyclerViewAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Game_RecyclerViewAdapter.MyViewHolder holder, int position) throws Resources.NotFoundException {
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
        ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gname = itemView.findViewById(R.id.GameName);
            gdesc = itemView.findViewById(R.id.description);
            numofpl = itemView.findViewById(R.id.NumberOfPlayers);
            datetext = itemView.findViewById(R.id.showndate);
            timetext = itemView.findViewById(R.id.timerange);
            agerestext = itemView.findViewById(R.id.agerestrictions);
            constraintLayout = itemView.findViewById(R.id.expanded_layout);

            gname.setOnClickListener(new View.OnClickListener() {
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


