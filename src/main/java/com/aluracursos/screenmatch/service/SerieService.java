package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Marca esta clase como un servicio por lo que maneja la logica de negocio, osea, los metodos relacionados
//a las series
@Service
public class SerieService {

    @Autowired
    //Inyecta el repositorio SerieRepository, que es una capa de acceso a la base de datos.
    private SerieRepository repository;

    public List<SerieDTO> obtenerTodasLasSeries(){
        return convierteDatos(repository.findAll());
    }


    public List<SerieDTO> obtenerTop5() {
        return convierteDatos(repository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> obtenerLanzamientosMasRecientes(){
        return convierteDatos(repository.lanzamientosMasRecientes());
    }

    public SerieDTO obtenerPorId(Long Id){
        Optional<Serie> serie = repository.findById(Id);
        if(serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),s.getTitulo(), s.getTotalDeTemporadas(), s.getEvaluacion(), s.getPoster(), s.getGenero(),
                    s.getActores(), s.getSinopsis());
        }
        return null;
    }

    public List<EpisodioDTO> obtenerTodasLasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);
        if(serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream().map(e -> new EpisodioDTO(e.getTemporada(),
                    e.getTitulo(), e.getNumeroEpisodio())).collect(Collectors.toList());
        }
        return null;

    }


    public List<EpisodioDTO> obtenerTemporadasPorNumero(Long id, Long numeroTemporada) {
        return repository.obtenerTemporadasPorNumero(id, numeroTemporada).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(),
                        e.getTitulo(), e.getNumeroEpisodio())).collect(Collectors.toList());
    }


    public List<SerieDTO> obtenerSeriePorCategoria(String nombreGenero) {
        Categoria categoria = Categoria.fromEspanol(nombreGenero);
        return convierteDatos(repository.findByGenero(categoria));
    }

    //Recibe una lista de objetos de tipo seriea los cuales convierte en objetos de serieDTO
    public List<SerieDTO> convierteDatos(List<Serie> serie){
        return  serie.stream()
                . map(s -> new SerieDTO(s.getId() ,s.getTitulo(), s.getTotalDeTemporadas(), s.getEvaluacion(), s.getPoster(), s.getGenero(),
                        s.getActores(), s.getSinopsis())).collect(Collectors.toList());
    }

}
