package com.example.nexusapp.proyectofinal.Firebase;

import android.content.Context;

import androidx.activity.result.ActivityResultLauncher;

import com.example.nexusapp.proyectofinal.DTO.Historia;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

/**
 * FirebaseCrudHistoriaDisponible una clase de apoyo para las historias. (Crear, leer, moidificar y eliminar)
 */
public class FirebaseCrudHistoriaDisponible  implements ICrudFirebase<Historia>{
    private FirebaseController firebase;
    private ActivityResultLauncher<String> pickImageLauncher;
    UploadTask uploadTask;

    StorageReference storageRef;
    public FirebaseCrudHistoriaDisponible(Context context){
        this.firebase = new FirebaseController("Historias" ,context);
    }
    @Override
    public void create(Historia historia) {
        this.firebase.getReference().child(historia.getUidHistoria()).setValue(historia);
    }

    @Override
    public void delete(String uid) {
        this.firebase.getReference().child(uid).removeValue();
    }

    @Override
    public void modify(Historia historia, String uid) {

        this.firebase.getReference().child(uid).setValue(historia);//reemplaza con nuevos valores

    }

    @Override
    public Historia get(String uuid) {

        return null;
    }

    @Override
    public ArrayList<Historia> getAll() {
        return null;
    }
}
