package com.aluracursos.screenmatch.model;

public enum Categoria {


    //IMPORTANTE
    //Las clases enum son especiales ya que no requieren que se instancien
    //Sus constantes se crean automaticamente y ya estan disponibles

    //Una clase enum define un conjunto fijo de constantes,
    // en este caso, las categorías o géneros posibles de una serie.
    // La enumeración Categoria se utiliza para representar los géneros de una manera controlada y estructurada.
    ACCION("Action", "Acción"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    DRAMA("Drama", "Drama"),
    CRIMEN("Crime", "Crimen");


    private String categoriaOmdb;
    private String categoriaEspanol;


    //Cuando se define una constante tipo comedy, comedia, se llama al constructor
    //Comedy va para categoriaomdb y comedia para categoriaespanol
    Categoria(String categoriaOmdb, String categoriaEspanol){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEspanol = categoriaEspanol;
    }
    //Permite buscar y devolver una instancia de Categoria basada en el
    // texto recibido. Si el texto coincide con uno de los valores definidos
    // en el enum, se devuelve esa instancia; de lo contrario, se lanza una excepción.

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

    public static Categoria fromEspanol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
