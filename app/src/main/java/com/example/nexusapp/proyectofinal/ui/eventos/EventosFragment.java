package com.example.nexusapp.proyectofinal.ui.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.EventosAdapter;
import com.example.nexusapp.proyectofinal.DTO.Evento;
import com.example.nexusapp.proyectofinal.Eventos.CrearEvento;
import com.example.nexusapp.proyectofinal.Eventos.Eventos;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseController;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudEventos;
import com.example.nexusapp.R;
import com.example.nexusapp.databinding.FragmentEventosBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventosFragment extends Fragment {

    private FragmentEventosBinding binding;
    private EventosViewModel eventosViewModel;
    private RecyclerView recyclerEventos;
    private View root;
   public static ArrayList<Evento> listaEventos;
    private FirebaseDatabase database;
    private ImageView crearEvento;
    private FirebaseController firebase;
    private GridLayout layoutManager;

    private FirebaseCrudEventos firebaseCrudEventos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        eventosViewModel = new ViewModelProvider(this).get(EventosViewModel.class);
        binding = FragmentEventosBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        ocultarNavegacion();

        inicializar();
        mostrarEventos();
        listeners();

        recyclerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                abrirVentana(Eventos.class);
            }
        });

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
     * listener es un método en espera de onclick para realizar acciones
     */
    void listeners() {

        this.crearEvento.setOnClickListener(v -> {
            abrirVentana(CrearEvento.class);
        });

    }

    /**
     * abrirVentana un método que recibe una clase para abrirla posteriormente.
     *
     * @param view view a visualizar
     */
    void abrirVentana(Class view) {
        Intent i = new Intent(root.getContext(), view);
        startActivity(i);
    }

    /**
     * inicializar atributos
     */
    void inicializar() {
        listaEventos = new ArrayList<>();
        this.firebase = new FirebaseController("Eventos", root.getContext());
        crearEvento = root.findViewById(R.id.crearNuevoEvento);
        recyclerEventos = root.findViewById(R.id.recyclerEventos);
        database = FirebaseDatabase.getInstance();
        firebaseCrudEventos = new FirebaseCrudEventos(root.getContext());
        layoutManager = new GridLayout(root.getContext());

        Eventos.posicionActual = 0;


    }

    /**
     * MostrarEventos metodo que carga datos de firebase en una lista y
     * las visualiza en RecyclerView desde un adapter
     */
    void mostrarEventos() {

        cargarDatosEventos();
    }

    /**
     * cargarDatosEventos metodo que carga datos de firebase en una lista y
     * las visualiza en RecyclerView desde un adapter
     */
    void cargarDatosEventos() {
        this.firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaEventos.clear();
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    Evento evento = element.getValue(Evento.class);
                    System.out.println(evento.toString());
                    listaEventos.add(evento);
                }
                System.out.println(listaEventos);

                EventosAdapter miAdaptador = new EventosAdapter(listaEventos, root.getContext(), new EventosAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Evento item) {
                        Eventos.eventos = item;
                        moveToDescription(item);
                    }
                });
                recyclerEventos.setHasFixedSize(true);
                //Modifica el layoutManager de acuerdo al Recycler
                recyclerEventos.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
                //Modifico el adapter del recycler con mi clase Adaptador para que detecte la configuracion
                //de mi recycler
                recyclerEventos.setAdapter(miAdaptador);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("------", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Navega a la descripción de un evento.
     * @param evento El evento al que se desea acceder a la descripción.
     */
    public void moveToDescription(Evento evento) {
        Intent intent = new Intent(root.getContext(), Eventos.class);
        //    intent.putExtra("Evento", evento);
        startActivity(intent);
    }

}
