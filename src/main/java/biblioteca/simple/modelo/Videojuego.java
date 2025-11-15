package biblioteca.simple.modelo;

import biblioteca.simple.contratos.Prestable;

public class Videojuego extends Producto implements Prestable {
    //Atributos propios de un Videojuego
    private Categoria categoria;
    private Plataforma plataforma;
    private String PEGI;

    //Control del estado del prestamo
    private boolean prestado;
    private Usuario prestadoA;

    //Constructor usado cuando el objeto proviene de una base de datos(ya tiene un id asignado)
    public Videojuego(int id, String titulo, String anho, Formato formato, Categoria categoria, Plataforma plataforma, String PEGI, boolean prestado, Usuario prestadoA) {
        super(id, titulo, anho, formato);
        this.categoria = categoria;
        this.plataforma = plataforma;
        this.PEGI = PEGI;
        this.prestado = prestado;
        this.prestadoA = prestadoA;
    }

    // Constructor para crear videojuegos nuevos (sin id inicial)
    public Videojuego(String titulo, String anho, Formato formato, Categoria categoria, Plataforma plataforma, String PEGI, boolean prestado, Usuario prestadoA) {
        super(titulo, anho, formato);
        this.categoria = categoria;
        this.plataforma = plataforma;
        this.PEGI = PEGI;
        this.prestado = prestado;
        this.prestadoA = prestadoA;
    }
    // Métodos getter para acceder a los atributos propios
        // Obtiene la categoría del videojuego.
    public Categoria getCategoria() {
        return categoria;
    }
        // Obtiene la Plataforma donde se juega el videojuego.
    public Plataforma getPlataforma() {
        return plataforma;
    }
        // Obtiene la clasificación por edad del videojuego.
    public String getPEGI() {
        return PEGI;
    }
    // Implementamos los métodos de lla interfaz prestable
    @Override public void prestar(Usuario u) {
        if (prestado) throw new IllegalStateException("El videojuego ya está prestado");
        prestado = true;
        this.prestadoA = u;
    }

    // Devuelve el Videojuego (lo marca como no prestado)
    @Override
    public void devolver() {
        this.prestado = false;
        this.prestadoA = null;
    }

    // Comprueba si está prestado
    @Override
    public boolean estaPrestado() {
        return this.prestado;
    }

    // Sobrescribimos toString() para representar toda la información
    // de Videojuego en forma de texto (útil al imprimir por consola)
    @Override
    public String toString() {
        return "Videojuego{" +
                "categoria=" + categoria +
                ", plataforma=" + plataforma +
                ", PEGI='" + PEGI + '\'' +
                ", prestado=" + prestado +
                ", prestadoA=" + prestadoA +
                ", id=" + id +
                ", titulo='" + titulo + '\'' +
                ", anho='" + anho + '\'' +
                ", formato=" + formato +
                '}';
    }
}
