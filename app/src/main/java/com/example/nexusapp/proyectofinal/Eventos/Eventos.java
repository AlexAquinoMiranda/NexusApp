package com.example.nexusapp.proyectofinal.Eventos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nexusapp.proyectofinal.DTO.Evento;
import com.example.nexusapp.proyectofinal.Auxiliares.SubirImagen;
import com.example.nexusapp.proyectofinal.DTO.Evento;
import com.example.nexusapp.proyectofinal.DTO.EventoImagen;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.CargarImagenGlide;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseController;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudEventos;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Eventos extends AppCompatActivity {

    private TextView nombreEvento, creadorEvento, descripcion, nombreUsuarioFoto, countImagenes, noHayImagen;
    static ImageView portadaEvento;
    private ArrayList<Evento> listaEventos;
    private ProgressBar progressBarEvent;
    private RelativeLayout relativeFotos;
    private CircleImageView perfilUsuario;
    private FirebaseCrudEventos firebaseCrudEventos;
    private FirebaseController firebase;
    private FirebaseDatabase database;
    private FloatingActionButton volver, siguiente;
    private ActivityResultLauncher<String> pickImageLauncher;
    public static Uri pathImage;
    private ImageView crearNuevoEvento, imageViewsalir;
    public static Evento eventos;
    private ImageView imagenPrincipal, imagenSiguiente, imagenAnterior;

    private List<EventoImagen> listaDeEventos;

    public static int posicionActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventos);
        ocultarNavegacion();

        // eventos = (Evento) getIntent().getSerializableExtra("Evento");

        inicializar();
        listaDeEventos = eventos.getImagenes();
        System.out.println(eventos);
        nombreEvento.setText(eventos.getNombreEvento());
        creadorEvento.setText(eventos.getUsuarioCreador());
        descripcion.setText(eventos.getDescripcionEvento());
        cargarDatosFotos();

        for (Usuario u : eventos.getParticipantes()) {
            if (u.getUid().equals(Login.usuarioAutenticado.getUid())) {
                crearNuevoEvento.setVisibility(View.VISIBLE);
                break;
            } else {
                crearNuevoEvento.setVisibility(View.GONE);
            }
        }

        listener();


