package com.example.nexusapp.proyectofinal.DTO;

/**
 * CrearMensaje es un objeto que crea como una sala de chat entre dos usuarios
 */
public class CrearMensaje {
    private String nombreSala, idFrom, idTo;


    public CrearMensaje() {
    }

    public String getIdTo() {
        return idTo;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public String getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(String idFrom) {
        this.idFrom = idFrom;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    @Override
    public String toString() {
        return "CrearMensaje{" +
                "nombreSala='" + nombreSala + '\'' +
                ", idFrom='" + idFrom + '\'' +
                ", idTo='" + idTo + '\'' +
                '}';
    }
}
