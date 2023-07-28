package com.example.nexusapp.proyectofinal.Configuracion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nexusapp.proyectofinal.Auxiliares.SubirImagen;
import com.example.nexusapp.proyectofinal.DTO.Evento;
import com.example.nexusapp.proyectofinal.DTO.Historia;
import com.example.nexusapp.proyectofinal.DTO.Publicacion;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseController;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudHistoriaDisponible;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudPublicacion;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudUser;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.R;
import com.example.nexusapp.proyectofinal.ui.home.InicioFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Configuracion es un activity en donde da opciones de configurar el perfil de usuario autenticado.
 */
public class Configuracion extends AppCompatActivity {

    private Button btnCancelar, btnGuardar, eliminarCuenta;
    private CircleImageView imagenPerfilNuevo;
    private EditText nombre, nombreUsuario, description;
    private ActivityResultLauncher<String> pickImageLauncher;
    public static Uri pathImage;
    private StorageReference storageRef;
    private UploadTask uploadTask;

    private static ArrayList<Evento> listaEventos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_perfil);
        ocultarNavegacion();
        inicializar();
        rellenarDatos();
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
     * rellenarDatos es un método que carga los datos actuales del usuario Autenticado para luego ser modificado
     */
    void rellenarDatos() {
        nombre.setText(Login.usuarioAutenticado.getNombre());
        nombreUsuario.setText(Login.usuarioAutenticado.getNombreUsuario());
        description.setText(Login.usuarioAutenticado.getDescripcion());
        new FirebaseCargaImg(Login.usuarioAutenticado.getPerfil(), imagenPerfilNuevo);
    }

    /**
     * Guardar cambios es un método que crea un objeto Usuario que contiene el mismo
     * id del UsuarioAutenticado y lo modifica
     * @param urlImagen urlImagen es la imagen que se va a guardar de perfil, (puede ser el mismo o uno nuevo)
     */
    void guardarCambios(String urlImagen) {

        Usuario usuario = new Usuario();
        usuario.setUid(Login.usuarioAutenticado.getUid());
        usuario.setNombreUsuario(nombreUsuario.getText().toString());
        usuario.setApellido(Login.usuarioAutenticado.getApellido());
        usuario.setNombre(nombre.getText().toString());
        usuario.setPassword(Login.usuarioAutenticado.getPassword());
        usuario.setEmail(Login.usuarioAutenticado.getEmail());
        usuario.setPerfil(urlImagen);
        usuario.setDescripcion(description.getText().toString());


        cambiarNombrePublicaciones(usuario.getNombreUsuario(), usuario.getPerfil());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child(usuario.getPerfil());
        new SubirImagen(Configuracion.pathImage, usuario.getPerfil());
        new FirebaseCrudUser(getApplicationContext()).modify(usuario, usuario.getUid());
    }

    /**
     * cambiarNombrePublicaciones es un método que cambia el nombre de usuario cuando éste lo modifique
     * @param newName newName es el nuevo nombre que va a recibir
     */
    void cambiarNombrePublicaciones(String newName, String perfil) {
        for (Publicacion p : InicioFragment.getPublicationes()) {
            if (p.getIdUsuario().equals(Login.usuarioAutenticado.getUid())) {
                p.setUserName(newName);
                p.setUserImage(perfil);
                new FirebaseCrudPublicacion(getApplicationContext()).modify(p, p.getUid());
            }
        }
        for (Historia h : InicioFragment.listaHistorias) {
            if (h.getIdUsuario().equals(Login.usuarioAutenticado.getUid())) {
                h.setUsuarioNombre(newName);
                h.setPerfilUsuario(perfil);
                new FirebaseCrudHistoriaDisponible(getApplicationContext()).modify(h, h.getUidHistoria());
            }
        }

        this.cargarDatosEventos();
        // FirebaseCrudEventos crudEventos = new FirebaseCrudEventos(this);
        /**   for (Evento ev : listaEventos) {

         for (Usuario h : ev.getParticipantes()) {
         if (h.getUid().equals(Login.usuarioAutenticado.getUid())) {
         ev.getParticipantes().remove(h);
         System.out.println("se ha mofodijsd el perdil de eee" + h);
         h.setNombreUsuario(newName);
         h.setPerfil(perfil);
         ev.getParticipantes().add(h);

         new FirebaseCrudEventos(getApplicationContext()).modify(ev, ev.getIdEvento());
         }
         }



         }**/

    }

    /**
     * Inicializar atributos
     */
    void inicializar() {
        listaEventos = new ArrayList<>();
        btnCancelar = findViewById(R.id.botonCancelar);
        btnGuardar = findViewById(R.id.btnGuardarConfig);
        imagenPerfilNuevo = findViewById(R.id.imagenPerfilNuevo);
        nombre = findViewById(R.id.nuevoNombre);
        nombreUsuario = findViewById(R.id.nuevoNombreUsuario);
        description = findViewById(R.id.descripcionPerfil);
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                imagenPerfilNuevo.setImageBitmap(bitmap);
                                pathImage = uri;
                            } catch (FileNotFoundException e) {
                            }

                        }
                    }
                });

    }

    /**
     * método que espera escucha de click de botones para realizar acciones
     */
    void listeners() {
        btnCancelar.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });

        btnGuardar.setOnClickListener(v -> {
            validarCampos();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });

        imagenPerfilNuevo.setOnClickListener(n -> {
            pickImageLauncher.launch("image/*");

        });
    }

    /**
     * Metodo que valida que todos los campos esten rellenos para realizar antes de canbiar perfil.
     */
    void validarCampos() {

        if (TextUtils.isEmpty(nombreUsuario.getText()) || TextUtils.isEmpty(nombre.getText())) {
            return;
        } else if (pathImage != null) {
            String titulo = String.valueOf(pathImage).replace("/", "_").replace("//", "_").replace(".", "_").replace(":", "_").replace("%", "_");

            guardarCambios(titulo);
            pathImage = null;
            abrirVentana(MainActivity.class);
        } else {
            guardarCambios(Login.usuarioAutenticado.getPerfil());
            abrirVentana(MainActivity.class);
        }
    }

    /**
     * cargarDatosEventos metodo que carga datos de firebase en una lista
     */
    void cargarDatosEventos() {
        FirebaseController firebase = new FirebaseController("Eventos", this);
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaEventos.clear();
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    Evento evento = element.getValue(Evento.class);
                    System.out.println(evento.toString());
                    listaEventos.add(evento);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("------", "Failed to read value.", error.toException());
            }
        });

    }

    /**
     * abrirVentana un método que recibe una clase para abrirla posteriormente.
     *
     * @param view view es la ventana que se va a abrir
     */
    void abrirVentana(Class view) {
        Intent i = new Intent(getApplicationContext(), view);
        startActivity(i);
        finish();
    }

}
