package com.cineplex.system.model;

public class Peliculas {
    
    private int ID_Pelicula;
    private String Titulo;
    private String Genero;
    private int Duracion;
    private String Categoria;
    private String Director;
    private String Poster;
    
    public Peliculas(){
    }
    
    public Peliculas(int ID_Pelicula, String Titulo, String Genero, int Duracion,
            String Categoria, String Director, String Poster){
        
        this.ID_Pelicula = ID_Pelicula;
        this.Titulo = Titulo;
        this.Genero = Genero;
        this.Duracion = Duracion;
        this.Categoria = Categoria;
        this.Director = Director;
        this.Poster = Poster;
    }

    public int getID_Pelicula() {
        return ID_Pelicula;
    }

    public void setID_Pelicula(int ID_Pelicula) {
        this.ID_Pelicula = ID_Pelicula;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String Genero) {
        this.Genero = Genero;
    }

    public int getDuracion() {
        return Duracion;
    }

    public void setDuracion(int Duracion) {
        this.Duracion = Duracion;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String Director) {
        this.Director = Director;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String Poster) {
        this.Poster = Poster;
    }
}
