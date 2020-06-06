package com.com.opinionciudadana.model;
import java.util.List;
import com.google.firebase.firestore.PropertyName;

public class Respuesta {
    private String id;
    private List<String> encuestas;

    @PropertyName("encuestas")
    public List<String> getEncuestas() {
        return encuestas;
    }

    @PropertyName("encuestas")
    public void setEncuestas(List<String> encuestas) {
        this.encuestas = encuestas;
    }
}
