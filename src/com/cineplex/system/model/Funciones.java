package com.cineplex.system.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

/**
 * Una funcion (horario de proyeccion) ya con la pelicula y la sala
 * resueltas -- viene de sp_Obtener_Funciones / sp_Buscar_Funcion_Por_Titulo,
 * que hacen el JOIN. Faltaba esta clase: FuncionRepository ya la
 * importaba pero el archivo no existia, asi que el proyecto no
 * compilaba.
 */
public class Funciones {

    private int ID_Funcion;
    private Date fecha;
    private Time hora;
    private BigDecimal precio;

    private int ID_Pelicula;
    private String titulo;
    private String genero;
    private int duracion;
    private String categoria;
    private String poster;

    private int ID_Sala;
    private String nombreSala;
    private int filas;
    private int columnas;

    public Funciones() {
    }

    public int getID_Funcion() {
        return ID_Funcion;
    }

    public void setID_Funcion(int ID_Funcion) {
        this.ID_Funcion = ID_Funcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getID_Pelicula() {
        return ID_Pelicula;
    }

    public void setID_Pelicula(int ID_Pelicula) {
        this.ID_Pelicula = ID_Pelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getID_Sala() {
        return ID_Sala;
    }

    public void setID_Sala(int ID_Sala) {
        this.ID_Sala = ID_Sala;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }
}
