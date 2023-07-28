package com.example.nexusapp.proyectofinal.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.DTO.Publicacion;
import com.example.nexusapp.proyectofinal.Firebase.CargarImagenGlide;
import com.example.nexusapp.proyectofinal.Comentario.Comentario;
import com.example.nexusapp.proyectofinal.DTO.Historia;
import com.example.nexusapp.proyectofinal.DTO.Notificacion;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.CargarImagenGlide;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudPublicacion;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.proyectofinal.Notificacion.Notificaciones;
import com.example.nexusapp.R;
import com.example.nexusapp.proyectofinal.ui.PerfilUsuarios.PerfilDeUsuario;
import com.example.nexusapp.proyectofinal.ui.home.InicioFragment;
import com.example.nexusapp.proyectofinal.ui.perfil.PerfilFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPublicacion extends RecyclerView.Adapter<AdapterPublicacion.publication> {
    private ArrayList<Publicacion> lisPublicaciones;
    private Context contexto;

    private DatabaseReference conversationRef;
    private FirebaseDatabase database;

    public static boolean volverAtras = false;
    public static boolean botonAtrasPresionado = false;
    OnItemClickListener listener;

    public AdapterPublicacion(ArrayList<Publicacion> lista, Context con, OnItemClickListener listener) {
        this.lisPublicaciones = lista;
        this.contexto = con;
        database = FirebaseDatabase.getInstance();
        this.listener = listener;
        conversationRef = database.getReference("Publicaciones");
        System.out.println("entrando en adapter publiiiiii");
    }

    public interface OnItemClickListener {

        void onItemClick(Publicacion item);


    }

    @NonNull
    @Override
    public publication onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_publicacion, null, false);
        return new publication(view);
    }

    @Override
    public void onBindViewHolder(@NonNull publication holder, int position) {
        int value = position;

        Publicacion publicacion = lisPublicaciones.get(value);
        holder.description.setText(publicacion.getDescripcion());
        holder.progressBar2.setVisibility(View.VISIBLE);
        Comentario.publicacion = publicacion;
        if (publicacion.getIdUsuario().equals(Login.usuarioAutenticado.getUid())) {
            holder.btnEliminarPublicacion.setVisibility(View.VISIBLE);

            holder.btnEliminarPublicacion.setOnClickListener(v -> {

                new FirebaseCrudPublicacion(contexto).delete(publicacion.getUid());
            });

        } else {
            holder.btnEliminarPublicacion.setVisibility(View.GONE);
        }

        if (volverAtras != false) {
            holder.volverPerfil.setVisibility(View.VISIBLE);
        } else {
            holder.volverPerfil.setVisibility(View.GONE);
        }


        holder.volverPerfil.setOnClickListener(v -> {
            botonAtrasPresionado = true;
            System.out.println("vilver atraassssssss desde lambdad adkajsd");
        });
        System.out.println(" desde sdarpertr " + publicacion.toString());

        holder.userName.setText(publicacion.getUserName());


        holder.countLike.setText(" A " + publicacion.getLike() + " personas le gusta esta foto.");
        //tengo que ver como cargo la imagen.
        new CargarImagenGlide(publicacion.getUserImage(), holder.imgUser, contexto);

        new CargarImagenGlide(publicacion.getUrlImagen(), holder.imgPublication, holder.progressBar2, contexto);

        holder.imgUser.setOnClickListener(n -> {
            comprobarHistoriaDeUsuario(n, publicacion);
        });
        for (Usuario con : Login.getUsers()) {
            if (con.getUid().equals(lisPublicaciones.get(value).getIdUsuario())) {
                PerfilDeUsuario.perfilUsuarioVisible = con;

            }
        }
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerfilFragment.idUsuario = lisPublicaciones.get(value).getIdUsuario();
                if (lisPublicaciones.get(value).getIdUsuario().equals(Login.usuarioAutenticado.getUid())) {

                    //Abrir PerfilFragment.class);
                    MainActivity.navController.navigate(R.id.navegacionPerfil);

                    System.out.println("mi perfiñlllll");
                } else {
                    MainActivity.navController.navigate(R.id.perfilDeUsuariosF);
                    //abrir PerfilDeUsuario.class);

                    System.out.println("perfil de otrooooo");

                }


            }
        });
        if (publicacion.getDatosLike().get(Login.usuarioAutenticado.getUid()) != null) {
            if (publicacion.getDatosLike().get(Login.usuarioAutenticado.getUid())) {
                holder.btnLike.setImageResource(R.drawable.like10);
            } else {
                holder.btnLike.setImageResource(R.drawable.dislike10);
            }


            //Notificaciones.comprobarLike();
            holder.btnLike.setOnClickListener(n -> {

                if (publicacion.getDatosLike().get(Login.usuarioAutenticado.getUid())) {
                    publicacion.setLike(publicacion.getLike() - 1);
                    //    notifyItemChanged(value);
                    publicacion.getDatosLike().put(Login.usuarioAutenticado.getUid(), false);
                    holder.btnLike.setImageResource(R.drawable.dislike10);
                } else {
                    publicacion.setLike(publicacion.getLike() + 1);
                    publicacion.getDatosLike().put(Login.usuarioAutenticado.getUid(), true);
                    holder.btnLike.setImageResource(R.drawable.like10);

                    Notificacion notificacion = new Notificacion();
                    notificacion.setUid(UUID.randomUUID().toString());
                    notificacion.setMensaje(publicacion.getUserName() + " recibiste un like de " + Login.usuarioAutenticado.getNombreUsuario() + " y " + publicacion.getLike() + " personas más en tu publicación");
                    notificacion.setTipo("publicación");

                    //  notifyItemChanged(value);
                    if(!publicacion.getIdUsuario().equals(Login.usuarioAutenticado.getUid())){
                        Notificaciones.notificar(notificacion, lisPublicaciones.get(value).getIdUsuario());
                    }

                }
                conversationRef.child(publicacion.getUid()).setValue(publicacion);
                holder.countLike.setText(" A " + publicacion.getLike() + " personas le gusta esta publicación.");

            });
        } else {
            publicacion.addDatosLike(Login.usuarioAutenticado.getUid(), false);
            conversationRef.child(publicacion.getUid()).setValue(publicacion);
        }

        holder.bindData(publicacion);

        holder.btnComentario.setOnClickListener(b -> {
            MainActivity.navController.navigate(R.id.coment);
            //abro ventana
            Comentario.publicacion =  publicacion;

        });

    }

    /**
     * mostrar btn de volver atrás
     */
    public void hacerVisibleBotonAtras() {
        this.volverAtras = true;
    }

    /**
     * metodo que comprueba si el boton ha sido pulsado
     *
     * @return true si se presionó. false en otro caso
     */
    public boolean volverAtrasOnClick() {
        System.out.println("vilver atraassssssss");
        return botonAtrasPresionado;
    }

    /**
     * ocultar opcion de volver atras
     */
    public void hacerInisibleBotonAtras() {
        this.volverAtras = false;
    }


    @Override
    public int getItemCount() {
        return this.lisPublicaciones.size();
    }

    /**
     * Método que comprueba si un usuario tiene historia para mostrarla.
     *
     * @param v v es la vista para iniciar la visualización de la historia
     */

    private void comprobarHistoriaDeUsuario(View v, Publicacion publicacion) {
        if (!InicioFragment.listaHistorias.isEmpty() && InicioFragment.listaHistorias != null) {
            for (Historia h : InicioFragment.listaHistorias) {
                if (h.getIdUsuario().equals(publicacion.getUid())) {

                    com.example.nexusapp.proyectofinal.Historia.Historia.historiaVisualizar = h;
                    Intent i = new Intent(v.getContext(), com.example.nexusapp.proyectofinal.Historia.Historia.class);
                    v.getContext().startActivity(i);
                    break;
                }
            }
        }
    }

    public class publication extends RecyclerView.ViewHolder {
        TextView userName;
        TextView countLike, description;
        ImageView imgPublication;
        CircleImageView imgUser;
        ImageView btnLike, volverPerfil;
        ImageView btnComentario, btnEliminarPublicacion;
        ProgressBar progressBar2;


        public publication(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_publicacion);
            imgPublication = itemView.findViewById(R.id.publicacionImage);
            imgUser = itemView.findViewById(R.id.perfil_publicacion);
            btnLike = itemView.findViewById(R.id.imagenLike);
            countLike = itemView.findViewById(R.id.cantidad_likes);
            btnComentario = itemView.findViewById(R.id.imagenComentario);
            btnEliminarPublicacion = itemView.findViewById(R.id.btnEliminarPublicacion);
            description = itemView.findViewById(R.id.descripcion_publicacion);
            volverPerfil = itemView.findViewById(R.id.volverAtraPublicacion);
            progressBar2 = itemView.findViewById(R.id.progressBar2);

        }

        public void bindData(Publicacion publicacion) {
            volverPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(publicacion);
                }
            });

        }
    }
}