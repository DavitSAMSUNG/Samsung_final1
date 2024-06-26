package com.example.finalproject2_0.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.finalproject2_0.Adapters.Game_AcceptedAdapter;
import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.Adapters.Game_RecyclerViewAdapter_MyGames;
import com.example.finalproject2_0.NewGameCreation;
import com.example.finalproject2_0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MyGames extends Fragment {

    private Button create;
    ArrayList<GameModel> Gamemodels = new ArrayList<>();

    private List<String> documentIDs = new ArrayList<>();
    RecyclerView recyclerView;
    Game_RecyclerViewAdapter_MyGames adapter;
    FirebaseFirestore mfstore;
    //_____________________________________________________
    ArrayList<GameModel> Gamemodelsaccepted = new ArrayList<>();
    private List<String> documentIDsaccepted = new ArrayList<>();
    RecyclerView recyclerViewaccepted;
    Game_AcceptedAdapter adapteraccepted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mygames, container, false);

        mfstore = FirebaseFirestore.getInstance();


        create = view.findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfstore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    openNewGameCreation();
                                }else {
                                    Toast.makeText(requireContext(), "Incomplete Profile", Toast.LENGTH_SHORT) . show();
                                }
                            }
                        });

            }
        });

        LoadMyGames(view);

        LoadAcceptedGames(view);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!Gamemodelsaccepted.isEmpty()){
                    recyclerView = view.findViewById(R.id.GameRecyclerView);
                    adapter = new Game_RecyclerViewAdapter_MyGames(requireContext(), Gamemodels);
                    recyclerView.setItemAnimator(new SlideInUpAnimator());

                    RecyclerView.ItemDecoration itemDecoration = new
                            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
                    recyclerView.addItemDecoration(itemDecoration);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    adapter.notifyDataSetChanged();
                }
            }
        }, 200);

        return view;
    }
    private void LoadAcceptedGames(View view){
        mfstore.collection("Games")
                .whereNotEqualTo("owneruserid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereArrayContains("requestorIds", new HashMap<String, Object>() {{
                    put("requestorId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    put("status", "accepted");
                }})
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            System.out.println(Objects.requireNonNull(error.getMessage()));
                            return;
                        }
                        assert value != null;
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Gamemodelsaccepted.add(dc.getDocument().toObject(GameModel.class));
                                documentIDsaccepted.add(dc.getDocument().getId());

                                for (int i = 0; i < Gamemodelsaccepted.size(); i++) {
                                    GameModel gameModelaccepted = Gamemodelsaccepted.get(i);
                                    String documentIdaccepted = documentIDsaccepted.get(i);
                                    gameModelaccepted.setDocumentID(documentIdaccepted);
                                }
                                //Gamemodels.add(dc.getDocument().getId());
                            }
                        }

                        recyclerViewaccepted = view.findViewById(R.id.GameRecyclerViewAccepted);
                        adapteraccepted = new Game_AcceptedAdapter(requireContext(), Gamemodelsaccepted);
                        recyclerViewaccepted.setItemAnimator(new SlideInUpAnimator());

                        recyclerViewaccepted.setAdapter(adapteraccepted);
                        recyclerViewaccepted.setHasFixedSize(true);
                        recyclerViewaccepted.setLayoutManager(new LinearLayoutManager(requireContext()));
                        adapteraccepted.notifyDataSetChanged();
                    }
                });
    }


    private void LoadMyGames(View view){
        mfstore.collection("Games")
            .whereEqualTo("owneruserid", FirebaseAuth.getInstance().getCurrentUser().getUid())
            .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firestore error", Objects.requireNonNull(error.getMessage()));
                            return;
                        }


                        assert value != null;
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Gamemodels.add(dc.getDocument().toObject(GameModel.class));
                                documentIDs.add(dc.getDocument().getId());

                                for (int i = 0; i < Gamemodels.size(); i++) {
                                    GameModel gameModel = Gamemodels.get(i);
                                    String documentId = documentIDs.get(i);
                                    gameModel.setDocumentID(documentId);
                                }
                                //Gamemodels.add(dc.getDocument().getId());
                            }
                        }


                    }
                });
//        mfstore.collection("Games").orderBy("gamename", Query.Direction.ASCENDING)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if(error != null){
//                            Log.e("Firestore error",error.getMessage());
//                            return;
//                        }
//
//                        assert value != null;
//                        for (DocumentChange dc : value.getDocumentChanges()){
//                            if (dc.getType() == DocumentChange.Type.REMOVED) {// Remove the game from Gamemodels
//                                Gamemodels.remove(dc.getDocument().toObject(GameModel.class));
//                            }
//
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                });
    }



    private void openNewGameCreation(){
        Intent intent = new Intent(getActivity(), NewGameCreation.class);
        startActivity(intent);
    }
}