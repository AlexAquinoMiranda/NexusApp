package com.example.nexusapp.proyectofinal.Comentario;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.AdapterComentario;
import com.example.nexusapp.proyectofinal.DTO.Comentar;
import com.example.nexusapp.proyectofinal.DTO.Publicacion;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseController;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Comentario es un fragment que contiene un Reccyleriew en donde visualiza
 * todos los comentarios que haya en una publicación y entrada de texto para
 * introducir comentario
 */
public class Comentario extends Fragment {

    private EditText comentarioNuevo;
    private TextView nombreUsuarioPublicacion, descripción;
    private CircleImageView perfilUsuario;
    private CircleImageView exit;
    private Button enviarComentario;
    private RecyclerView recyclerView;
    private FirebaseController firebase;
    View root;
    private DatabaseReference conversationRef;
    private FirebaseDatabase database;
    public static Map<String, ArrayList<Comentar>> comentarios;
    public static Publicacion publicacion;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Comentar> comentars;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AdapterComentario com;

    public Comentario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Comentario.
     */
    // TODO: Rename and change types and number of parameters
    public static Comentario newInstance(String param1, String param2) {
        Comentario fragment = new Comentario();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Inicializar atributos
     */
    void inicializar() {
        this.comentarioNuevo = root.findViewById(R.id.textComentarioNuevo);
        this.enviarComentario = root.findViewById(R.id.btnSendComentario);
        this.recyclerView = root.findViewById(R.id.recyclerViewComentario);
        this.nombreUsuarioPublicacion = root.findViewById(R.id.user_name_publicacionFragment);
        this.descripción = root.findViewById(R.id.desc);
        this.exit = root.findViewById(R.id.exit);
        perfilUsuario = root.findViewById(R.id.perfil_publicacionCom);
        database = FirebaseDatabase.getInstance();
        conversationRef = database.getReference("Publicaciones");
        comentars = publicacion.getComentarios();

        for (Comentar i : comentars) {
            System.out.println(i.toString());
        }

        com = new AdapterComentario(comentars, root.getContext());
        recyclerView.setLayoutManager(
                new LinearLayoutManager(root.getContext()));
    }

    /**
     * Cargar datos es un métodoo que rellena la vista de los datos de la publicación a visualizar
     */
    void cargarDatos() {
        this.nombreUsuarioPublicacion.setText(publicacion.getUserName());
        this.descripción.setText(publicacion.getDescripcion());
        new FirebaseCargaImg(publicacion.getUserImage(), perfilUsuario);
        recyclerView.setAdapter(com);
    }

    /**
     * Listeners, método en espera de escucha de onClick para realizan acciones
     */
    void listeners() {
        exit.setOnClickListener(v -> {
            Intent i = new Intent(root.getContext(), MainActivity.class);
            startActivity(i);

        });
        this.enviarComentario.setOnClickListener(v -> {
            String texto = comentarioNuevo.getText().toString();

            if (TextUtils.isEmpty(texto)) {
                Toast.makeText(root.getContext(), "Introduce un texto.", Toast.LENGTH_SHORT).show();
            } else {
                comentar(texto);
                comentarioNuevo.setText("");
            }

        });

    }

    /**
     * comentar es un metodo que crea un objeto de tipo comentario y lo guarda en firebase
     * @param v v es el comentario que se va a guardar en el objeto
     */
    void comentar(String v) {

        comentars = publicacion.getComentarios();

        Comentar comentar = new Comentar();
        comentar.setComentario(v);
        comentar.setUuid(UUID.randomUUID().toString());
        comentar.setNombreUsuario(Login.usuarioAutenticado.getNombreUsuario());
        comentar.setPerfilUsuario(Login.usuarioAutenticado.getPerfil());

        comentars.add(comentar);
        publicacion.setComentarios(comentars);
        conversationRef.child(publicacion.getUid()).setValue(publicacion);
        com = new AdapterComentario(comentars, root.getContext());

        recyclerView.setAdapter(com);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_comentario, container, false);
        ocultarNavegacion();
        inicializar();
        listeners();
        cargarDatos();
        return root;
    }
    void ocultarNavegacion(){
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}