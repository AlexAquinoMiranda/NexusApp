package com.example.nexusapp.proyectofinal.Firebase;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class CargarImagenGlide {

    private StorageReference storageRef;
    public CargarImagenGlide(String archivo, ImageView image, ProgressBar pro, Context context) {
        this.cargarFotos(archivo, image,pro,  context);
    }

    public CargarImagenGlide(String archivo, CircleImageView image,Context context) {
        this.cargarFotos(archivo, image,  context);
    }

    /**
     * Es un método que recibe datos de imagen a buscar en firebase para posteriormente mostrarla
     *
     * @param archivo archivo es el nombre que tiene para buscar en firebase
     * @param image   image es View de tipo CircleImageView en el que se mostrará la foto.
     */
    private void cargarFotos(String archivo, ImageView image,ProgressBar pro, Context c) {
        storageRef = FirebaseStorage.getInstance().getReference().child(archivo);
        mostrarFotoEjemploViewGlide(image,pro, c);
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
    private void mostrarFotoEjemploViewGlide(ImageView foto, ProgressBar progressBar, Context context) {
        // Descargar la imagen y mostrarla en el ImageView utilizando Picasso
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Cargar la imagen en el ImageView utilizando Picasso
                Glide.with(context)
                        .load(uri)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                        })
                        .into(foto);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception exception) {
                // Manejar el error en caso de que la descarga de la URL falle
            }
        });
    }
}
