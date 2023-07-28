package com.example.nexusapp.proyectofinal.Firebase;

import android.content.Context;
import android.util.Log;

import com.example.nexusapp.proyectofinal.DTO.Evento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * FirebaseCrudEventos una clase de apoyo para los eventos. (Crear, leer, moidificar y eliminar)
 */
public class FirebaseCrudEventos implements ICrudFirebase<Evento> {

    private FirebaseController firebase;
    public static ArrayList<Evento> listaEventos = new ArrayList<>();

    public FirebaseCrudEventos(Context context) {
        this.firebase = new FirebaseController("Eventos", context);
    }


    @Override
    public void create(Evento evento) {
        this.firebase.getReference().child(evento.getIdEvento()).setValue(evento);
    }

    @Override
    public void delete(String uid) {

        this.firebase.getReference().child(uid).removeValue();
    }

    @Override
    public void modify(Evento evento, String uid) {
        this.firebase.getReference().child(uid).setValue(evento);
    }

    @Override
    public Evento get(String uuid) {


        return null;
    }

    @Override
    public ArrayList<Evento> getAll() {
        cargarDatosEventos();

        return listaEventos;
    }

    void cargarDatosEventos() {
        this.firebase.getReference().addValueEventListener(new ValueEventListener() {
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
}
