package com.example.nexusapp.proyectofinal.Notificacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.AdapterNotificaciones;
import com.example.nexusapp.proyectofinal.DTO.Notificacion;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Notificaciones es un activity en donde se visualiza todas las notificaciones que
 * recibe el usuario autenticado
 */
public class Notificaciones extends AppCompatActivity {
    private DatabaseReference conversationRef;
    static FirebaseDatabase database;
    static DatabaseReference notificacionesRef;
    static ArrayList<Notificacion> notificaciones;
    private RecyclerView recyclerView;
    private ImageView atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        ocultarNavegacion();
        inicializar();
        rellenarLista();
        // cargarDatos();
        listeners();
    }

    void ocultarNavegacion() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * listeners es un método en donde realiza acciones de botones
     * cuando estas se presionen
     */
    void listeners() {
        atras.setOnClickListener(m -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    /**
     * cargarDatos es un método que rellena el recyclerView de datos.
     */
    void cargarDatos() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AdapterNotificaciones adapterNotificaciones =
                new AdapterNotificaciones(Notificaciones.notificaciones, getApplicationContext());
        recyclerView.setAdapter(adapterNotificaciones);
    }

    /**
     * Inicializar atributos
     */
    void inicializar() {
        recyclerView = findViewById(R.id.recyclerViewNotify);
        atras = findViewById(R.id.atras);
        notificaciones = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        conversationRef = database.getReference("Publicaciones");
        notificacionesRef = database.getReference("Notificaciones").child(Login.usuarioAutenticado.getUid());
        //InicioFragment.getPublicationes()
    }

    /**
     * rellenarLista es un método que rellena un arrayList de Notificaciones de firebase
     */
    void rellenarLista() {
        notificacionesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Notificaciones.notificaciones.clear();
                for (DataSnapshot element : snapshot.getChildren()) {
                    Notificacion notificacion = element.getValue(Notificacion.class);
                    System.out.println(notificacion.toString());

                    Notificaciones.notificaciones.add(notificacion);
                }
                cargarDatos();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * notificar es un método que se utiliza para cuando ocurra un evento{'mensaje', 'me gusta'} se envíe una notificación
     *
     * @param notificacion notificacion es el objeto que contiene los datos a notificar
     * @param idUsuario    idUsuario es el id del usuario que va a recibir la notificación
     */
    public static void notificar(Notificacion notificacion, String idUsuario) {
        /** database = FirebaseDatabase.getInstance();
         notificacionesRef = database.getReference("Notificaciones").child(idUsuario);
         notificacionesRef.child(notificacion.getUid()).setValue(notificacion);
         */

        database = FirebaseDatabase.getInstance();
        notificacionesRef = database.getReference("Notificaciones").child(idUsuario);
        String claveUnica = notificacionesRef.push().getKey();
        notificacion.setUid(claveUnica);
        System.out.println("la clave unica essssss (((((((((( " + claveUnica);
        notificacionesRef.child(claveUnica).setValue(notificacion);
    }


}