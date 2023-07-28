package com.example.nexusapp.proyectofinal.DTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Historia es un objeto que guarda los datos cuando un usuario publique una historia
 */
public class Historia implements Serializable {
    private String uidHistoria, idUsuario, usuarioNombre, perfilUsuario;
    private Long tiempoInicio, tiempoFin;

    public Map<String, Long> getFotos() {
        return fotos;
    }

    public void setFotos(Map<String, Long> fotos) {
        this.fotos = fotos;
    }

    private Map<String, Long> fotos;

    public String getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(String perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }

    public Historia() {
        this.fotos = new HashMap<>();
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUidHistoria() {
        return uidHistoria;
    }

    public void setUidHistoria(String uidHistoria) {
        this.uidHistoria = uidHistoria;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getTiempoFin() {
        return tiempoFin;
    }

    public void setTiempoFin(Long tiempoFin) {
        this.tiempoFin = tiempoFin;
    }

    public Long getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(Long tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public void addFoto(String photoId, Long tiempoFin) {
        fotos.put(photoId, tiempoFin);
    }

    @Override
    public String toString() {
        return "Historia{" +
                "uidHistoria='" + uidHistoria + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", usuarioNombre='" + usuarioNombre + '\'' +
                ", perfilUsuario='" + perfilUsuario + '\'' +
                ", tiempoInicio=" + tiempoInicio +
                ", tiempoFin=" + tiempoFin +
                ", fotos=" + fotos +
                '}';
    }
}
