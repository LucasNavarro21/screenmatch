//package com.aluracursos.screenmatch.service;
//
//import com.aluracursos.screenmatch.principal.Principal;
//import com.aluracursos.screenmatch.repository.SerieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//
//@SpringBootApplication
//
////Al implementar esta interfaz el metodo run se ejecutara cuando aplicacion inicie como codigo de
////inicializacion
//
//public class ScreenmatchApplicationConsola implements CommandLineRunner {
//
//	//Esta anotacion genera una inyeccion de dependencias, osea nos permite usar repo
//	@Autowired
//	private SerieRepository repository;
//
//	public static void main(String[] args) {
//		SpringApplication.run(ScreenmatchApplicationConsola.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//
//		//Se pasa al constructor el repository para que la clase principal pueda usar sus metodos
//		Principal principal = new Principal(repository);
//		principal.muestraElMenu();
//	}
//}
