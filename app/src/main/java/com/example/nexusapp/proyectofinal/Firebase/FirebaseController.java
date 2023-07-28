package com.example.nexusapp.proyectofinal.Firebase;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * FirebaseCotroller es una clase para inicializar firebase haciendo referencias.
 */
public class FirebaseController {
    private FirebaseDatabase firebase;
    private DatabaseReference reference;
    public FirebaseController(String database, Context context){
        this.reference=initFirebase(database,context);
    }
    /**
     * Método que genera una instancia de firebase y obtiene una refrencia a una
     * coleccion en especifico.
     * @param database Nombre de la coleccion de firebase a la  que se quiere obtener
     *                 referencia
     * @param context Conexto de la aplicacion
     * @return DatabaseRefrence con la referencia a una colección particular
     */
    private DatabaseReference initFirebase(String database, Context context) {
        FirebaseApp.initializeApp(context);
        //Instancia a la base de datos
        firebase = FirebaseDatabase.getInstance();
        //Referencia al nodo que queremos acceder
        reference= firebase.getReference(database);
        return reference;
    }
    public DatabaseReference getReference() {
        return reference;
    }
}
