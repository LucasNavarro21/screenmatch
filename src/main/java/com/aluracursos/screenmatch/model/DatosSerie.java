package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//El json alias en este contexto busca en el json el alias pasado como argumento y lo asocia con el
// String
//Ademas del json alis que solo es de lecturam esta el json property el cual ademas de permitir leer
//tambien permite escribir, osea enviar informacion

//JsonIgnoreProperties ignora las propiedades que devuelve el json que no esten mencionadas en el record
//Por lo que nos evita errores
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(@JsonAlias("Title") String titulo, @JsonAlias("totalSeasons")  Integer totalTemporadas, @JsonAlias("imdbRating") String evaluacion, @JsonAlias("Genre") String genero, @JsonAlias("Plot") String sinopsis, @JsonAlias("Poster") String poster, @JsonAlias("Actors") String actores) {
}
