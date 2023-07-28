package com.example.nexusapp.proyectofinal.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.DTO.Usuario;

import com.example.nexusapp.proyectofinal.Firebase.CargarImagenGlide;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.R;
import com.example.nexusapp.proyectofinal.ui.PerfilUsuarios.PerfilDeUsuario;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterBusqueda extends RecyclerView.Adapter<AdapterBusqueda.Busqueda> {
    public AdapterBusqueda(ArrayList<Usuario> contactos, Context context) {
        this.contactos = contactos;
        this.context = context;

    }

    private ArrayList<Usuario> contactos;
    private Context context;
    public View view;

    @NonNull
    @Override
    public Busqueda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resultado_usuario_busqueda, null, false);
        return new Busqueda(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Busqueda holder, int position) {
        Usuario con = this.contactos.get(position);

        holder.rellenarDatos(con);


    }

    @Override
    public int getItemCount() {
        return this.contactos.size();
    }

    public class Busqueda extends RecyclerView.ViewHolder {
        TextView nombreUser;
        CircleImageView imagen;
        CardView cardBusqueda;

        public Busqueda(@NonNull View itemView) {
            super(itemView);
            nombreUser = itemView.findViewById(R.id.nombreUsuarioBusqueda);
            imagen = itemView.findViewById(R.id.perfilUsuarioBusqueda);
            cardBusqueda = itemView.findViewById(R.id.cardBusqueda);

            //imageView5
        }

        /**
         * Metodo que rellena los datos del ítem
         * @param con
         */
        public void rellenarDatos(Usuario con) {

            nombreUser.setText(con.getNombreUsuario());
            new CargarImagenGlide(con.getPerfil(), imagen, context);

            cardBusqueda.setOnClickListener(v -> {
                itemView.setOnClickListener(n->{
                 PerfilDeUsuario.perfilUsuarioVisible = con;
                    MainActivity.navController.navigate(R.id.perfilDeUsuariosF);
                });
                //abro perfil
               PerfilDeUsuario.perfilUsuarioVisible = con;
                MainActivity.navController.navigate(R.id.perfilDeUsuariosF);
            });
        }
    }


}
