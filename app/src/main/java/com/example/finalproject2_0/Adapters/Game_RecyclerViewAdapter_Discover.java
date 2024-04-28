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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Game_RecyclerViewAdapter_Discover extends RecyclerView.Adapter<Game_RecyclerViewAdapter_Discover.MyViewHolder> {
    Context context;
    public ArrayList<GameModel> gameModels;
    FirebaseFirestore mStore;
    private List<String> documentIDs;

    public Game_RecyclerViewAdapter_Discover(Context context, ArrayList<GameModel> gameModels, List<String> documentIDs){
        this.mStore = FirebaseFirestore.getInstance();
        this.context = context;
        this.gameModels = gameModels;
        this.documentIDs = documentIDs;
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
        //String docId = gamemodels.getDocumentID();
        //holder.itemView.setTag(docId);
        holder.gname.setText(gameModels.get(position).getGamename());
        holder.datetext.setText(gameModels.get(position).getDate());
        holder.timetext.setText(gameModels.get(position).getTime());
        holder.gdesc.setText(gameModels.get(position).getGamedescription());
        holder.numofpl.setText(String.valueOf(gameModels.get(position).getNumofplayers()));
        holder.agerestext.setText(String.valueOf(gameModels.get(position).getAgeRes()));
        Button addbutton = holder.itemView.findViewById(R.id.add);

        String currentDocumentID = documentIDs.get(position);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(context, "Request sent!", Toast. LENGTH_SHORT) . show();

                                    mStore.collection("Games").document(currentDocumentID)
                                            .update("requestorIds", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                                }else {
                                    Toast.makeText(context, "Incomplete Profile", Toast. LENGTH_SHORT) . show();
                                }
                            }
                        });

            }
        });


        boolean isVisible = gamemodels.visibility;
        holder.constraintLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        System.out.println(gameModels.get(position));

    }

    @Override
    public int getItemCount() {
        return gameModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView gname,gdesc,numofpl,datetext,timetext,agerestext;

        //public Button addbutton;
        ConstraintLayout constraintLayout,irritating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gname = itemView.findViewById(R.id.GameName);
            gdesc = itemView.findViewById(R.id.description);
            numofpl = itemView.findViewById(R.id.NumberOfPlayers);
            datetext = itemView.findViewById(R.id.showndate);
            timetext = itemView.findViewById(R.id.timerange);
            agerestext = itemView.findViewById(R.id.agerestrictions);
            //String documentId = gamemodels.getDocumentID();


            constraintLayout = itemView.findViewById(R.id.expanded_layout);
            irritating = itemView.findViewById(R.id.irritating);

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


