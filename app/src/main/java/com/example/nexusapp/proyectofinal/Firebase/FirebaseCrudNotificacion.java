package com.example.nexusapp.proyectofinal.Firebase;

import android.content.Context;

import com.example.nexusapp.proyectofinal.DTO.Notificacion;

import java.util.ArrayList;

/**
 * FirebaseCrudNotificacion una clase de apoyo para las notificaciones. (Crear, leer, moidificar y eliminar)
 */
public class FirebaseCrudNotificacion implements ICrudFirebase<Notificacion> {
    private FirebaseController firebase;
    private String idUsuarioNotificado;

    public FirebaseCrudNotificacion(String idUsuario, Context context) {
        this.firebase = new FirebaseController("Notificaciones", context);
        this.idUsuarioNotificado = idUsuario;
    }

    @Override
    public void create(Notificacion notificacion) {
        this.firebase.getReference().child(this.idUsuarioNotificado).child(notificacion.getUid()).setValue(notificacion);
    }

    @Override
    public void delete(String uid) {
        this.firebase.getReference().child(this.idUsuarioNotificado).child(uid).removeValue();
    }

    @Override
    public void modify(Notificacion notificacion, String uid) {
        this.firebase.getReference().child(uid).setValue(notificacion);
    }

    @Override
    public Notificacion get(String uuid) {
        return null;
    }

    @Override
    public ArrayList<Notificacion> getAll() {
        return null;
    }
}
