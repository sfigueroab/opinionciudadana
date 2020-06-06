package com.com.opinionciudadana.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anychart.AnyChartView;
import com.anychart.charts.Pie;
import com.com.opinionciudadana.R;
import com.com.opinionciudadana.model.Encuesta;
import com.com.opinionciudadana.model.Respuesta;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class EncuestaActivity extends DefaultActivity {

    private AnyChartView chart;
    private Button yes;
    private Button no;
    private String key;

    private Pie pie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        key = getIntent().getStringExtra("key");
        Log.i("debug", "Recibiendo la encuesta" + key);
        getEncuesta();
    }

    @Override
    void setLayout() {
        setContentView(R.layout.activity_encuesta);
    }

    @Override
    public void createViews() {
        super.createViews();
        // chart = findViewById(R.id.any_chart_view);
        yes = findViewById(R.id.si);
        yes.setOnClickListener(view -> sendYes(view));
        no = findViewById(R.id.no);
        no.setOnClickListener(view -> sendNo(view));

    }

    public Boolean validarEncuesta() {
        return false;
    }

    public void getEncuesta() {
        firestoreManager.getDocument("encuestas", key, queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isSuccessful()) {
                DocumentSnapshot encuesta = queryDocumentSnapshots.getResult();
                Encuesta miEncuesta = encuesta.toObject(Encuesta.class);
                // createChart(miEncuesta);
                TextView tituloTextView = findViewById(R.id.textView8);
                tituloTextView.setText(miEncuesta.getPregunta().toString());
            } else {
            }
        });
    }



    public void sendYes(View view) {
        firestoreManager.getDocument("encuestas", key, queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isSuccessful()) {
                DocumentSnapshot encuesta = queryDocumentSnapshots.getResult();
                Encuesta miEncuesta = encuesta.toObject(Encuesta.class);
                int respuestasActuales = miEncuesta.getResultados().get(0);
                respuestasActuales++;
                miEncuesta.getResultados().set(0, respuestasActuales);
                firestoreManager.setField("encuestas", key, "resultados", miEncuesta.getResultados(), task -> {
                    addData(miEncuesta);
                   // chart.refreshDrawableState();
                    this.setRespuesta(key);

                });
            }
        });
    }

    public void sendNo(View view) {
        firestoreManager.getDocument("encuestas", key, queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isSuccessful()) {
                DocumentSnapshot encuesta = queryDocumentSnapshots.getResult();
                Encuesta miEncuesta = encuesta.toObject(Encuesta.class);
                int respuestasActuales = miEncuesta.getResultados().get(1);
                respuestasActuales++;
                miEncuesta.getResultados().set(1, respuestasActuales);
                firestoreManager.setField("encuestas", key, "resultados", miEncuesta.getResultados(), task -> {
                    addData(miEncuesta);
                   // chart.refreshDrawableState();
                    this.setRespuesta(key);
                });
            }
        });
    }


    public void setRespuesta(String key) {
        firestoreManager.getDocument("users", authManager.getUser().getId(), task1 -> {
            if(!task1.isSuccessful()) {
                Log.i("debug", "esta nula");
                return;
            }

            DocumentSnapshot document = task1.getResult();
            Respuesta respuesta = document.toObject(Respuesta.class);
            if(respuesta == null) {
                Log.i("debug", "respuesta nula");
                respuesta = new Respuesta();
            }

            if(respuesta.getEncuestas() == null) {
                Log.i("debug", "nulo nulo");
                respuesta.setEncuestas(new ArrayList<>());
            }

            respuesta.getEncuestas().add(key);
            Log.i("debug", "agregamos el key " + key);
            firestoreManager.saveObject("users", authManager.getUser().getId(), respuesta, task2 -> {
                Log.i("debug", "llegue");
            });
        });
    }


    public void createChart(Encuesta encuesta) {
     /*   pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(thisActivity, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(encuesta.getEncuestas().get(0), encuesta.getResultados().get(0)));
        data.add(new ValueDataEntry(encuesta.getEncuestas().get(1), encuesta.getResultados().get(1)));

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
        */

    }

    public void addData(Encuesta encuesta) {
        /* List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(encuesta.getEncuestas().get(0), encuesta.getResultados().get(0)));
        data.add(new ValueDataEntry(encuesta.getEncuestas().get(1), encuesta.getResultados().get(1)));
        pie.data(data);
         */
    }
}
