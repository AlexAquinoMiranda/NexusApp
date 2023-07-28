package com.example.nexusapp.proyectofinal.ui.PerfilUsuarios;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.AdapterImagenPublicacion;
import com.example.nexusapp.proyectofinal.Adaptadores.AdapterPublicacion;
import com.example.nexusapp.proyectofinal.Auxiliares.AuxiliarChat;
import com.example.nexusapp.proyectofinal.DTO.Historia;
import com.example.nexusapp.proyectofinal.DTO.Notificacion;
import com.example.nexusapp.proyectofinal.DTO.Publicacion;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.Mensajeria.Mensajeria;
import com.example.nexusapp.proyectofinal.Notificacion.Notificaciones;
import com.example.nexusapp.R;
import com.example.nexusapp.proyectofinal.ui.home.InicioFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * PerfilDeUsuario es un fragment que visualiza el perfil de los demas usuarios
 */
public class PerfilDeUsuario extends Fragment {
    private TextView usuario, descripcion, publicaciones, seguidores, siguiendo;
    private CircleImageView imagen;
    AdapterPublicacion adapterNuevo;
    AdapterPublicacion adapter;
    AdapterImagenPublicacion adapterImagenPublicacion;
    private static ArrayList<Publicacion> misPublicaciones;
    private RecyclerView recyclerView;
    private Button btnSeguir;
    private Button btnMensaje;
    private View root;
    private PerfilDeUsuarioViewModel mViewModel;
    public static Usuario perfilUsuarioVisible;
    private DatabaseReference conversationRef;
    private FirebaseDatabase database;

    public static PerfilDeUsuario newInstance() {
        return new PerfilDeUsuario();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_perfil_de_usuario, container, false);
        ocultarNavegacion();

