package com.example.nexusapp.proyectofinal.Auxiliares;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * SubirImagen es una clase encargada de subir imagenes a firebase Storage.
 */
public class SubirImagen {
    private UploadTask uploadTask;

    private StorageReference storageRef;

    public SubirImagen(Uri urlImagen, String nombreImagen) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child(nombreImagen);
        guardarImagen(urlImagen);
    }
    /**
     * GuardarImagen es un método que guarda la imagen a firebase
     */
    private void guardarImagen(Uri urlImagen) {
        if (urlImagen != null) {
            uploadTask = storageRef.putFile(urlImagen);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // La imagen se subió correctament
                    System.out.println("se subuiopppppp");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.err.println(exception.getMessage());
                    // Hubo un error al subir la imagen
                }
            });
        }
    }
}
