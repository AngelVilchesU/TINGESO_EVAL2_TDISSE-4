package com.mueblesStgo.mueblesStgo.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mueblesStgo.mueblesStgo.models.AutorizacionModel;

@Service
public class AutorizacionService {

    @Autowired
    RestTemplate restTemplate;

    public List<AutorizacionModel> obtenerAutorizaciones() {
        String url = "http://localhost:8085/autorizacion";
        ResponseEntity<Object[]> response = restTemplate.getForEntity(url, Object[].class);
        Object[] records = response.getBody();
        if (records == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return Arrays.stream(records)
                .map(autorizacion -> mapper.convertValue(autorizacion, AutorizacionModel.class))
                .collect(Collectors.toList());
    }

    public boolean tieneAutorizacion(LocalDate fechaHorasExtra, String rutEmpleado){
        List<AutorizacionModel> autorizacionModels = obtenerAutorizaciones();
        for (int i = 0; i < autorizacionModels.size(); i++){
            if(autorizacionModels.get(i).getFechaHoraExtra().equals(fechaHorasExtra) &&
                    autorizacionModels.get(i).getRutEmpleado().equals(rutEmpleado)){
                return true;
            }
        }
        return false;
    }

    public float horasExtra(LocalDate fechaHorasExtra, String rutEmpleado){
        List<AutorizacionModel> autorizacionModels = obtenerAutorizaciones();
        for (int i = 0; i < autorizacionModels.size(); i++){
            if(autorizacionModels.get(i).getFechaHoraExtra().equals(fechaHorasExtra) &&
                    autorizacionModels.get(i).getRutEmpleado().equals(rutEmpleado)){
                return autorizacionModels.get(i).getHorasExtra();
            }
        }
        return 0;
    }
}