        inicializar();
        rellenarDatos();
        listeners();
        return root;
    }

    void ocultarNavegacion(){
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inicializar();
        rellenarDatos();
        listeners();
        mViewModel = new ViewModelProvider(this).get(PerfilDeUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

    /**
     * listeners es un método en donde realiza acciones de botones
     * cuando estas se presionen
     */
    void listeners() {
        Map<String, Boolean> seguidor = perfilUsuarioVisible.getSeguidores();
        conversationRef = database.getReference("Usuarios");
        boolean seguir = false;
        if (!seguidor.isEmpty() && seguidor.get(Login.usuarioAutenticado.getUid()) != null) {
            seguir = seguidor.get(Login.usuarioAutenticado.getUid());

            if (seguir == false) {
                btnSeguir.setVisibility(View.VISIBLE);
                btnSeguir.setText("Seguir");
            } else {
                btnSeguir.setText("Dejar de seguir");
                //  btnSeguir.setVisibility(View.INVISIBLE);
            }
        } else if (seguidor != null) {
            seguidor.put(Login.usuarioAutenticado.getUid(), false);
            btnSeguir.setVisibility(View.VISIBLE);
        }


        AtomicBoolean finalSeguir = new AtomicBoolean(seguir);
        btnSeguir.setOnClickListener(v -> {
            if (finalSeguir.get()) {
                finalSeguir.set(false);
                Login.usuarioAutenticado.setSiguiendo(Login.usuarioAutenticado.getSiguiendo() - 1);
                System.out.println("os habéis dejado de  a seguir...");
                seguidor.put(Login.usuarioAutenticado.getUid(), false);
                btnSeguir.setText("Seguir");
            } else {
                Login.usuarioAutenticado.setSiguiendo(Login.usuarioAutenticado.getSiguiendo() + 1);
                //ahora es true
                finalSeguir.set(true);
                btnSeguir.setText("dejar de seguir");
                seguidor.put(Login.usuarioAutenticado.getUid(), true);


                Notificacion notificacion = new Notificacion();
                notificacion.setUid(UUID.randomUUID().toString());
                notificacion.setMensaje("El usuario " + Login.usuarioAutenticado.getNombreUsuario() + " ha comenzado a seguirte.");
                notificacion.setTipo("Seguidores");
                Notificaciones.notificar(notificacion, PerfilDeUsuario.perfilUsuarioVisible.getUid());


                // btnSeguir.setVisibility(View.INVISIBLE);

            }
            perfilUsuarioVisible.setSeguidores(seguidor);

            conversationRef.child(perfilUsuarioVisible.getUid()).setValue(perfilUsuarioVisible);
            conversationRef.child(Login.usuarioAutenticado.getUid()).setValue(Login.usuarioAutenticado);
        });
        btnMensaje.setOnClickListener(n -> {
            if (perfilUsuarioVisible != null) {
                AuxiliarChat a = new AuxiliarChat(perfilUsuarioVisible);
                System.out.println("abrir chatttttttt");
                Intent i = new Intent(root.getContext(), Mensajeria.class);
                n.getContext().startActivity(i);
            }
        });

        this.recyclerView.setOnClickListener(v -> {
            this.recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 1));
            this.recyclerView.setAdapter(adapter);
            //  showSingleImage(position);
            System.out.println("se ha presionado el recyclerrrrrrr");
        });
        imagen.setOnClickListener(v -> {

            comprobarHistoriaDeUsuario(v);


        });
    }

    /**
     * inicializar atributos
     */
    void inicializar() {
        recyclerView = root.findViewById(R.id.recyclerPublicacionesUsuarios);
        this.usuario = root.findViewById(R.id.nombrePerfilUsuario);
        descripcion = root.findViewById(R.id.descripcionUsuario);
        publicaciones = root.findViewById(R.id.publicacionesPerfilUsuario);
        seguidores = root.findViewById(R.id.seguidoresUsuario);
        siguiendo = root.findViewById(R.id.siguiendoUsuario);
        imagen = root.findViewById(R.id.imagenPerfilDeUsuario);
        btnSeguir = root.findViewById(R.id.seguir);
        btnMensaje = root.findViewById(R.id.mensaje);
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
        database = FirebaseDatabase.getInstance();
        conversationRef = database.getReference("Publicaciones");

        /** for (Usuario u : Login.getUsers()) {
         perfilUsuarioVisible.addSeguidor(u.getUid(), false);
         }*/
    }

    /**
     * Método que comprueba si un usuario tiene historia para mostrarla.
     *
     * @param v v es la vista para iniciar la visualización de la historia
     */

    private void comprobarHistoriaDeUsuario(View v) {
        if (!InicioFragment.listaHistorias.isEmpty() && InicioFragment.listaHistorias != null) {
            for (Historia h : InicioFragment.listaHistorias) {
                if (h.getIdUsuario().equals(PerfilDeUsuario.perfilUsuarioVisible.getUid())) {

                    com.example.nexusapp.proyectofinal.Historia.Historia.historiaVisualizar = h;
                    Intent i = new Intent(v.getContext(), com.example.nexusapp.proyectofinal.Historia.Historia.class);
                    v.getContext().startActivity(i);
                    break;
                }
            }
        }
    }

    /**
     * RellenarDatos es un método que obtiene los datos del usuario
     * para visualizarlo.
     */
    void rellenarDatos() {
        misPublicaciones = new ArrayList<>();
        this.usuario.setText(PerfilDeUsuario.perfilUsuarioVisible.getNombreUsuario());
        this.descripcion.setText(PerfilDeUsuario.perfilUsuarioVisible.getDescripcion());
        new FirebaseCargaImg(PerfilDeUsuario.perfilUsuarioVisible.getPerfil(), imagen);
        int count = 0;
        for (Boolean value : PerfilDeUsuario.perfilUsuarioVisible.getSeguidores().values()) {
            if (value) { // Verificar si el valor es true
                count++;
            }
        }
        this.seguidores.setText(String.valueOf(count));

        //   this.seguidores.setText(String.valueOf(PerfilDeUsuario.perfilUsuarioVisible.getSeguidores().size()));
        this.publicaciones.setText(String.valueOf(misPublicaciones.size()));
        this.siguiendo.setText(String.valueOf(PerfilDeUsuario.perfilUsuarioVisible.getSiguiendo()));
        mostraPublicaciones();
    }

    /**
     * metodo que rellena una lista de publicaciones del usuario a consultar para luego mostrarlas.
     */
    void mostraPublicaciones() {
        conversationRef = database.getReference("Publicaciones");
        conversationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                misPublicaciones.clear();

                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    Publicacion userAux = element.getValue(Publicacion.class);
                    System.out.println(userAux.toString());

                    if (userAux.getIdUsuario().equals(PerfilDeUsuario.perfilUsuarioVisible.getUid())) {
                        misPublicaciones.add(userAux);
                    }

                }
                adapterImagenPublicacion = new AdapterImagenPublicacion(misPublicaciones, root.getContext(), new AdapterImagenPublicacion.OnItemClickListener() {
                    @Override
                    public void onItemClick(Publicacion item) {
                        //mostrar Publicación
                        mostrarPublicacion(item);
                    }
                });

                adapter = new AdapterPublicacion(misPublicaciones, root.getContext(), new AdapterPublicacion.OnItemClickListener() {
                    @Override
                    public void onItemClick(Publicacion item) {
                        //volver atrás
                    }
                });
                recyclerView.setAdapter(adapterImagenPublicacion);
                publicaciones.setText(String.valueOf(misPublicaciones.size()));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("------", "Failed to read value.", error.toException());
            }
        });

    }

    /**
     * metodo que muestra info de publicación.
     *
     * @param image
     */
    private void mostrarPublicacion(Publicacion image) {

        // Configurar el RecyclerView para mostrar solo la imagen seleccionada
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 1));

        ArrayList<Publicacion> list = new ArrayList<>();
        list.add(image);
        adapterNuevo = new AdapterPublicacion(list, root.getContext(), new AdapterPublicacion.OnItemClickListener() {
            @Override
            public void onItemClick(Publicacion item) {
                recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
                recyclerView.setAdapter(adapterImagenPublicacion);
                adapterNuevo.hacerInisibleBotonAtras();

            }
        });

        recyclerView.setAdapter(adapterNuevo);
    }

}