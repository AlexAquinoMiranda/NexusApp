package com.example.nexusapp.proyectofinal.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.AdapterImagenPublicacion;
import com.example.nexusapp.proyectofinal.Adaptadores.AdapterPublicacion;
import com.example.nexusapp.proyectofinal.Configuracion.Ajustes;
import com.example.nexusapp.proyectofinal.Configuracion.Configuracion;
import com.example.nexusapp.proyectofinal.DTO.Historia;
import com.example.nexusapp.proyectofinal.DTO.Publicacion;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.R;
import com.example.nexusapp.databinding.PerfilBinding;
import com.example.nexusapp.proyectofinal.ui.home.InicioFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * PerfilFragment es un fragment que visualiza los datos del usuario autenticado.
 */
public class PerfilFragment extends Fragment {
    private TextView usuario, descripcion, publicaciones, seguidores, siguiendo;
    private CircleImageView imagen;
    private AdapterImagenPublicacion adapterImagenPublicacion = null;
    private PerfilBinding binding;
    private RecyclerView recyclerView;
    private Button btnEditar;
    AdapterPublicacion adapterNuevo;

    private ImageView btnAjustes;
    private View root;
    public static String idUsuario;
    private DatabaseReference conversationRef;
    private FirebaseDatabase database;
    public static ArrayList<Publicacion> misPublicaciones;

    AdapterPublicacion adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PerfilViewModel notificationsViewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = PerfilBinding.inflate(inflater, container, false);
        root = binding.getRoot();
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

    /**
     * abrirVentana un método que recibe una clase para abrirla posteriormente.
     *
     * @param view view es la ventana que se va a abrir
     */
    void abrirVentana(Class view) {
        Intent i = new Intent(root.getContext(), view);
        startActivity(i);
    }

    /**
     * listeners es un método en donde realiza acciones de botones
     * cuando estas se presionen
     */
    void listeners() {

        imagen.setOnClickListener(v -> {

            comprobarHistoriaDeUsuario(v);

            //todo si tiene historia que la muestre
        });
        btnEditar.setOnClickListener(v -> {
            abrirVentana(Configuracion.class);
        });
        btnAjustes.setOnClickListener(n -> {
            abrirVentana(Ajustes.class);
        });

        this.recyclerView.setOnClickListener(v -> {
            this.recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 1));
            this.recyclerView.setAdapter(adapter);
            //  showSingleImage(position);
            System.out.println("se ha presionado el recyclerrrrrrr");
        });
    }

    /**
     * Método que comprueba si un usuario tiene historia para mostrarla.
     * @param v v es la vista para iniciar la visualización de la historia
     */
    private void comprobarHistoriaDeUsuario(View v) {
        if (!InicioFragment.listaHistorias.isEmpty() && InicioFragment.listaHistorias != null) {
            for (Historia h : InicioFragment.listaHistorias) {
                if (h.getIdUsuario().equals(Login.usuarioAutenticado.getUid())) {

                    com.example.nexusapp.proyectofinal.Historia.Historia.historiaVisualizar = h;
                    Intent i = new Intent(v.getContext(), com.example.nexusapp.proyectofinal.Historia.Historia.class);
                    v.getContext().startActivity(i);
                    break;
                }
            }
        }
    }

    /**
     * Inicializar atributos
     */
    void inicializar() {
        recyclerView = root.findViewById(R.id.recyclerMisPublicaciones);
        this.usuario = root.findViewById(R.id.nombrePerfil);
        descripcion = root.findViewById(R.id.descripcion);
        publicaciones = root.findViewById(R.id.publicacionesPerfil);
        seguidores = root.findViewById(R.id.seguidores);
        siguiendo = root.findViewById(R.id.siguiendoPefil);
        imagen = root.findViewById(R.id.imagenPerfil);
        btnEditar = root.findViewById(R.id.botonEditar);
        btnAjustes = root.findViewById(R.id.configuracion);
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
        database = FirebaseDatabase.getInstance();
        conversationRef = database.getReference("Publicaciones");
    }

    /**
     * RellenarDatos es un método que obtiene los datos del usuario
     * autenticado para visualizarlo.
     */
    void rellenarDatos() {
        misPublicaciones = new ArrayList<>();
        this.usuario.setText(Login.usuarioAutenticado.getNombre() + " - " + Login.usuarioAutenticado.getNombreUsuario());
        this.descripcion.setText(Login.usuarioAutenticado.getDescripcion());
        new FirebaseCargaImg(Login.usuarioAutenticado.getPerfil(), imagen);

        int count = 0;
        for (Boolean value : Login.usuarioAutenticado.getSeguidores().values()) {
            if (value) { // Verificar si el valor es true
                count++;
            }
        }
        this.seguidores.setText(String.valueOf(count));

        this.siguiendo.setText(String.valueOf(Login.usuarioAutenticado.getSiguiendo()));
        this.publicaciones.setText(String.valueOf(misPublicaciones.size()));

        mostraPublicaciones();
    }

    /**
     * Metodo que rellena una lista de publicaciones del usuario autenticado para luego mostrarlas.
     */
    void mostraPublicaciones() {
        conversationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                misPublicaciones.clear();

                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    Publicacion userAux = element.getValue(Publicacion.class);
                    System.out.println(userAux.toString());

                    if (userAux.getIdUsuario().equals(Login.usuarioAutenticado.getUid())) {
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
                // adapter = new AdapterPublicacion(misPublicaciones, root.getContext());
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
     * Metodo que muestra info de publicación.
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
                System.out.println("vilver atraassssssss");
            }
        });
        adapterNuevo.hacerVisibleBotonAtras();

        recyclerView.setAdapter(adapterNuevo);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}