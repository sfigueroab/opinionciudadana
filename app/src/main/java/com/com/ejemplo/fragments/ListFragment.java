package com.com.ejemplo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.com.ejemplo.R;
import com.com.ejemplo.activities.EncuestaActivity;
import com.com.ejemplo.adapters.EncuestasAdapter;
import com.com.ejemplo.model.Encuesta;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends DefaultFragment {
    private List<String> keys;
    private List<String> titulos;
    private RecyclerView lista;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        getEncuestas();
        return root;
    }

    @Override
    public View setFragmentLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frament_list, container, false);
    }

    @Override
    public void createViewItems(View root) {
        lista = root.findViewById(R.id.encuestas_list);
    }

    public void getEncuestas() {
        firestoreManager.getCollection("encuestas", queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isSuccessful()) {
                List<DocumentSnapshot> encuestas = queryDocumentSnapshots.getResult().getDocuments();
                keys = new ArrayList<>();
                titulos = new ArrayList<>();
                for(int i = 0; i < encuestas.size() ; i++) {
                    DocumentSnapshot encuestaSnap = encuestas.get(i);
                    keys.add(i, encuestaSnap.getId());
                    Encuesta encuesta = encuestaSnap.toObject(Encuesta.class);
                    titulos.add(i, encuesta.getEjemplo());
                }
                final EncuestasAdapter encuestasAdapter = new EncuestasAdapter(titulos);
                encuestasAdapter.setOnClickListener(v -> goToEncuestaPage(keys.get(lista.getChildAdapterPosition(v))));

                lista.setAdapter(encuestasAdapter);
                lista.setLayoutManager(new LinearLayoutManager(thisFragment.getActivity(), LinearLayoutManager.VERTICAL, false));
                lista.invalidate();
            } else {
            }
        });
    }

    public void goToEncuestaPage(String key){
        Intent intent = new Intent(thisFragment.getActivity(), EncuestaActivity.class);
        intent.putExtra("key", key);
        startActivity(intent);
    }
}