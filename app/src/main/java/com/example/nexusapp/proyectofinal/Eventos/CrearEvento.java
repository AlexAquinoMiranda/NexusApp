package com.example.nexusapp.proyectofinal.Eventos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nexusapp.proyectofinal.DTO.Evento;
import com.example.nexusapp.proyectofinal.Auxiliares.SubirImagen;
import com.example.nexusapp.proyectofinal.Configuracion.Configuracion;
import com.example.nexusapp.proyectofinal.DTO.Evento;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudEventos;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.R;
import com.example.nexusapp.proyectofinal.ui.eventos.EventosInvitar;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Crear Evento es un activity encargado de recibir datos para crear un nuevo evento.
 */
public class CrearEvento extends AppCompatActivity {

    private Button crearEvento, volver;
    private TextView descripcion, nombreEvento;
    private FirebaseCrudEventos firebaseCrudEventos;
    private CircleImageView portadaEventos;
    private ActivityResultLauncher<String> pickImageLauncher;
    public static Uri pathImage;
    private StorageReference storageRef;
    public static Evento eventoCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_crear_evento);
        ocultarNavegacion();
        inicializar();
        listeners();
    }

    void ocultarNavegacion(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * Inicializar atributos
     */
    void inicializar() {
        descripcion = findViewById(R.id.crearDescripcion);
        nombreEvento = findViewById(R.id.crearNombreEvento);
        portadaEventos = findViewById(R.id.portadaCrearEvento);
        crearEvento = findViewById(R.id.btnGuardarConfig);
        volver = findViewById(R.id.botonCancelar);
        firebaseCrudEventos = new FirebaseCrudEventos(this);
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                portadaEventos.setImageBitmap(bitmap);
                                pathImage = uri;
                            } catch (FileNotFoundException e) {
                            }

                        }
                    }
                });

    }

    /**
     * listeners es un método en espera de onClick para realizar acciones
     */
    void listeners() {
        crearEvento.setOnClickListener(v -> {
            if (pathImage != null) {
                String titulo = String.valueOf(pathImage).replace("/", "_").replace("//", "_").replace(".", "_").replace(":", "_").replace("%", "_");
               System.out.println(titulo + " ES LA FOTO ELEGIDAAAAA");
                crearEvento(titulo);
            }
            abrirVentana(EventosInvitar.class);
            pathImage = null;
        });

        volver.setOnClickListener(v -> {
            abrirVentana(MainActivity.class);
        });

        portadaEventos.setOnClickListener(n -> {

            pickImageLauncher.launch("image/*");

        });

    }

    /**
     * abrirVentana es un método que recibe una clase de tipo activity para visualizarlo
     *
     * @param view view a visualizar
     */
    void abrirVentana(Class view) {
        Intent i = new Intent(getApplicationContext(), view);
        startActivity(i);
        finish();
    }

    /**
     * crearEvento es el método que obtiene datos introducidos por el usuario
     * para crear el objeto evento y guardarlos en firebase
     * NO LO SUBE AUN A FIREBASE
     */
    void crearEvento(String urlImagen) {
        eventoCrear = new Evento();
        eventoCrear.setNombreEvento(String.valueOf(nombreEvento.getText()));
        eventoCrear.setDescripcionEvento(String.valueOf(descripcion.getText()));
        eventoCrear.setUsuarioCreador(Login.usuarioAutenticado.getNombreUsuario());
        eventoCrear.setIdEvento(UUID.randomUUID().toString());
        eventoCrear.setPortada(urlImagen);

      //  FirebaseStorage storage = FirebaseStorage.getInstance();
     //   storageRef = storage.getReference().child(eventoCrear.getPortada());
        new SubirImagen(Configuracion.pathImage, eventoCrear.getPortada());


        //  this.firebaseCrudEventos.create(eventoCrear);
    }
}
