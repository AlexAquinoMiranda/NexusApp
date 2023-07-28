package com.example.nexusapp.proyectofinal.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Evento implements Serializable {

    private String idEvento;
    private String nombreEvento;
    private String usuarioCreador;
    private String descripcionEvento;
    private List<Usuario> participantes;
    public static ArrayList<Evento> eventos;
    private String portada;
    private List<EventoImagen> imagenes;

    public Evento() {

        this.participantes = new ArrayList<>();
        imagenes = new ArrayList<>();
    }

    public String getNombreEvento() {
        return nombreEvento;
    }


    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public List<EventoImagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<EventoImagen> imagenes) {
        this.imagenes = imagenes;
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }

    public String getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public List<Usuario> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Usuario> participantes) {
        this.participantes = participantes;
    }


    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    /**
     * metodo que recibe un usuario y lo agrega en lista de participantes
     *
     * @param user
     */
    public void agregarParticipante(Usuario user) {
        this.participantes.add(user);
    }

    /**
     * método que añade un objeto de tipo EventoImagen en donde se guarda datos del usuario y la imagen que sube
     * @param user usuario que sube la imagen
     * @param imagen imagen a subir
     */
    public void addImagen(Usuario user, String imagen) {
        EventoImagen evento = new EventoImagen();
        evento.setImagenEvento(imagen);
        evento.setUsuarioParticipante(user);
        this.imagenes.add(evento);
    }

    @Override
    public String toString() {
        return "Evento{" +
                "idEvento='" + idEvento + '\'' +
                ", nombreEvento='" + nombreEvento + '\'' +
                ", usuarioCreador='" + usuarioCreador + '\'' +
                ", descripcionEvento='" + descripcionEvento + '\'' +
                ", participantes=" + participantes +
                ", portada='" + portada + '\'' +
                ", imagenes=" + imagenes +
                '}';
    }
}
