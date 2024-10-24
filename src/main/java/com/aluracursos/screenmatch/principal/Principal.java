package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    ConvierteDatos conversor = new ConvierteDatos();
    ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String base_url = "https://www.omdbapi.com/?t=";
    private final String apikey = "&apikey=fb96295a";
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private List<Serie> series;
    Optional<Serie> serieBuscada;

    private SerieRepository repository;

    //Haciendo esto podemos acceder a los metodos de la inteface repostorio
    //Colocando el constructor de la clase repository permitimos que la clase tenga
    // acceso a los metodos de la interfaz repository.
    //Esto es una forma de inyeccion de dependencias, cuando se crea una instancia de principal
    //Tambien se crea una instancia de SerieRepository
    public Principal(SerieRepository repository) {
        this.repository = repository;
    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3-  Mostrar series buscadas
                    4-  Buscar serie por titulo     
                    5-  Top 5 mejores Series   
                    6-  Bucar serie por categoria    
                    7-  Buscar serie por temporada y evaluacion 
                    8-  Buscar episodio por titulo
                    9-  Mostrar top 5 episodios
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriePorCategoria();
                    break;
                case 7:
                    buscarSeriePorCategoriaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodioPorTitulo();
                    break;
                case 9:
                    top5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void buscarEpisodioPorSerie(){
        mostrarSeriesBuscadas();
        System.out.println("Escriba el nombre de la serie que desea ver");
        var nombreSerie = teclado.nextLine();
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if (serie.isPresent()){
            var serieBuscada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieBuscada.getTotalDeTemporadas(); i++) {
                var json = consumoAPI.obtenerDatos(base_url + serieBuscada.getTitulo().replace(" ", "+") + "&season=" + i + apikey);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieBuscada.setEpisodios(episodios);
            repository.save(serieBuscada);
        }

    }


    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(base_url + nombreSerie.replace(" ", "+") + apikey);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }


    private void buscarSerieWeb(){
        DatosSerie datos = getDatosSerie();
//        datosSeries.add(datos);
        Serie serie = new Serie(datos);
        repository.save(serie);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        //Find all trae todo lo de la lista

        series = repository.findAll();
//        datosSeries.forEach(System.out::println);

//        List<Serie> series = new ArrayList<>();
//
//        //Se asigna a la variable al arraylist los datos obtenidos de datosSeries y se mapea cada
//        //dato a un nuevo objeto serie
//        series = datosSeries.stream().map(d -> new Serie(d)).collect(Collectors.toList());
//
//        //De los datos obtenidos en el anterios stream se ordenan con sorted en base al genero
//        //Y se imprimen al final con el foreach.
        series.stream().sorted(Comparator.comparing(Serie::getGenero)).
                forEach(System.out::println);


    }

    private void buscarSeriesPorTitulo() {

        System.out.println("Ingrese el nombre de la serie");
        var nombreSerie = teclado.nextLine();
        //Uso optional ya que maneja la posibilidad de que el resultado este vacio o no
        //Si recibo algo, debe ser un tipo de dato de la clase Serie
        serieBuscada = repository.findByTituloContainsIgnoreCase(nombreSerie);

        if(serieBuscada.isPresent()){
            System.out.println("La seria es: " + serieBuscada.get());
        }else{
            System.out.println("No se encontro la serie");
        }

    }
    private void buscarTop5Series(){
        List<Serie> topSeries = repository.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " evaluacion: " + s.getEvaluacion()));
    }

    private void buscarSeriePorCategoria(){
        System.out.println("Escriba el genero/categoria que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Las series de la categoria " + genero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriePorCategoriaYEvaluacion(){
        System.out.println("Ingrese la cantidad de temporadas");
        var temporadas = teclado.nextInt();
        System.out.println("Ingrese la valoracion");
        var evaluacion = teclado.nextDouble();
        List<Serie> serieCategoriaYEvaluacion = repository.seriesPorTemporadaYEvaluacion(temporadas, evaluacion);
        System.out.println("Los resultados son: ");
        serieCategoriaYEvaluacion.forEach(s -> System.out.println( s.getTitulo() + " - evaluacion: " + s.getEvaluacion()));
    }

    private void buscarEpisodioPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodioPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf(
                "Serie: %s - Episodio: %s (Temporada %d, Episodio %d) Evaluación: %s\n",
                e.getSerie().getTitulo(), e.getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));

    }
    private void top5Episodios(){
        buscarSeriesPorTitulo();
        if(serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            //Serie tiene el valor de la serie seleccionada
            List<Episodio> topEpisodios = repository.top5Episodios(serie);
            topEpisodios.forEach(e -> System.out.printf(
                    "Serie: %s - Episodio: %s (Temporada %d, Episodio %d) Evaluación: %s%n",
                    e.getSerie().getTitulo(), e.getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));

        }

    }

}

