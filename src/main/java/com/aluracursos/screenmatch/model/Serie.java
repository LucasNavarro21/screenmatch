package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;


@Entity //le indicamos que va a ser una entidad de la tabla de datos
@Table(name = "series") //Con esto seteamos como va a ser el nombre de la tabla en la bd, si no se
//se especifica por defecto queda el nombre de la clase
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true) //Indicamos que esta columna no puede tener registros repetidos
    private String titulo;
    private Integer totalDeTemporadas;
    private Double evaluacion;

    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String sinopsis;
    private String poster;
    private String actores;


    //Declara el tipo de relacion que tiene con la tabla episodio
    //En este caso 1 serie puede contener varios episodios
    //La funcion del cascade es que cada vez que haya cambios en serie, tambien lo haya en episodio
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }

    public void setTotalDeTemporadas(Integer totalDeTemporadas) {
        this.totalDeTemporadas = totalDeTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        //Se indica para cada uno de los episodios el id correspondiente
        //El this hace referencia al valor de esta misma
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    //Agrego este constructor ya hibernate exige un constructor predeterminado
    public Serie(){}
    //el constructor de la clase Serie acepta
    // un objeto de tipo DatosSerie y asigna sus valores a los atributos de la clase Serie

    //Utiliza OptionalDouble para manejar el valor de evaluacion
    // en caso de que sea nulo o no esté presente, asignando un valor por defecto de 0 si no existe.
    public Serie(DatosSerie datosSerie){
        this.titulo = datosSerie.titulo();
        this.totalDeTemporadas = datosSerie.totalTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        this.sinopsis = datosSerie.sinopsis();
        this.poster = datosSerie.poster();
        this.actores = datosSerie.actores();

    }

    @Override
    public String toString() {
        return
                "genero= " + genero +
                " titulo='" + titulo + '\'' +
                ", totalDeTemporadas=" + totalDeTemporadas +
                ", evaluacion=" + evaluacion +
                ", sinopsis='" + sinopsis + '\'' +
                ", poster='" + poster + '\'' +
                ", actores='" + actores + '\'' +
                ", episodios='" + episodios + '\''
                ;
    }
}
