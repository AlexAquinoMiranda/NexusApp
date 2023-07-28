package com.example.nexusapp.proyectofinal.DTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Usuario es un obejto que guarda los datos de todos los que se hayan registrado
 * y iniciar sesión con sus credenciales
 */
public class Usuario implements Serializable {
    //atributos del usuario
    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private String password;
    private String email;
    private String descripcion;
    private String perfil;
    private String uid;

    public Map<String, Boolean> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(Map<String, Boolean> seguidores) {
        this.seguidores = seguidores;
    }

    public Map<String, Boolean> seguidores;
    public int siguiendo;

    public int getSiguiendo() {
        return siguiendo;
    }

    public void setSiguiendo(int siguiendo) {
        this.siguiendo = siguiendo;
    }

    /**
     * Constructor por defecto.
     */
    public Usuario() {
        seguidores = new HashMap<>();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String username) {
        this.nombreUsuario = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void addSeguidor(String k, boolean v) {
        this.seguidores.put(k, v);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", perfil='" + perfil + '\'' +
                ", uid='" + uid + '\'' +
                ", seguidores=" + seguidores +
                ", siguiendo=" + siguiendo +
                '}';
    }
}
