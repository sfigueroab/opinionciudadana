package com.com.opinionciudadana.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.com.opinionciudadana.model.Encuesta;
import com.com.opinionciudadana.model.Respuesta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
        chart = findViewById(R.id.any_chart_view);
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
                createChart(miEncuesta);


                Button si = findViewById(R.id.si);
                si.setText(miEncuesta.getPreguntas().get(0).toString());
                si.setEnabled(false);

                Button no = findViewById(R.id.no);
                no.setText(miEncuesta.getPreguntas().get(1).toString());
                no.setEnabled(false);

                validarUsuario(key);
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
                    chart.refreshDrawableState();
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
                    chart.refreshDrawableState();
                    this.setRespuesta(key);
                });
            }
        });
    }

    public void validarUsuario(String encuestaId) {
        firestoreManager.getDocument("users", authManager.getUser().getId(), task -> {
            if(!task.isSuccessful()) {
                return;
            }
            DocumentSnapshot _respuesta = task.getResult();
            Respuesta respuesta = _respuesta.toObject(Respuesta.class);

            if(respuesta == null || respuesta.getEncuestas() == null) {
                Button si = findViewById(R.id.si);
                si.setEnabled(true);

                Button no = findViewById(R.id.no);
                no.setEnabled(true);

                return;
            }

            boolean activar = true;
            for (String res : respuesta.getEncuestas()) {
                if(res == encuestaId) {
                    activar = false;
                }
            }

            if(activar) {
                Button si = findViewById(R.id.si);
                si.setEnabled(true);

                Button no = findViewById(R.id.no);
                no.setEnabled(true);
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
        validarUsuario(key);
        new AlertDialog.Builder(EncuestaActivity.this)
                .setTitle("Muchas gracias")
                .setMessage("Su respuesta ha sido guardada con éxito").show();
    }


    public void createChart(Encuesta encuesta) {
        pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(thisActivity, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        Log.i("debug", "Resultados"  + encuesta.getResultados().get(0).toString());
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
        /* List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(encuesta.getEncuestas().get(0), encuesta.getResultados().get(0)));
        data.add(new ValueDataEntry(encuesta.getEncuestas().get(1), encuesta.getResultados().get(1)));
        pie.data(data);
         */
    }
}
