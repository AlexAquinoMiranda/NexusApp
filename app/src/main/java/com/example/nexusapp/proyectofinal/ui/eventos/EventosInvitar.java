package com.example.nexusapp.proyectofinal.ui.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexusapp.proyectofinal.Adaptadores.AdapterUsuariosInvitar;
import com.example.nexusapp.proyectofinal.DTO.Notificacion;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Eventos.CrearEvento;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudEventos;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.proyectofinal.Notificacion.Notificaciones;
import com.example.nexusapp.R;

import java.util.UUID;

public class EventosInvitar extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView exit;
    private Button crearEvento;

    AdapterUsuariosInvitar adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_invitar);
        ocultarNavegacion();
        inicializar();
        listeners();
    }

    void ocultarNavegacion(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * Inicializar atributos
     */
    public void inicializar() {
        this.recyclerView = findViewById(R.id.recyclerEventosParticipantes);
        this.exit = findViewById(R.id.salirParticipantes);
        crearEvento = findViewById(R.id.buttonCreate);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext()));
        if (Login.getUsers() != null) {

            if (Login.getUsers().contains(Login.usuarioAutenticado)) {
                System.out.println("contiene a miiiiiiiiiiii");
                Login.getUsers().remove(Login.usuarioAutenticado);
            }

            adapter = new AdapterUsuariosInvitar(Login.getUsers(), this);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Metodo que crea una notificación para todos los usuarios que sean participantes de un evento.
     * @param id id es el identificador del usuario al que se le va a notificar
     */
    void notificarEventosInvitacion(String id) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUid(UUID.randomUUID().toString());
        notificacion.setMensaje("Has sido invitado al evento '" + CrearEvento.eventoCrear.getNombreEvento() + "' de " + CrearEvento.eventoCrear.getUsuarioCreador());
        notificacion.setTipo("Eventos");
        Notificaciones.notificar(notificacion, id);

    }

    /**
     * método en espera de onClick para realizar acciones
     */
    void listeners() {
        crearEvento.setOnClickListener(v -> {
            if (AdapterUsuariosInvitar.listaParticipantes.isEmpty() || AdapterUsuariosInvitar.listaParticipantes == null) {
                Toast.makeText(getApplicationContext(), "Lista de participantes vacía", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (CrearEvento.eventoCrear != null) {

                    AdapterUsuariosInvitar.listaParticipantes.add(Login.usuarioAutenticado);
                    CrearEvento.eventoCrear.setParticipantes(AdapterUsuariosInvitar.listaParticipantes); //recibe la lista llena de adapter
                    for (Usuario usersNotificar : CrearEvento.eventoCrear.getParticipantes()) {
                        if (!usersNotificar.getUid().equals(Login.usuarioAutenticado.getUid())) {
                            notificarEventosInvitacion(usersNotificar.getUid());
                        }

                    }

                    new FirebaseCrudEventos(this).create(CrearEvento.eventoCrear);
                }
            }
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

            //ABRIR VENTANA EVENTOS

        });
        exit.setOnClickListener(v -> {
            //volver atrás
        });

    }
}