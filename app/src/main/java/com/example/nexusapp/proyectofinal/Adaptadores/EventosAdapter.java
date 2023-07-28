package com.example.nexusapp.proyectofinal.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.DTO.Evento;
import com.example.nexusapp.proyectofinal.Firebase.CargarImagenGlide;
import com.example.nexusapp.R;

import java.util.ArrayList;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder> {

    private ArrayList<Evento> listaEventos;

    private Context miContext;
    final OnItemClickListener listener;

    public EventosAdapter(ArrayList<Evento> l, Context c, OnItemClickListener listener) {
        this.listaEventos = l;
        this.miContext = c;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(listaEventos.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listaEventos.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Evento item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textoUsuario, textoEvento;
        private ImageView portadaEvento;
        private CardView cardViewEvento;

        ViewHolder(View itemView) {
            super(itemView);

            cardViewEvento = itemView.findViewById(R.id.cardViewEvento);
            textoUsuario = itemView.findViewById(R.id.creadorEvent);
            textoEvento = itemView.findViewById(R.id.tituloEvent);
            portadaEvento = itemView.findViewById(R.id.portadaEvent);
        }

        public void bindData(Evento evento) {


            textoUsuario.setText(evento.getUsuarioCreador());
            textoEvento.setText(evento.getNombreEvento());
            System.out.println(evento.getPortada()+ "ES LA PORTADA QUE SE VA A CARHAR");
            new CargarImagenGlide(evento.getPortada(), portadaEvento, new ProgressBar(miContext), miContext);
//            new FirebaseCargaImg("content___com_android_providers_media_documents_document_image_3A30", portadaEvento);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(evento);
                }
            });

        }
    }

}
