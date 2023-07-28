package com.example.nexusapp.proyectofinal.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Auxiliares.AuxiliarChat;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.CargarImagenGlide;
import com.example.nexusapp.proyectofinal.Mensajeria.Mensajeria;
import com.example.nexusapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContactos extends RecyclerView.Adapter<AdapterContactos.Contact>{
    public AdapterContactos(ArrayList<Usuario> contactos, Context context) {
        this.contactos = contactos;
        this.context = context;
    }

    private ArrayList<Usuario> contactos;
    private Context context;


    @NonNull
    @Override
    public Contact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_contacto_res, null, false);
        return new Contact(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Contact holder, int position) {
        Usuario con = this.contactos.get(position);
        holder.nombreUser.setText(con.getNombreUsuario());
        new CargarImagenGlide(con.getPerfil(), holder.imagen, context);

        holder.send.setOnClickListener(n -> {
            AuxiliarChat a = new AuxiliarChat(con);
            //a.mostrar();
            //a.crearConversation(con);

            System.out.println("abrir chatttttttt");
            Intent i = new Intent(context, Mensajeria.class);
            n.getContext().startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return this.contactos.size();
    }

    public class Contact extends RecyclerView.ViewHolder {
        private TextView nombreUser;
        CircleImageView imagen ;
        FloatingActionButton send;

        public Contact(@NonNull View itemView) {
            super(itemView);

           send = itemView.findViewById(R.id.floatingActionButton2);
            nombreUser = itemView.findViewById(R.id.nombreContacto);
            imagen = itemView.findViewById(R.id.imageView5);
            //imageView5
        }
    }
}
