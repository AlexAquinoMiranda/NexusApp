package com.example.nexusapp.proyectofinal.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.R;
import com.example.nexusapp.proyectofinal.Login.Login;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUsuariosInvitar extends RecyclerView.Adapter<AdapterUsuariosInvitar.UsuarioInvitar> {
    private ArrayList<Usuario> usuarios;
    private Context context;

    public static ArrayList<Usuario> listaParticipantes;

    public AdapterUsuariosInvitar(ArrayList<Usuario> users, Context c) {
        this.usuarios = users;
        listaParticipantes = new ArrayList<>();
        this.context = c;
    }

    @NonNull
    @Override
    public UsuarioInvitar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_usuario_invitar, null, false);
        return new UsuarioInvitar(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioInvitar holder, int position) {
        Usuario itemUser = usuarios.get(position);
        holder.bindData(itemUser);

        holder.send.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Guardar el nuevo estado en el objeto Item
                holder.send.setChecked(isChecked);

                // Realizar alguna acción adicional según sea necesario
                if (isChecked) {
                    listaParticipantes.add(itemUser);
                  //  if(itemUser.getUid().equals(Login.usuarioAutenticado.getUid())){
                  //      listaParticipantes.remove(itemUser);
                 //   }
                    System.out.println("se añadio a aaaaa" + itemUser.getNombreUsuario());
                    // El Switch está activado (true)
                } else {
                    listaParticipantes.remove(itemUser);
                    // El Switch está desactivado (false)
                    System.out.println("SE ELIMINO AAA " + itemUser.getNombreUsuario());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class UsuarioInvitar extends RecyclerView.ViewHolder {
        private TextView nombreUser;
        private CircleImageView imagen;
        Switch send;

        public UsuarioInvitar(@NonNull View itemView) {
            super(itemView);
            send = itemView.findViewById(R.id.switch1);
            nombreUser = itemView.findViewById(R.id.nombreUsuarioInvitar);
            imagen = itemView.findViewById(R.id.perfilUsuarioInvitar);
        }

        public void bindData(Usuario u) {
            nombreUser.setText(u.getNombreUsuario());
            new FirebaseCargaImg(u.getPerfil(), imagen);
        }
    }
}
