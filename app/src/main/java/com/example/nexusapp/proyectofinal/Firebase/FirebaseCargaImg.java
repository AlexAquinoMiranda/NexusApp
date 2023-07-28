package com.example.nexusapp.proyectofinal.Firebase;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * {@link FirebaseCargaImg } es una clase encargada de visualizar las imagenes que se encuentran en firebase
 */
public class FirebaseCargaImg {
    private StorageReference storageRef;

    /**
     * Constructor que recibe nombre del archivo y view en el que se va a visualizar
     *
     * @param archivo archivo es el nombre que tiene en firebase
     * @param image   image es View de tipo CircleImageView
     */
    public FirebaseCargaImg(String archivo, CircleImageView image) {
        this.cargarFotos(archivo, image);
    }

    public FirebaseCargaImg(String archivo, ImageView image, Context context) {
        this.cargarFotos(archivo, image, context);
    }


    /**
     * Constructor que recibe nombre del archivo y view en el que se va a visualizar
     *
     * @param archivo archivo es el nombre que tiene en firebase
     * @param image   image es View de tipo ImageView
     */
    public FirebaseCargaImg(String archivo, ImageView image) {
        this.cargarImageView(archivo, image);
    }

    public FirebaseCargaImg(String archivo, ImageView image, ProgressBar p) {
        this.cargarImageViewProgress(archivo, image, p);

    }


    /**
     * Es un método que recibe datos de imagen a buscar en firebase para posteriormente mostrarla
     *
     * @param archivo archivo es el nombre que tiene para buscar en firebase
     * @param image   image es View de tipo ImageView en el que se mostrará la foto.
     */
    private void cargarImageView(String archivo, ImageView image) {
        storageRef = FirebaseStorage.getInstance().getReference().child(archivo);
        mostrarFotoEjemploView(image);

    }


    /**
     * Es un método que recibe datos de imagen a buscar en firebase para posteriormente mostrarla
     *
     * @param archivo archivo es el nombre que tiene para buscar en firebase
     * @param image   image es View de tipo ImageView en el que se mostrará la foto.
     */
    private void cargarImageViewProgress(String archivo, ImageView image, ProgressBar p) {
        storageRef = FirebaseStorage.getInstance().getReference().child(archivo);
        mostrarFotoEjemploViewProgress(image, p);

    }

    /**
     * Es un método que recibe datos de imagen a buscar en firebase para posteriormente mostrarla
     *
     * @param foto foto es View de tipo ImageView en el que se mostrará la foto.
     */
    private void mostrarFotoEjemploViewGlide(ImageView foto, Context context) {
        // Descargar la imagen y mostrarla en el ImageView utilizando Picasso
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Cargar la imagen en el ImageView utilizando Picasso
                Glide.with(context)
                        .load(uri)
                        .into(foto);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception exception) {
                // Manejar el error en caso de que la descarga de la URL falle
            }
        });
    }

    /**
     * Es un método que recibe datos de imagen a buscar en firebase para posteriormente mostrarla
     *
     * @param foto foto es View de tipo ImageView en el que se mostrará la foto.
     */
    private void mostrarFotoEjemploView(ImageView foto) {
        // Descargar la imagen y mostrarla en el ImageView utilizando Picasso
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Cargar la imagen en el ImageView utilizando Picasso
                Picasso.get()
                        .load(uri)
                        .into(foto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception exception) {
                // Manejar el error en caso de que la descarga de la URL falle
            }
        });
    }

    /**
     * Es un método que recibe datos de imagen a buscar en firebase para posteriormente mostrarla
     *
     * @param archivo archivo es el nombre que tiene para buscar en firebase
     * @param image   image es View de tipo CircleImageView en el que se mostrará la foto.
     */
    private void cargarFotos(String archivo, CircleImageView image) {
        storageRef = FirebaseStorage.getInstance().getReference().child(archivo);
        mostrarFotoEjemplo(image);
    }

    /**
     * Es un método que recibe datos de imagen a buscar en firebase para posteriormente mostrarla
     *
     * @param archivo archivo es el nombre que tiene para buscar en firebase
     * @param image   image es View de tipo CircleImageView en el que se mostrará la foto.
     */
    private void cargarFotos(String archivo, ImageView image, Context c) {
        storageRef = FirebaseStorage.getInstance().getReference().child(archivo);
        mostrarFotoEjemploViewGlide(image, c);
    }

    /**
     * Es un método que recibe datos de imagen a buscar en firebase para posteriormente mostrarla
     *
     * @param foto foto es View de tipo CircleImageView en el que se mostrará la foto.
     */
    private void mostrarFotoEjemplo(CircleImageView foto) {
        // Descargar la imagen y mostrarla en el ImageView utilizando Picasso
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Cargar la imagen en el ImageView utilizando Picasso
                Picasso.get()
                        .load(uri)
                        .into(foto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception exception) {
                // Manejar el error en caso de que la descarga de la URL falle
            }
        });
    }

    /**
     * Es un método que recibe datos de imagen a buscar en firebase para posteriormente mostrarla
     *
     * @param foto foto es View de tipo ImageView en el que se mostrará la foto.
     */
    private void mostrarFotoEjemploViewProgress(ImageView foto, ProgressBar progressBar) {
        // Descargar la imagen y mostrarla en el ImageView utilizando Picasso
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Cargar la imagen en el ImageView utilizando Picasso
                Picasso.get()
                        .load(uri)
                        .into(foto, new Callback() {
                            @Override
                            public void onSuccess() {
                                // La imagen se ha cargado exitosamente
                                progressBar.setVisibility(View.GONE); // Oculta el ProgressBar
                            }

                            @Override
                            public void onError(Exception e) {
                                // Maneja la falla de carga de la imagen
                                progressBar.setVisibility(View.GONE); // Oculta el ProgressBar
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception exception) {
                // Manejar el error en caso de que la descarga de la URL falle
            }
        });
    }
}
