package com.aluracursos.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos{

    //ObjectMapper es una clase proporcionada por Jackson que se
    // encarga de mapear JSON a objetos Java y viceversa.

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    //T es un tipo de dato generico, su funcion es que un metodo funcione sin aclarar de antemano
    //cual sera el tipo exacto por lo que lo hace flexible, en este caso t representara el tipo exacto
    //cuando se llame al metodo

    //Este metodo indica que es generico anteponiento la t
    //String json es el contenido json que se va a convertir en un objeto java
    //Class t especifica la clase en la que se quiere convertir el json
    //Se utiliza para indicarle a ObjectMapper el tipo de objeto que debe crear a partir del JSON.

    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            //readvalue estimo que es un metodo para leer el valor de los parametros asignados.
            return objectMapper.readValue(json,clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
