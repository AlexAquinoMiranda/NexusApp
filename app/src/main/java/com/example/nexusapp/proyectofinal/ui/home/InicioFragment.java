package com.example.nexusapp.proyectofinal.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.AdapterHistoria;
import com.example.nexusapp.proyectofinal.Adaptadores.AdapterPublicacion;
import com.example.nexusapp.proyectofinal.DTO.Historia;
import com.example.nexusapp.proyectofinal.DTO.Publicacion;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseController;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.Publicacion.SubirHistoria;
import com.example.nexusapp.R;
import com.example.nexusapp.databinding.InicioBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * InicioFragment es un fragment que contiene recycler de publicaciones y historias en donde se encarga de visualizarlas.
 */
public class InicioFragment extends Fragment {

    private InicioBinding binding;
    private View root;
    private ImageView addHistoria, addHistoriaPerfil;
    private RecyclerView recyclerView, recyclerHistorias;
    private DatabaseReference conversationRef;
    private FirebaseDatabase database;

    public static ArrayList<Publicacion> getListaMostrar() {
        return listaMostrar;
    }

    private static ArrayList<Publicacion> listaMostrar;
    public static ArrayList<Historia> listaHistorias;

    public static ArrayList<Publicacion> getPublicationes() {
        return publicationes;
    }

    private static ArrayList<Publicacion> publicationes;
    private InicioViewModel homeViewModel;
    private FirebaseController firebase;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(InicioViewModel.class);
        binding = InicioBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        ocultarNavegacion();

        inicializar();
        cargarDatos();
        eventos();
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
     * Eventos es un método en donde realiza acciones de botones
     * cuando estas se presionen
     */
    void eventos() {
        this.addHistoria.setOnClickListener(v -> {
            abrirVentana(SubirHistoria.class);
        });
        this.addHistoriaPerfil.setOnClickListener(v -> {
            abrirVentana(SubirHistoria.class);
        });

    }


    /**
     * Inicializar atributos.
     */
    void inicializar() {
        this.firebase = new FirebaseController("Historias", root.getContext());
        recyclerView = root.findViewById(R.id.recyclerPublication);
        addHistoria = root.findViewById(R.id.imageView2);
        addHistoriaPerfil = root.findViewById(R.id.imageViewPerfil);
        recyclerHistorias = root.findViewById(R.id.recyclerHistorias);
        listaMostrar = new ArrayList<>();
        publicationes = new ArrayList<>();
        listaHistorias = new ArrayList<>();

        recyclerView.setLayoutManager(
                new LinearLayoutManager(root.getContext()));
        recyclerHistorias.setLayoutManager(
                new LinearLayoutManager(root.getContext(),
                        LinearLayoutManager.HORIZONTAL, false));
        database = FirebaseDatabase.getInstance();
        conversationRef = database.getReference("Publicaciones");
        new FirebaseCargaImg(Login.usuarioAutenticado.getPerfil(), addHistoriaPerfil);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * CargarDatos es un método que se encarga de rellenar una lista de publicaciones de firebase
     */
    void cargarDatos() {

        conversationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InicioFragment.getListaMostrar().clear();
                InicioFragment.getPublicationes().clear();

                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    Publicacion userAux = element.getValue(Publicacion.class);
                    System.out.println(userAux.toString());
                    InicioFragment.getPublicationes().add(userAux);
                }


                AdapterPublicacion adapter = new AdapterPublicacion(obtenerDatosEnOrdenInvertido(InicioFragment.getPublicationes()), root.getContext(), new AdapterPublicacion.OnItemClickListener() {
                    @Override
                    public void onItemClick(Publicacion item) {

                    }
                });

                recyclerView.setAdapter(adapter);

                for (Publicacion p : InicioFragment.getPublicationes()) {
                    if (p.getIdUsuario() == Login.usuarioAutenticado.getUid()) {
                        System.out.println("son iguales el id ------------------\n" + p.toString());
                        InicioFragment.getListaMostrar().add(p);//tiene que estar en el perfil de usuario
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("------", "Failed to read value.", error.toException());
            }
        });
        cargarDatosHistoria();
    }

    /**
     * obtenerDatosEnOrdenInvertido es un metodo que coge y agrega los datos en orden invertido
     * al de un Array normal
     * @param datos
     * @return datos
     */
    public ArrayList<Publicacion> obtenerDatosEnOrdenInvertido(ArrayList<Publicacion> datos) {
        // Agregar los datos en el orden inverso al ArrayList
        Collections.reverse(datos); // Invertir el orden del ArrayList
        return datos;
    }

    /**
     * abrirVentana un método que recibe una clase para abrirla posteriormente.
     *
     * @param view
     */
    void abrirVentana(Class view) {
        Intent i = new Intent(root.getContext(), view);
        startActivity(i);
    }

    /**
     * cargarDatosHistoria meétodo que se encarga de rellenar una lista de historias  de firebase
     */
    void cargarDatosHistoria() {
        this.firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaHistorias.clear();
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    Historia historia = element.getValue(Historia.class);
                    System.out.println(historia.toString());
                    listaHistorias.add(historia);
                }
                AdapterHistoria adapter = new AdapterHistoria(listaHistorias, getContext());
                recyclerHistorias.setAdapter(adapter);

            }

            // listaHistorias

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("------", "Failed to read value.", error.toException());
            }
        });

    }
}