package com.clase.ejemplo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clase.ejemplo.R;

import java.util.List;

public class EncuestasAdapter extends RecyclerView.Adapter<EncuestasAdapter.LineaViewHolder> implements View.OnClickListener {
    private List<String> titulos;

    private View.OnClickListener listener;

    public EncuestasAdapter(List<String> titulos) {
        this.titulos = titulos;
    }

    @NonNull
    @Override
    public LineaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_encuesta, parent, false);
        itemView.setOnClickListener(this);
        LineaViewHolder viewHolder = new LineaViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LineaViewHolder holder, int position) {
        holder.setEncuesta(titulos.get(position));
    }

    @Override
    public int getItemCount() {
        return titulos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public static class LineaViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public LineaViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nombre);
        }

        public void setEncuesta(String titulo) {
            name.setText(titulo);
        }
    }
}
