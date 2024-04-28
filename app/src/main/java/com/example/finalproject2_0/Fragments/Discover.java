package com.example.finalproject2_0.Fragments;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject2_0.Adapters.Game_RecyclerViewAdapter_Discover;
import com.example.finalproject2_0.Models.GameModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.finalproject2_0.R;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Discover extends Fragment {

    ArrayList<GameModel> Gamemodels = new ArrayList<>();
    private List<String> documentIDs = new ArrayList<>();

    RecyclerView recyclerView;
    GameModel gameModel = new GameModel();
    Game_RecyclerViewAdapter_Discover adapter;
    FirebaseFirestore mfstore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        mfstore = FirebaseFirestore.getInstance();

        LoadMyGame();

        recyclerView = view.findViewById(R.id.GameRecyclerView);
        adapter = new Game_RecyclerViewAdapter_Discover(requireContext(), Gamemodels, documentIDs);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));



        return view;


    }

    private void LoadMyGame(){
        mfstore.collection("Games").whereNotEqualTo("owneruserid", FirebaseAuth.getInstance().getCurrentUser().getUid()).orderBy("gamename", Query.Direction.ASCENDING)
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
                                documentIDs.add(dc.getDocument().getId());
                                adapter.notifyDataSetChanged();

                            }

                        }

                    }
                });

    }


}