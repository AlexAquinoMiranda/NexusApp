package com.example.nexusapp.proyectofinal.DTO;

/**
 * Notificación es una clase que crea una notificación y es asignado a un
 * usuario para avisar de eventos que estén ocurriendo con los usuarios
 */
public class Notificacion {
    private String mensaje, usuarioNotifica, uid;

    public Notificacion() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTipo() {
        return usuarioNotifica;
    }

    public void setTipo(String tipo) {
        this.usuarioNotifica = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "mensaje='" + mensaje + '\'' +
                ", tipo='" + usuarioNotifica + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
