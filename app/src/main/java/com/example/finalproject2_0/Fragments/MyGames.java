package com.example.finalproject2_0.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.finalproject2_0.GameModel;
import com.example.finalproject2_0.Game_RecyclerViewAdapter;
import com.example.finalproject2_0.NewGameCreation;
import com.example.finalproject2_0.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MyGames extends Fragment {

    private Button create;
    ArrayList<GameModel> Gamemodels = new ArrayList<>();
    RecyclerView recyclerView;
    Game_RecyclerViewAdapter adapter;
    FirebaseFirestore mfstore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mygames, container, false);

        mfstore = FirebaseFirestore.getInstance();
//        ImageView a = ItemView.findViewById(R.id.add);


        create = (Button) view.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewGameCreation();
            }
        });


        recyclerView = view.findViewById(R.id.GameRecyclerView);
        adapter = new Game_RecyclerViewAdapter(requireContext(), Gamemodels);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        EventChangeListener();

        return view;
    }
    private void EventChangeListener(){
        mfstore.collection("Games").orderBy("gamename", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firestore error",error.getMessage()) ;
                            return;
                        }

                        assert value != null;
                        for (DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                Gamemodels.add(dc.getDocument().toObject(GameModel.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
//    private void setUpGameModels() {
//        Bundle args = getArguments();
//        ArrayList<Integer> numofplayers = new ArrayList<>();
//        ArrayList<String> gamenames = new ArrayList<>();
//        ArrayList<String> gamedescriptions = new ArrayList<>();
//        if (args != null) {
//            gamenames.add(args.getString("GameName"));
//            gamedescriptions.add(args.getString("GameDesc"));
//            numofplayers.add(args.getInt("NumberOfPlayers"));
//        }
//
//        for (int i = 0; i < gamenames.size(); i++) {
//            Gamemodels.add(new GameModel(gamenames.get(i), gamedescriptions.get(i), numofplayers.get(i)));
//        }
//        System.out.println(gamenames.size());
//
//    }

    private void openNewGameCreation(){
        Intent intent = new Intent(getActivity(), NewGameCreation.class);
        startActivity(intent);
    }
}