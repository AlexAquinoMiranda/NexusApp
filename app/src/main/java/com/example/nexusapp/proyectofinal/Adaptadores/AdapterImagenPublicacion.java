package com.example.nexusapp.proyectofinal.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.DTO.Publicacion;
import com.example.nexusapp.proyectofinal.Firebase.CargarImagenGlide;
import com.example.nexusapp.R;

import java.util.ArrayList;

public class AdapterImagenPublicacion extends RecyclerView.Adapter<AdapterImagenPublicacion.publication> {

    private ArrayList<Publicacion> lisPublicaciones;
    private Context contexto;
    public static int valorItemPresionado;
    public static boolean condicion = false;
    OnItemClickListener listener;


    public AdapterImagenPublicacion(ArrayList<Publicacion> lista, Context con, OnItemClickListener listener) {
        this.lisPublicaciones = lista;
        this.contexto = con;
        this.listener = listener;

    }

    public interface OnItemClickListener {

        void onItemClick(Publicacion item);
    }


    @NonNull
    @Override
    public publication onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_mis_publicaciones, null, false);
        return new publication(view);
    }

    @Override
    public void onBindViewHolder(@NonNull publication holder, int position) {
        int value = position;


        Publicacion publicacion = this.lisPublicaciones.get(value);

        holder.bindData(publicacion);



    }

    @Override
    public int getItemCount() {
        return this.lisPublicaciones.size();
    }

    public class publication extends RecyclerView.ViewHolder {
        ImageView imgPublication;

        public publication(@NonNull View itemView) {
            super(itemView);
            imgPublication = itemView.findViewById(R.id.publicacionImageSolo);

        }

        public void bindData(Publicacion publicacion) {


            new CargarImagenGlide(publicacion.getUrlImagen(), imgPublication, new ProgressBar(contexto),contexto);


            // new FirebaseCargaImg("content___com_android_providers_media_documents_document_image_3A30", portadaEvento);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(publicacion);
                }
            });

        }
    }
}
