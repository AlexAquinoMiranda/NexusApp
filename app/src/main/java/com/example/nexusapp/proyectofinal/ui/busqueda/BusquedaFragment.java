package com.example.nexusapp.proyectofinal.ui.busqueda;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.AdapterBusqueda;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.R;
import com.example.nexusapp.databinding.*;

import java.util.ArrayList;

/**
 * BusquedaFragment es un fragment en donde se realiza busqueda de usuarios en forma de lista y acceder al perfil.
 */
public class BusquedaFragment extends Fragment {

    private FragmentBuscarBinding binding;
    private BusquedaViewModel busquedaViewModel;
    private View root;
    private EditText busqueda;
    private RecyclerView ressultados;
    private ImageView botonBuscar, btnSalir;
    private AdapterBusqueda adapter;

    ArrayList<Usuario> resultadosExactos = new ArrayList<>();
    ArrayList<Usuario> resultadosSimilares = new ArrayList<>();
    ArrayList<Usuario> resultadoFinal = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        busquedaViewModel = new ViewModelProvider(this).get(BusquedaViewModel.class);
        binding = FragmentBuscarBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        ocultarNavegacion();
        inicializar();
        listeners();
        return root;
    }

    void ocultarNavegacion() {
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * inicializar atributos
     */
    void inicializar() {
        this.busqueda = root.findViewById(R.id.busqueda);
        this.ressultados = root.findViewById(R.id.resultadoBusqueda);
        this.botonBuscar = root.findViewById(R.id.botonBuscar);
        //this.btnSalir = root.findViewById(R.id.botonVolverHome);

        ressultados.setLayoutManager(
                new LinearLayoutManager(root.getContext()));
    }

    /**
     * método en espera de eventos para realizar acciones
     */
    void listeners() {
        /*this.btnSalir.setOnClickListener(v -> {
            Intent i = new Intent(root.getContext(), MainActivity.class);
            startActivity(i);
        });*/
        this.botonBuscar.setOnClickListener(v -> {
            String texto = busqueda.getText().toString();

            this.resultadosExactos.clear();
            this.resultadosSimilares.clear();
            this.resultadoFinal.clear();

            if (TextUtils.isEmpty(texto)) {
                Toast.makeText(root.getContext(), "Introduce un datos para realizar la busqueda.", Toast.LENGTH_SHORT).show();
            } else {
                //buscar
                buscarUsuario(texto);
            }

        });
    }

    /**
     * Método que realiza busqueda en la lista de usuarios, texto completo o si el nombre contiene ese texto introducido
     *
     * @param busqueda texto a buscar
     */
    void buscarUsuario(String busqueda) {
        for (Usuario usuario : Login.getUsers()) {
            if (usuario.getNombreUsuario().equalsIgnoreCase(busqueda)) {
                System.out.println("nombre de usuario encontrado -> " + busqueda);
                resultadosExactos.add(usuario);
            } else if (usuario.getNombreUsuario().toLowerCase().contains(busqueda)) {
                System.out.println("resultados similares");
                resultadosSimilares.add(usuario);
            }
        }
        resultadoFinal.addAll(0, resultadosExactos);

        resultadoFinal.addAll(resultadosSimilares);
        if (!resultadoFinal.isEmpty()) {
            System.out.println("imprimiendo resultados");
            for (Usuario resultado : resultadoFinal) {
                System.out.println(resultado.toString());
                if (resultado.getNombre().equals(Login.usuarioAutenticado.getNombre()) && resultado.getNombreUsuario().equals(Login.usuarioAutenticado.getNombreUsuario())) {
                    resultadoFinal.remove(resultado);
                }
            }
        }

        if (resultadoFinal.isEmpty()) {
            Toast.makeText(root.getContext(), "No se encontraron resultados.", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new AdapterBusqueda(resultadoFinal, root.getContext());
            this.ressultados.setAdapter(adapter);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
