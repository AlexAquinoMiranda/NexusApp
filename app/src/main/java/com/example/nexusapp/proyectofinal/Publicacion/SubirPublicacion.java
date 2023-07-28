package com.example.nexusapp.proyectofinal.Publicacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nexusapp.proyectofinal.Auxiliares.SubirImagen;
import com.example.nexusapp.proyectofinal.DTO.Historia;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseController;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudHistoriaDisponible;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * SubirPublicacion es una clase que abre la galería para seleccionar una foto y posteriormente crear la publicación
 * <p>
 * todo: Cambiar a subirHistoria....
 */
public class SubirPublicacion extends AppCompatActivity {
    private FloatingActionButton exit;
    private ActivityResultLauncher<String> pickImageLauncher;
    Button btnAbrir;
    ImageView subirImagen;
    EditText titulo, descripcion;
    ImageView imageView;
    private FirebaseController firebase;
    public static Uri pathImage;
    private FirebaseCrudHistoriaDisponible firebaseCrudHistoria;

    public ArrayList<Historia> historiasLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_publicacion);
        ocultarNavegacion();
        inicializar();
        listeners();
        pickImageLauncher.launch("image/*");
        listadoDeHistorias();
    }

    /**
     * método que obtiene fecha del sistema para guardarlo al momento de crear una historia
     */
    String obtenerFechaActual() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss", Locale.getDefault());
        Date fechaActual = new Date();
        return dateFormat.format(fechaActual);

    }

    void ocultarNavegacion() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * Crea una nueva publicación con la URL de la imagen proporcionada.
     * Genera un identificador único para la historia y establece la información relacionada.
     * Si ya existe una historia para el usuario actual, se agrega la foto a esa historia y se actualiza en la base de datos.
     * Si no existe una historia para el usuario actual, se crea una nueva historia y se guarda en la base de datos.
     * @param urlImagen La URL de la imagen a agregar a la publicación.
     */
    void crearPublicacion(String urlImagen) {

        LocalDateTime fechaHoraActual = LocalDateTime.now();

        // Crear el patrón para obtener día, hora y minuto
        String patron = "ddHHmm";

        // Crear el objeto DateTimeFormatter
        DateTimeFormatter formato = DateTimeFormatter.ofPattern(patron);

        // Formatear la fecha y hora actual usando el patrón y el formato
        String fechaHoraFormateada = fechaHoraActual.format(formato);

        // Imprimir la fecha y hora actual formateada
        System.out.println("Fecha y hora actual formateada: " + fechaHoraFormateada);

        // Obtener la fecha y hora del día siguiente
        LocalDateTime fechaHoraManana = fechaHoraActual.plusDays(1);

        // Formatear la fecha y hora del día siguiente usando el mismo patrón y formato
        String fechaHoraMananaFormateada = fechaHoraManana.format(formato);

        Historia p = new Historia();
        Date date = new Date();

// Calcula la fecha del día siguiente a la misma hora en formato de milisegundos
        long undia = 24L * 60L * 60L * 1000L;
        Date siguienteDia = new Date(date.getTime() + undia);

// Convierte los objetos Date en longs
        long inicio = date.getTime();
        long fin = siguienteDia.getTime();
        p.setTiempoFin(Long.parseLong(fechaHoraMananaFormateada));

        Date fechaActual = new Date(); // Obtén la fecha actual

        long fechaActualLong = fechaActual.getTime();
        p.setTiempoInicio(fechaActualLong);
        p.setUidHistoria(UUID.randomUUID().toString());
        p.setPerfilUsuario(Login.usuarioAutenticado.getPerfil());
        p.setUsuarioNombre(Login.usuarioAutenticado.getNombreUsuario());
        p.setIdUsuario(Login.usuarioAutenticado.getUid());
        p.addFoto(urlImagen, p.getTiempoInicio());

        if (comprobarExisteHistoria() != null) {
            comprobarExisteHistoria().addFoto(urlImagen, p.getTiempoInicio());
            this.firebaseCrudHistoria.modify(comprobarExisteHistoria(), comprobarExisteHistoria().getUidHistoria());
            new SubirImagen(SubirPublicacion.pathImage, urlImagen);
            System.out.println(comprobarExisteHistoria().toString());
            System.out.println("se moidficoooooo");
        } else {
            new SubirImagen(SubirPublicacion.pathImage, urlImagen);
            this.firebaseCrudHistoria.create(p);
            System.out.println("se creoooooooooo");
        }
    }

    /**
     * Comprueba si existe una historia para el usuario autenticado.
     * @return La instancia de la historia si existe una para el usuario actual, o null si no hay ninguna.
     */
    Historia comprobarExisteHistoria() {
        Historia condicion = null;
        for (Historia i : historiasLista) {
            if (i.getIdUsuario().equals(Login.usuarioAutenticado.getUid())) {
                condicion = i;
                break;
            }
        }
        return condicion;
    }

    void abrirVentana(Class view) {
        Intent i = new Intent(getApplicationContext(), view);
        startActivity(i);
        finish();
    }

    /**
     * Obtiene el listado de historias desde Firebase Database.
     * Utiliza un ValueEventListener para obtener los datos y los agrega a la lista de historias.
     * Si ocurre un error durante la lectura de los datos, se maneja el error.
     */
    void listadoDeHistorias() {
        firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historiasLista.clear();
                Historia story = null;
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    story = element.getValue(Historia.class);
                    historiasLista.add(story);
                }
                // Agregar la nueva foto a la historia
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error
            }
        });
    }

    /**
     * inicializar atributos
     */
    void inicializar() {
        historiasLista = new ArrayList<>();
        imageView = findViewById(R.id.imagePublicar);
        firebase = new FirebaseController("Historias", getApplicationContext());
        exit = findViewById(R.id.cancelar);
        descripcion = findViewById(R.id.descripcionPublicacion);
        subirImagen = findViewById(R.id.subirImagen);
        firebaseCrudHistoria = new FirebaseCrudHistoriaDisponible(getApplicationContext());

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap);
                        pathImage = uri;
                    } catch (FileNotFoundException e) {
                    }

                }
            }
        });
    }

    /**
     * listeners esperando realizar acciones de los botones,
     */
    void listeners() {
        subirImagen.setOnClickListener(v -> {
            validarCampos();
        });

        exit.setOnClickListener(v -> {
            abrirVentana(MainActivity.class);
        });
    }

    /**
     * Metodo que valida que todos los campos esten rellenos para realizar pulicación.
     */
    void validarCampos() {
        if (pathImage == null) {
            Toast.makeText(this, "Cancela y vuelve a intentar seleccionando una foto.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String titulo = String.valueOf(pathImage).replace("/", "_").replace("//", "_").replace(".", "_").replace(":", "_").replace("%", "_");

            crearPublicacion(titulo);
            abrirVentana(MainActivity.class);
            pathImage = null;
        }
    }
}