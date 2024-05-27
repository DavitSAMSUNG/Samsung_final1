package com.example.finalproject2_0.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2_0.Adapters.Game_RecyclerViewAdapter_Discover;
import com.example.finalproject2_0.Models.GameModel;
import com.example.finalproject2_0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Discover extends Fragment {

    ArrayList<GameModel> Gamemodels = new ArrayList<>();
    private List<String> documentIDs = new ArrayList<>();
    private SearchView searchView;
    RecyclerView recyclerView;
    Game_RecyclerViewAdapter_Discover adapter;
    FirebaseFirestore mfstore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        mfstore = FirebaseFirestore.getInstance();
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        String[] provinces = {"All", "Yerevan", "Aragatsotn", "Ararat", "Armavir", "Gegharkunik", "Kotayk", "Lori", "Shirak", "Syunik", "Tavush", "Vayots Dzor"};
        Spinner provincespinner = view.findViewById(R.id.filter_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                R.layout.spinneritem_, provinces);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provincespinner.setAdapter(spinnerAdapter);

        LoadGames();

        adapter = new Game_RecyclerViewAdapter_Discover(requireContext(), Gamemodels);
        Handler handler = new Handler();
        handler.postDelayed(() -> provincespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = parent.getItemAtPosition(position).toString();
                adapter.filterByProvince(selectedProvince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.filterByProvince("All");
            }
        }), 200);

        recyclerView = view.findViewById(R.id.GameRecyclerView);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    private void filterList(String text) {
        List<GameModel> filteredList = new ArrayList<>();
        for (GameModel gameModel : Gamemodels) {
            if (gameModel.getGamename().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(gameModel);
            }
        }
        adapter.setFilteredGameModels(filteredList);
    }

    private void LoadGames() {
        mfstore.collection("Games")
                .whereNotEqualTo("owneruserid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
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
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
