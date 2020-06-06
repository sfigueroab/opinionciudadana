package com.com.ejemplo.model;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Encuesta {
    private List<String> opciones;
    private List<Integer> respuestas;
    private String ejemplo;

    @PropertyName("opciones")
    public List<String> getOpciones() {
        return opciones;
    }

    @PropertyName("opciones")
    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    @PropertyName("respuestas")
    public List<Integer> getRespuestas() {
        return respuestas;
    }

    @PropertyName("respuestas")
    public void setRespuestas(List<Integer> respuestas) {
        this.respuestas = respuestas;
    }

    @PropertyName("titulo")
    public String getEjemplo() {
        return ejemplo;
    }

    @PropertyName("titulo")
    public void setEjemplo(String ejemplo) {
        this.ejemplo = ejemplo;
    }
}
