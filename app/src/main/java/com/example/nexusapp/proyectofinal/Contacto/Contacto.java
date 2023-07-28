package com.example.nexusapp.proyectofinal.Contacto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.AdapterContactos;
import com.example.nexusapp.proyectofinal.DTO.CrearMensaje;
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
 * Contacto es un activity en donde contiene una lista de usuarios al cual se puede enviar mensajes
 */
public class Contacto extends AppCompatActivity {
    private RecyclerView listaContacts;
    private DatabaseReference conversationRef;
    private FirebaseDatabase database;
    private AdapterContactos adapter;
    private ImageView volver;

    public static ArrayList<CrearMensaje> contactos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        ocultarNavegacion();
        inicializar();
        eventos();
    }
    void ocultarNavegacion(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * inicializar atributos
     */
    void inicializar() {
        listaContacts = findViewById(R.id.listContacts);
        volver = findViewById(R.id.volverAtrasContact);
        database = FirebaseDatabase.getInstance();
        conversationRef = database.getReference("Contacts");
        listaContacts.setLayoutManager(
                new LinearLayoutManager(this));

        mostrar();

    }

    /**
     * Metodo que obtiene todos los contactos de firebase y los rellena en una lista.
     */
    public void mostrar() {
        conversationRef = database.getReference("Conversaciones");
        conversationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Contacto.contactos.clear();
                System.out.println("entrandi en for de chatsssss\n");
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    CrearMensaje value = element.getValue(CrearMensaje.class);
                    System.out.println("--------- " + value.toString());
                    Contacto.contactos.add(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("------", "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * método que espera escucha de click de botones para realizar acciones y rellena lista de contactos
     */
    private void eventos() {

        volver.setOnClickListener(n -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });
        if (Login.getUsers() != null) {

            if (Login.getUsers().contains(Login.usuarioAutenticado)) {
                System.out.println("contiene a miiiiiiiiiiii");
                Login.getUsers().remove(Login.usuarioAutenticado);
            }

            adapter = new AdapterContactos(Login.getUsers(), this);
            listaContacts.setAdapter(adapter);
        }


    }
}