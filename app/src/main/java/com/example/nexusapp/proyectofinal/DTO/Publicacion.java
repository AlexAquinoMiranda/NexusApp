package com.example.nexusapp.proyectofinal.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Publicacion es un objeto que almacena todos los datos de la publicación que realiza un usuario
 */
public class Publicacion implements Serializable {
    private String idUsuario, título, descripcion, urlImagen;
    private String userName;
    private String userImage;
    private String uid;
    private int like;




    private  ArrayList<Comentar> comentarios;

    public Map<String, Boolean> getDatosLike() {
        return datosLike;
    }

    public void setDatosLike(Map<String, Boolean> datosLike) {
        this.datosLike = datosLike;
    }

    private Map<String, Boolean> datosLike;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isMeGusta() {
        return meGusta;
    }

    public void setMeGusta(boolean meGusta) {
        this.meGusta = meGusta;
    }

    private boolean meGusta;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Comentar> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentar> comentarios) {
        this.comentarios = comentarios;
    }

    public Publicacion() {

        datosLike = new HashMap<>();
        comentarios = new ArrayList<>();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTítulo() {
        return título;
    }

    public void setTítulo(String título) {
        this.título = título;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public void addDatosLike(String k, boolean v) {
        this.datosLike.put(k, v);
    }


    @Override
    public String toString() {
        return "Publicacion{" +
                "idUsuario='" + idUsuario + '\'' +
                ", título='" + título + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", urlImagen='" + urlImagen + '\'' +
                ", userName='" + userName + '\'' +
                ", userImage='" + userImage + '\'' +
                ", uid='" + uid + '\'' +
                ", like=" + like +
                ", comentarios=" + comentarios +
                ", datosLike=" + datosLike +
                ", meGusta=" + meGusta +
                '}';
    }
}
