package com.example.nexusapp.proyectofinal.DTO;

/**
 * Comentar es un objeto que guarda datos de comentarios de una publicación
 */
public class Comentar {

    private String nombreUsuario;
    private String perfilUsuario;
    private String comentario;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String uuid;
    public Comentar() {
    }

    public String getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(String perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public String toString() {
        return "Comentar{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", perfilUsuario='" + perfilUsuario + '\'' +
                ", comentario='" + comentario + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
