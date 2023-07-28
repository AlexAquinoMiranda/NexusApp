package com.example.nexusapp.proyectofinal.Firebase;

import java.util.ArrayList;

/**
 * {@link ICrudFirebase }interfaz genérico que recibe objeto para manipularlos en firebase  (Crear, leer, moidificar y eliminar)
 *
 * @param <T> T es el objeto que se va a manipular.
 */
public interface ICrudFirebase<T> {
    /**
     * método que recibe un objeto y lo agrega a firebase.
     *
     * @param t objeto a crear
     */
    public void create(T t);

    /**
     * método que elimina un objeto por su id.
     *
     * @param uid uid del objeto a eliminar
     */
    public void delete(String uid);

    /**
     * método para modificar un objeto, mantiene el mismo id por el cual reemplaza los datos.
     *
     * @param t   objeto a modificar
     * @param uid uid del objeto
     */
    public void modify(T t, String uid);

    /**
     * @param uuid uuid del objeto a buscar
     * @return objeto con id buscado
     */
    public T get(String uuid);

    /**
     * método que devuelve todos los objetos en una lista.
     *
     * @return lista llena de objetos <T>
     */
    public ArrayList<T> getAll();


}
