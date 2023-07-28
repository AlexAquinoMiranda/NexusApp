package com.example.nexusapp.proyectofinal.ui.paneles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nexusapp.proyectofinal.Auxiliares.SubirImagen;
import com.example.nexusapp.proyectofinal.DTO.Publicacion;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudPublicacion;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.R;
import com.example.nexusapp.databinding.SubirPublicacionBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

/**
 * PanelesFragmentes un fragment encargado de abrir galería y crear una publicación
 */
public class PanelesFragment extends Fragment {
    private SubirPublicacionBinding binding;
    private FloatingActionButton exit;
    private ActivityResultLauncher<String> pickImageLauncher;
    private ImageView subirImagen, imageView;
    private EditText descripcion;
    private View root;
    public static Uri pathImage;
    private PanelesViewModel dashboardViewModel;
    private FirebaseCrudPublicacion firebaseCrudPublicacion;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = new ViewModelProvider(this).get(PanelesViewModel.class);
        binding = SubirPublicacionBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        ocultarNavegacion();

        inicializar();
        listeners();

        pickImageLauncher.launch("image/*");
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * CrearPublicacion es un método que crea un objeto Publicacion y guardarlo en firebase
     *
     * @param titulo      titulo de la publicación
     * @param descripcion descripción de la publicación
     */
    void crearPublicacion(String titulo, String descripcion) {
        Publicacion p = new Publicacion();
        p.setLike(0);
        p.setUid(UUID.randomUUID().toString());
        p.setMeGusta(false);
        p.setDescripcion(descripcion);
        p.setUserImage(Login.usuarioAutenticado.getPerfil());
        p.setUserName(Login.usuarioAutenticado.getNombreUsuario());
        p.setIdUsuario(Login.usuarioAutenticado.getUid());
        p.setTítulo(titulo);
        p.setUrlImagen(titulo);
        for (Usuario u : Login.getUsers()) {
            p.addDatosLike(u.getUid(), false);//añado todos los usuarios y le doy que es false.
        }
        new SubirImagen(pathImage, p.getUrlImagen());
        this.firebaseCrudPublicacion.create(p);
    }

    /**
     * metodo que valida que todos los campos esten rellenos para realizar pulicación.
     */
    void validarCampos() {
        String desciption;

        desciption = this.descripcion.getText().toString();
        if (desciption == null || pathImage == null) {
            System.out.println("selecciona una foto.");
            Toast.makeText(root.getContext(), "Cancela y vuelve a intentar seleccionando una foto.", Toast.LENGTH_SHORT).show();

            return;
        } else {
            Random random = new Random();
            int numeroAleatorio = random.nextInt(999001) + 1000;

            String titulo = String.valueOf(pathImage).replace("/", "_").replace("//", "_").replace(".", "_").replace(":", "_").replace("%", "_").replace("(", "_").replace(")", "_");
            crearPublicacion(titulo + numeroAleatorio, desciption);
            abrirVentana(MainActivity.class);
            pathImage = null;
        }

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
     * listeners esperando realizar acciones de los botones,
     */
    void listeners() {
        subirImagen.setOnClickListener(v -> {
            validarCampos();
        });

        exit.setOnClickListener(v -> {
            //imageView.setImageResource(com.google.android.material.R.drawable.ima);
            abrirVentana(MainActivity.class);
        });
    }

    /**
     * inicializar atributos
     */
    void inicializar() {
        imageView = root.findViewById(R.id.imagePublicar);
        exit = root.findViewById(R.id.cancelar);
        descripcion = root.findViewById(R.id.descripcionPublicacion);
        subirImagen = root.findViewById(R.id.subirImagen);
        firebaseCrudPublicacion = new FirebaseCrudPublicacion(root.getContext());

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                InputStream inputStream = root.getContext().getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                imageView.setImageBitmap(bitmap);
                                pathImage = uri;
                                System.out.println(uri.toString());
                            } catch (FileNotFoundException e) {
                            }

                        }
                    }
                });
    }
}