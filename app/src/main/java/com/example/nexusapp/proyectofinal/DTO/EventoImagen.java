package com.example.nexusapp.proyectofinal.DTO;

public class EventoImagen {
    private Usuario usuarioParticipante;
    private String imagenEvento;

    public String getImagenEvento() {
        return imagenEvento;
    }

    public void setImagenEvento(String imagenEvento) {
        this.imagenEvento = imagenEvento;
    }

    public Usuario getUsuarioParticipante() {
        return usuarioParticipante;
    }

    public void setUsuarioParticipante(Usuario usuarioParticipante) {
        this.usuarioParticipante = usuarioParticipante;
    }

    public EventoImagen() {
    }

    @Override
    public String toString() {
        return "EventoImagen{" +
                "usuarioParticipante=" + usuarioParticipante +
                ", imagenEvento='" + imagenEvento + '\'' +
                '}';
    }
}
