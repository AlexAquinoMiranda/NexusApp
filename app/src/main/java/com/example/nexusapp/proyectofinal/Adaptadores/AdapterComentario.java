package com.example.nexusapp.proyectofinal.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.DTO.Comentar;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.Comentario> {
    public AdapterComentario(ArrayList<Comentar> contactos, Context context) {
        this.comentarios = contactos;
        this.context = context;
    }


    private ArrayList<Comentar> comentarios;
    private Context context;


    @NonNull
    @Override
    public Comentario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comentario_res, null, false);
        return new Comentario(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Comentario holder, int position) {
        Comentar con = this.comentarios.get(position);
        holder.nombreUser.setText(con.getNombreUsuario());
        new FirebaseCargaImg(con.getPerfilUsuario(), holder.imagen);
       holder.texto.setText(con.getComentario());

    }

    @Override
    public int getItemCount() {
        return this.comentarios.size();
    }

    public class Comentario extends RecyclerView.ViewHolder {
        private TextView nombreUser;
        CircleImageView imagen;
        TextView texto;

        public Comentario(@NonNull View itemView) {
            super(itemView);

            texto = itemView.findViewById(R.id.textoComentario);
            nombreUser = itemView.findViewById(R.id.nombreUsuarioComenta);
            imagen = itemView.findViewById(R.id.perfilComentador);
            //imageView5
        }
    }
}
