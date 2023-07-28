package com.example.nexusapp.proyectofinal.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.DTO.Notificacion;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudNotificacion;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterNotificaciones extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Notificacion> notificaciones;
    private static final int ENVIA = 1;
    private static final int RECIBE = 2;
    private Context con;

    private FirebaseCrudNotificacion crudNotificacion;

    public AdapterNotificaciones(ArrayList<Notificacion> lis, Context context) {
        this.notificaciones = lis;
        this.con = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ENVIA) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.re_notificacion_evento, parent, false);
            return new NotificacionMensaje(view);
        } else if (viewType == RECIBE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.res_notificacion, parent, false);
            return new NotificacionPublicacion(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Notificacion notificacion = notificaciones.get(position);
        System.out.println(notificacion.toString());
        crudNotificacion = new FirebaseCrudNotificacion(Login.usuarioAutenticado.getUid(), con);
        switch (holder.getItemViewType()) {
            case ENVIA:
                System.out.println("Desde envia -> " +notificacion.toString());
                NotificacionMensaje mensaje = (NotificacionMensaje) holder;
                mensaje.mensaje.setText(notificacion.getMensaje());
                mensaje.titulo.setText(notificacion.getTipo());
                mensaje.eliminar.setOnClickListener(v -> {
                    crudNotificacion.delete(notificacion.getUid());
                });
                break;
            case RECIBE:
                System.out.println("Desde recibe -> " +notificacion.toString());
                NotificacionPublicacion publicaciom = (NotificacionPublicacion) holder;
                publicaciom.mensaje.setText(notificacion.getMensaje());
                publicaciom.titulo.setText(notificacion.getTipo());
                publicaciom.eliminar.setOnClickListener(v -> {
                    crudNotificacion.delete(notificacion.getUid());
                });

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Notificacion notificacion = notificaciones.get(position);
        if (notificacion.getTipo().equals("Mensajes") ||  notificacion.getTipo().equals("Eventos")) {
            return ENVIA;
        } else {
            return RECIBE;
        }
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public class NotificacionMensaje extends RecyclerView.ViewHolder {
        TextView titulo, mensaje;
        CircleImageView imagen;
        ImageView eliminar;

        public NotificacionMensaje(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tituloNoti);
            mensaje = itemView.findViewById(R.id.notificacionEvento);
            eliminar = itemView.findViewById(R.id.imageView3);

        }
    }

    public class NotificacionPublicacion extends RecyclerView.ViewHolder {
        TextView titulo, mensaje;
        CircleImageView imagen;
        ImageView eliminar;

        public NotificacionPublicacion(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tituloNormal);
            mensaje = itemView.findViewById(R.id.notificationNormal);
            eliminar = itemView.findViewById(R.id.imageView4);

        }
    }


}
