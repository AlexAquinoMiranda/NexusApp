package com.example.nexusapp.proyectofinal.Mensajeria;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.AdapterMensajes;
import com.example.nexusapp.proyectofinal.Contacto.Contacto;
import com.example.nexusapp.proyectofinal.DTO.CrearMensaje;
import com.example.nexusapp.proyectofinal.DTO.Mensaje;
import com.example.nexusapp.proyectofinal.DTO.Notificacion;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseController;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.Notificacion.Notificaciones;
import com.example.nexusapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Mensajería es un activity en donde se visualiza toods los mensajesa
 * que se dan entre dos usuarios através de una sala de chat creados entre ellos
 */
public class Mensajeria extends AppCompatActivity {
    public static CrearMensaje chatAbrir;
    private FirebaseDatabase database;
    private DatabaseReference conversationRef;
    private RecyclerView recyclerView;
    private EditText textMensaje;
    private Button btnSend;
    private AdapterMensajes adapter;
    private TextView nombre;
    private CircleImageView perfil;
    static ArrayList<Mensaje> mensajes;
    private FirebaseController firebase;
    private ImageView revertChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);
        ocultarNavegacion();
        inicializar();

        if (mensajes != null) {
            adapter = new AdapterMensajes(mensajes, this);
            recyclerView.setAdapter(adapter);
        }
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
     * método que espera escucha de click de botones para realizar acciones
     */
    private void eventos() {
        mostrar();

        btnSend.setOnClickListener(n -> {
            if (textMensaje.getText().length() > 0) {
                guardarMensaje();
                textMensaje.setText("");
                mostrar();
            }
        });
        revertChat.setOnClickListener(n -> {
            Intent i = new Intent(getApplicationContext(), Contacto.class);
            startActivity(i);
            finish();

        });
    }

    /**
     * inicializar atributos
     */
    void inicializar() {
        revertChat = findViewById(R.id.reset);
        recyclerView = findViewById(R.id.recycler);
        textMensaje = findViewById(R.id.textMensaje);
        nombre = findViewById(R.id.user_name_Menssaje);
        btnSend = findViewById(R.id.btnSend);
        perfil = findViewById(R.id.perfilUsuario);
        database = FirebaseDatabase.getInstance();
        mensajes = new ArrayList<>();
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        if (chatAbrir != null) {
            firebase = new FirebaseController("chats", getApplicationContext());

        }
        mostrar();
    }

    /**
     * metodo que guarda los mensajes con el objeto Mensaje Y realiza una notificación
     */
    void guardarMensaje() {

        LocalDateTime fechaHora = null; // fecha y hora actual

        fechaHora = LocalDateTime.now();

        DateTimeFormatter formato = null; // formato deseado

        formato = DateTimeFormatter.ofPattern("dd-MM-HH:mm:ss");

        String fechaHoraString = "";

        fechaHoraString = fechaHora.format(formato);

        Mensaje p = new Mensaje();
        p.setMensaje(textMensaje.getText().toString());
        p.setDate(fechaHoraString);
        p.setToUser(Mensajeria.chatAbrir.getIdTo());
        p.setUid(UUID.randomUUID().toString());
        p.setFromUser(Login.usuarioAutenticado.getUid());
         firebase.getReference().child(chatAbrir.getNombreSala()).push().setValue(p);

        //   FirebaseDatabase database = FirebaseDatabase.getInstance();
      /**  DatabaseReference salaRef = firebase.getReference().child(chatAbrir.getNombreSala());
        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("date", ServerValue.TIMESTAMP);
        mensaje.put("fromUser", Login.usuarioAutenticado.getUid());
        mensaje.put("mensaje", p.getMensaje());
        mensaje.put("toUser", Mensajeria.chatAbrir.getIdTo());
        mensaje.put("uid", UUID.randomUUID().toString());

        salaRef.push().setValue(mensaje);
*/

        //falta ponerle el id al que va a recibir la notificación.
        Notificacion notificacion = new Notificacion();
        notificacion.setUid(UUID.randomUUID().toString());
        notificacion.setMensaje(" Tienes un nuevo mensaje de " + Login.usuarioAutenticado.getNombreUsuario());
        notificacion.setTipo("Mensajes");

        Notificaciones.notificar(notificacion, p.getToUser());
    }

    /**
     * Método que carga la lista de mensajes.
     */
    void mostrar() {

        firebase.getReference().child(chatAbrir.getNombreSala()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensajes.clear();
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    Mensaje value = element.getValue(Mensaje.class);
                    System.out.println(value.toString());
                    if (value.getFromUser().equals(Login.usuarioAutenticado.getUid())) {
                        //
                        System.err.println("entrando  em el lambadaa ------");
                        try {
                            for (Usuario user : Login.getUsers()) {
                                if (user.getUid().equals(value.getToUser())) {
                                    nombre.setText(user.getNombreUsuario());
                                    new FirebaseCargaImg(user.getPerfil(), perfil);
                                }
                            }
                            Usuario destino = Login.getUsers().stream().filter(e -> (e.getUid().equals(value.getToUser()))).findFirst().get();
                            if (destino != null) {
                                nombre.setText(destino.getNombreUsuario());
                                new FirebaseCargaImg(destino.getPerfil(), perfil);
                            }
                        } catch (Exception e) {
                            System.err.println("error em el lambadaa ------");
                        }
                    }
                    mensajes.add(value);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Mensaje", "Failed to read value.", error.toException());
            }
        });
    }
}