package com.com.opinionciudadana.model;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Encuesta {
    private List<String> preguntas;
    private List<Integer> resultados;
    private String pregunta;

    @PropertyName("preguntas")
    public List<String> getPreguntas() {
        return preguntas;
    }

    @PropertyName("preguntas")
    public void setPreguntas(List<String> preguntas) {
        this.preguntas = preguntas;
    }

    @PropertyName("resultados")
    public List<Integer> getResultados() {
        return resultados;
    }

    @PropertyName("resultados")
    public void setResultados(List<Integer> resultados) {
        this.resultados = resultados;
    }

    @PropertyName("pregunta")
    public String getPregunta() {
        return pregunta;
    }

    @PropertyName("pregunta")
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
}
