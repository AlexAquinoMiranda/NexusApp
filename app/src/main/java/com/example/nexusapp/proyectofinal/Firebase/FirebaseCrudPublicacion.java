package com.example.nexusapp.proyectofinal.Firebase;

import android.content.Context;

import androidx.activity.result.ActivityResultLauncher;

import com.example.nexusapp.proyectofinal.DTO.Publicacion;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
/**
 * FirebaseCrudPublicacion una clase de apoyo para las publicaciones. (Crear, leer, moidificar y eliminar)
 */
public class FirebaseCrudPublicacion implements ICrudFirebase<Publicacion> {
    private FirebaseController firebase;
    private ActivityResultLauncher<String> pickImageLauncher;
    UploadTask uploadTask;

    StorageReference storageRef;

    public FirebaseCrudPublicacion(Context context) {
        this.firebase = new FirebaseController("Publicaciones", context);
    }

    @Override
    public void create(Publicacion publicacion) {
        // FirebaseStorage storage = FirebaseStorage.getInstance();
        //  storageRef = storage.getReference().child(publicacion.getTítulo());//subo a storage
        //  new SubirImagen(SubirPublicacion.pathImage, publicacion.getUrlImagen());
        String claveUnica =  this.firebase.getReference().push().getKey();
        publicacion.setUid(claveUnica);
        this.firebase.getReference().child(claveUnica).setValue(publicacion);//guardo en publicaciones
        //guardarImagen();
    }

    @Override
    public void delete(String uid) {
        this.firebase.getReference().child(uid).removeValue();
    }

    @Override
    public void modify(Publicacion publicacion, String uid) {
        this.firebase.getReference().child(uid).setValue(publicacion);
    }

    @Override
    public Publicacion get(String uuid) {
        return null;
    }

    @Override
    public ArrayList<Publicacion> getAll() {
        return null;
    }

}
