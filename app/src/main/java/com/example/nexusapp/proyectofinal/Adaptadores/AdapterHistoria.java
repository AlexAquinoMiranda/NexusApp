package com.example.nexusapp.proyectofinal.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.DTO.Historia;
import com.example.nexusapp.proyectofinal.Firebase.CargarImagenGlide;
import com.example.nexusapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterHistoria extends RecyclerView.Adapter<AdapterHistoria.HistoriaLogo> {

    private ArrayList<Historia> listaHistoria;
    Context context;

    public AdapterHistoria(ArrayList<Historia> i, Context context) {
        this.listaHistoria = i;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoriaLogo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historia_res, null, false);
        return new HistoriaLogo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriaLogo holder, int position) {
        Historia historia = listaHistoria.get(position);
        System.out.println(historia.toString());
        new CargarImagenGlide(historia.getPerfilUsuario(), holder.imagen, context);//cargo la imagen de perfil
        holder.nombreUser.setText(historia.getUsuarioNombre());//muestro el nombre

        holder.imagen.setOnClickListener(v -> {
            com.example.nexusapp.proyectofinal.Historia.Historia.historiaVisualizar = historia;
            Intent i = new Intent(v.getContext(), com.example.nexusapp.proyectofinal.Historia.Historia.class);
            v.getContext().startActivity(i);
        });
        //cuando se haga click en el perfil se debe de mostrar la historia
        // con siguiente y retroceder. y se se acaba que vuelva a normal...

    }

    @Override
    public int getItemCount() {

        return this.listaHistoria.size();
    }

    public class HistoriaLogo extends RecyclerView.ViewHolder {
        private TextView nombreUser;
        CircleImageView imagen;

        public HistoriaLogo(@NonNull View itemView) {
            super(itemView);
            nombreUser = itemView.findViewById(R.id.nombreUsuarioHistoria);
            imagen = itemView.findViewById(R.id.imageUserHistory);
        }
    }
}
