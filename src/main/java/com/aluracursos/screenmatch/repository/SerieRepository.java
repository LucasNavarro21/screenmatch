package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//JPARepository proporciona metodos para realizar operaciones crud
//Serie es el tipo de entidad con el que estara trabajando y long el tipo de dato del Id de la serie
public interface SerieRepository extends JpaRepository<Serie, Long> {

    //Esto se traduce como una consulta query en donde aplica camelCase
    //Buscar por el titulo ya que se aclara que tiene que ser del objeto serie
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    //Aca usamos una lista ya que siempre tiene que devolver un resultado
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria categoria);
    List<Serie> findByTotalDeTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int totalTemporadas, Double evaluacion);

        //IMPORTANTE
        //En esta query se toma el nombre del atributo en la clase, no del atributo en la tabla
        //los ":" se usan para diferenciar el nombre del atributo de la bd con la variable
        // Esto es query jqpl
        @Query("SELECT s FROM Serie s WHERE s.totalDeTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
        List<Serie> seriesPorTemporadaYEvaluacion(int totalTemporadas , Double evaluacion);

        @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
        List<Episodio> episodioPorNombre(String nombreEpisodio);

        //Trae todos los episodios de la serie seleccionada(:serie) y desoues los filtra
        @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
        List<Episodio> top5Episodios(Serie serie);

        @Query("SELECT s FROM Serie s " + "JOIN s.episodios e " + "GROUP BY s " + "ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 5")
        List<Serie> lanzamientosMasRecientes();

        @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numeroTemporada")
        List<Episodio> obtenerTemporadasPorNumero(Long id, Long numeroTemporada);
}
