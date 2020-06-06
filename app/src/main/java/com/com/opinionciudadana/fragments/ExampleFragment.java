package com.com.opinionciudadana.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.com.opinionciudadana.R;
import com.com.opinionciudadana.managers.FirestoreManager;
import com.com.opinionciudadana.model.Encuesta;
import com.com.opinionciudadana.model.Example;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExampleFragment extends DefaultFragment {
    TextView exampleText;
    private AnyChartView chart;
    private Button yes;
    private Button no;
    private Pie pie;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        loadExampleTexts();
        // getEncuestas();
        return root;
    }

    @Override
    public View setFragmentLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void createViewItems(View root) {
        exampleText = root.findViewById(R.id.example);
        chart = root.findViewById(R.id.any_chart_view);
        yes = root.findViewById(R.id.si);
        yes.setOnClickListener(view -> sendYes(view));
        no = root.findViewById(R.id.no);
        no.setOnClickListener(view -> sendNo(view));

    }

    public void loadExampleTexts() {
        firestoreManager.getDocument(
                FirestoreManager.FS_COLLECTION_EXAMPLE,
                FirestoreManager.FS_DOCUMENT_EXAMPLE,
                documentSnapshot -> {
                    if(!documentSnapshot.isSuccessful()) {
                        exampleText.setText("---");
                        return;
                    }
                    Example object = documentSnapshot.getResult().toObject(Example.class);
                    exampleText.setText(object.getExampleField());
                });
    }

    public void getEncuestas() {
        firestoreManager.getCollection("encuestas", queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isSuccessful()) {
                List<DocumentSnapshot> encuestas = queryDocumentSnapshots.getResult().getDocuments();
                DocumentSnapshot encuesta = encuestas.get(1);
                Encuesta miEncuesta = encuesta.toObject(Encuesta.class);
                createChart(miEncuesta);
                exampleText.setText("Éxito");
            } else {
                exampleText.setText("Error");
            }
        });
    }

    public void sendYes(View view) {
        firestoreManager.getDocument("encuestas", "encuesta1", queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isSuccessful()) {
                DocumentSnapshot encuesta = queryDocumentSnapshots.getResult();
                Encuesta miEncuesta = encuesta.toObject(Encuesta.class);
                int respuestasActuales = miEncuesta.getResultados().get(0);
                respuestasActuales++;
                miEncuesta.getResultados().set(0, respuestasActuales);
                firestoreManager.setField("encuestas", "encuesta1", "respuestas", miEncuesta.getResultados(), task -> {
                    addData(miEncuesta);
                    chart.refreshDrawableState();
                });
                exampleText.setText("Éxito");
            } else {
                exampleText.setText("Error");
            }
        });
    }

    public void sendNo(View view) {
        firestoreManager.getDocument("encuestas", "encuesta1", queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isSuccessful()) {
                DocumentSnapshot encuesta = queryDocumentSnapshots.getResult();
                Encuesta miEncuesta = encuesta.toObject(Encuesta.class);
                int respuestasActuales = miEncuesta.getResultados().get(1);
                respuestasActuales++;
                miEncuesta.getResultados().set(1, respuestasActuales);
                firestoreManager.setField("encuestas", "encuesta1", "respuestas", miEncuesta.getResultados(), task -> {
                    addData(miEncuesta);
                    chart.refreshDrawableState();
                });
                exampleText.setText("Éxito");
            } else {
                exampleText.setText("Error");
            }
        });
    }

    public void createChart(Encuesta encuesta) {
        pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(thisFragment.getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(encuesta.getPreguntas().get(0), encuesta.getResultados().get(0)));
        data.add(new ValueDataEntry(encuesta.getPreguntas().get(1), encuesta.getResultados().get(1)));

        pie.data(data);

        pie.title(encuesta.getPregunta());

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Opciones")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        chart.setChart(pie);
    }

    public void addData(Encuesta encuesta) {
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(encuesta.getPreguntas().get(0), encuesta.getResultados().get(0)));
        data.add(new ValueDataEntry(encuesta.getPreguntas().get(1), encuesta.getResultados().get(1)));
        pie.data(data);
    }
}