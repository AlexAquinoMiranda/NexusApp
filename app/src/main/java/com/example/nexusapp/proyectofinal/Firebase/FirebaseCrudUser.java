package com.example.nexusapp.proyectofinal.Firebase;

import android.content.Context;

import com.example.nexusapp.proyectofinal.DTO.Usuario;

import java.util.ArrayList;

/**
 * FirebaseCrudUser una clase de apoyo para los usuarios. (Crear, leer, moidificar y eliminar)
 */
public class FirebaseCrudUser implements ICrudFirebase<Usuario> {
    private FirebaseController firebase;

    public FirebaseCrudUser(Context context) {
        this.firebase = new FirebaseController("Usuarios", context);
    }

    @Override
    public void create(Usuario user) {
        this.firebase.getReference().child(user.getUid()).setValue(user);
    }

    @Override
    public void delete(String uid) {
        this.firebase.getReference().child(uid).removeValue();
    }

    @Override
    public void modify(Usuario user, String uid) {

        this.firebase.getReference().child(uid).setValue(user);
    }

    @Override
    public Usuario get(String uuid) {
        return null;
    }

    @Override
    public ArrayList<Usuario> getAll() {

        return null;
    }
}