//        Bundle extras = getIntent().getExtras();
//        if(extras!=null){
//            textoUsuario = extras.getString("textoUsuario");
//        }
//        listaEventos = firebaseCrudEventos.getAll();
//
//        for( int i = 0; i<listaEventos.size();i++){
//
//            if(eventos.getUsuarioCreador() == textoUsuario) {
//
//                nombreEvento.setText(listaEventos.get(i).getNombreEvento());
//                creadorEvento.setText(listaEventos.get(i).getUsuarioCreador());
//                descripcion.setText(listaEventos.get(i).getDescripcionEvento());
//            }
//        }
    }

    void ocultarNavegacion() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * Metodo que detecta si se pulsa botón hacia Atras
     */
    @Override
    public void onBackPressed() {
        posicionActual = 0; // Inicializar la variable a 0
        System.out.println("Botón atrasssssss");
        super.onBackPressed();
    }

    /**
     * método en espera de onclick para realizar acciones
     */
    void listener() {

        this.crearNuevoEvento.setOnClickListener(V -> {
            pickImageLauncher.launch("image/*");
            //otra ventana para cargar Fotossssss......

        });
        if (listaDeEventos != null) {
            volver.setOnClickListener(c -> {
                progressBarEvent.setVisibility(View.VISIBLE);
                retrocederFoto();
                System.out.println("antess te e");

            });
            siguiente.setOnClickListener(b -> {
                progressBarEvent.setVisibility(View.VISIBLE);
                siguienteFoto();
                System.out.println("sigueitee te e");
            });
            System.out.println(eventos);
        }
        //cargar fotos.
        cargarDatosFotos();

        this.imageViewsalir.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    /**
     * Guardar nueva foto que se suba de un evento
     */
    private void guardarNuevaFoto() {
        if (pathImage != null) {
            String titulo = String.valueOf(pathImage).replace("/", "_").replace("//", "_").replace(".", "_").replace(":", "_").replace("%", "_");


            EventoImagen datosImagen = new EventoImagen();

            datosImagen.setImagenEvento(titulo);//rellenar la imagen que seleccione el usuario
            datosImagen.setUsuarioParticipante(Login.usuarioAutenticado);//se supone que es el autenticado el que va a subir
            List<EventoImagen> nuevoImagenes = new ArrayList<>();

            nuevoImagenes.add(datosImagen);
            nuevoImagenes.addAll(eventos.getImagenes());
            eventos.setImagenes(nuevoImagenes);
            new FirebaseCrudEventos(this).modify(eventos, eventos.getIdEvento());
            new SubirImagen(pathImage, titulo);
            listaDeEventos = eventos.getImagenes();
        }
    }

    /**
     * Método que carga la siguiente imagen en la central {lista dinámica}
     */
    void siguienteFoto() {
        if (posicionActual < this.listaDeEventos.size() - 1) {
            posicionActual++;
        } else {
            posicionActual = 0;//del final se ubica al principio
        }

        cargarDatosFotos();
        //cargarFoto
    }

    /**
     * Método que retrocede lista dinámica de fotos
     */
    void retrocederFoto() {
        if (posicionActual > 0) {
            posicionActual--;
        } else {
            posicionActual = listaDeEventos.size() - 1; //se ubica al final.
        }
        cargarDatosFotos();
    }

    /**
     * método que carga iamgenes de acuerdo a la lista.
     * {% size}
     * nos permite obtener un índice válido dentro del rango del ArrayList de eventos y mostrar su imagen,
     */
    void cargarDatosFotos() {
        if (!listaDeEventos.isEmpty() && listaDeEventos != null) {

            relativeFotos.setVisibility(View.VISIBLE);
            noHayImagen.setVisibility(View.GONE);
            System.out.println("esta llena la lisyaaaa");
            int size = listaDeEventos.size();

            String imagenAntes = listaDeEventos.get((posicionActual + size - 1) % size).getImagenEvento();
            String imagenActual = listaDeEventos.get(posicionActual).getImagenEvento();

            Usuario usuario = listaDeEventos.get(posicionActual).getUsuarioParticipante();

            String imagenSig = listaDeEventos.get((posicionActual + 1) % size).getImagenEvento();

            System.out.println(imagenActual + " la otrs \n " + imagenSig);

            new CargarImagenGlide(imagenActual, imagenPrincipal, progressBarEvent, getApplicationContext());
            new CargarImagenGlide(imagenAntes, imagenAnterior, progressBarEvent, getApplicationContext());
            new CargarImagenGlide(imagenSig, imagenSiguiente, progressBarEvent, getApplicationContext());
            new FirebaseCargaImg(usuario.getPerfil(), perfilUsuario);
            nombreUsuarioFoto.setText(usuario.getNombreUsuario());
            String contador = String.valueOf(posicionActual);
            String leng = String.valueOf(size - 1);

            countImagenes.setText(contador + "/" + leng);
        } else {
            relativeFotos.setVisibility(View.GONE);
            noHayImagen.setVisibility(View.VISIBLE);
        }
    }


    /**
     * inicializar atributos
     */
    void inicializar() {
        listaEventos = new ArrayList<>();
        progressBarEvent = findViewById(R.id.progressBarEvent);
        imageViewsalir = findViewById(R.id.imageViewsalir);

        noHayImagen = findViewById(R.id.noHayImagen);
        relativeFotos = findViewById(R.id.relativeFotos);
        crearNuevoEvento = findViewById(R.id.SubirFotoEvento);
        this.firebase = new FirebaseController("Eventos", this);
        database = FirebaseDatabase.getInstance();
        firebaseCrudEventos = new FirebaseCrudEventos(this);
        nombreEvento = findViewById(R.id.tituloEvento);
        creadorEvento = findViewById(R.id.creadorEvento);
        descripcion = findViewById(R.id.descripcionEvento);
        volver = findViewById(R.id.volverFoto);
        siguiente = findViewById(R.id.siguienteFoto);
        perfilUsuario = findViewById(R.id.perfilUsuarioEvento);
        countImagenes = findViewById(R.id.contadorImagens);
        nombreUsuarioFoto = findViewById(R.id.user_name_Evento);


        imagenSiguiente = findViewById(R.id.imagenSiguiente);
        imagenPrincipal = findViewById(R.id.imagePrincipal);
        imagenAnterior = findViewById(R.id.imagenAntes);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                // portadaEventos.setImageBitmap(bitmap);
                                pathImage = uri;
                                guardarNuevaFoto();
                            } catch (FileNotFoundException e) {
                            }

                        }
                    }
                });

    }

    public ArrayList<Evento> getAll() {
        cargarDatosEventos();

        return listaEventos;
    }

    /**
     * Carga los datos de eventos desde Firebase Database.
     * Utiliza un ValueEventListener para obtener los datos y los agrega a la lista de eventos.
     * Si ocurre un error durante la lectura de los datos, se registra un mensaje de error en el registro.
     */
    void cargarDatosEventos() {
        this.firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaEventos.clear();
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    Evento evento = element.getValue(Evento.class);
                    listaEventos.add(evento);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("------", "Failed to read value.", error.toException());
            }
        });
    }

}
