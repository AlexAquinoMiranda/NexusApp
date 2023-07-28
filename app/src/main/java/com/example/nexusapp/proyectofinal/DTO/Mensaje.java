package com.example.nexusapp.proyectofinal.DTO;

/**
 * Mensaje es una clase que se utiliza como auxiliar para los datos de mensajes
 * que se envíen entre dos usuarios
 */
public class Mensaje {
    public Mensaje() {
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "mensaje='" + mensaje + '\'' +
                ", date='" + date + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", id='" + uid + '\'' +
                '}';
    }

    private String mensaje;
    private String date;
    private String fromUser;
    private String toUser;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
