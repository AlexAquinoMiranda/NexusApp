package com.example.nexusapp.proyectofinal.Auxiliares;

import android.util.Log;

import com.example.nexusapp.proyectofinal.Contacto.Contacto;
import com.example.nexusapp.proyectofinal.DTO.CrearMensaje;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.Mensajeria.Mensajeria;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * AuxiliarChat es una clase que recibe un usuario de destino en donde comprueba si ya existe
 * una sala entre el destinatario y el usuario autencticado
 */
public class AuxiliarChat {
    private DatabaseReference conversationRef;
    private FirebaseDatabase database;
    static Usuario user;
    static ArrayList<CrearMensaje> contactos;

    public AuxiliarChat(Usuario userTo) {
        database = FirebaseDatabase.getInstance();
        conversationRef = database.getReference("Conversaciones");
        AuxiliarChat.contactos = new ArrayList<>();
        mostrar();
        crearConversation(userTo);
    }
    /**
     * metodo que crea en firebase una referencia hacia su chat.
     *
     * @param userTo
     */
    public void crearConversation(Usuario userTo) {

        System.out.println(" el id del logeado es : " + Login.usuarioAutenticado.getUid() +
                "\n y el del que va a enviar el mensaje es " + userTo.getUid());
        user = userTo;
        CrearMensaje encontrado = null;
        conversationRef = database.getReference("Conversaciones");
        int valorChat = 0;

        if (encontrarChat(userTo) != null) {
            System.out.print("true ---------");
            Mensajeria.chatAbrir = encontrarChat(userTo);
            return;
        } else {
            System.out.print("Falseeee ---------");
            CrearMensaje con = new CrearMensaje();
            con.setNombreSala(Login.usuarioAutenticado.getUid() + "-" + userTo.getUid());
            con.setIdFrom(Login.usuarioAutenticado.getUid());
            con.setIdTo(userTo.getUid());
            conversationRef.child(con.getNombreSala()).setValue(con);
            Mensajeria.chatAbrir = con;
        }

        ///abrir chat con estos datos
    }

    /**
     * encontrarChat es un método que busca en una lista de contactos si ya se ha establecido una sala de chat
     *
     * @param user user es el usuario que recibiría el mensaje
     * @return CrearMensaje es una sala de chat que se va a abrir y visualizar
     */
    CrearMensaje encontrarChat(Usuario user) {
        CrearMensaje valor = null;
        if (!Contacto.contactos.isEmpty()) {
            for (CrearMensaje value : Contacto.contactos) {
                System.out.println(" ----------------" + value.toString());
                if (value.getNombreSala().equals(Login.usuarioAutenticado.getUid() + "-" + user.getUid())
                        || value.getNombreSala() == Login.usuarioAutenticado.getUid() + "-" + user.getUid() ||
                        value.getNombreSala() == user.getUid() + "-" + Login.usuarioAutenticado.getUid()
                        || value.getNombreSala().equals(user.getUid() + "-" + Login.usuarioAutenticado.getUid())) {
                    // if (value.getNombreSala() == "49d758ea-48a8-44cb-91fe-6b8b8874c833-2be14fc6-88a0-41dd-9886-896a9e602766" ||
                    //         value.getNombreSala().equals("49d758ea-48a8-44cb-91fe-6b8b8874c833-2be14fc6-88a0-41dd-9886-896a9e602766")) {

                    System.out.print("trueeee  foooorr---------");
                    System.out.print("trueeee ---------");

                    valor = value;
                }

            }
        } else {
            System.out.println("listavaciaaa --------");
        }
        return valor;
    }

    /**
     * mostrar es un método que rellena una lista de Mensajes/salas que estén disponibles en firebase
     */
    public void mostrar() {
        conversationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AuxiliarChat.contactos.clear();
                System.out.println("entrandi en for de chatsssss\n");
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    CrearMensaje value = element.getValue(CrearMensaje.class);
                    System.out.println(value.toString());
                    AuxiliarChat.contactos.add(value);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("------", "Failed to read value.", error.toException());
            }
        });

    }

}
