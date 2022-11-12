package com.mueblesStgo.mueblesStgo.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mueblesStgo.mueblesStgo.models.JustificativoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JustificativoService {
    @Autowired
    RestTemplate restTemplate;

    public List<JustificativoModel> obtenerJustificativos() {
        String url = "http://localhost:8084/justificativo";
        ResponseEntity<Object[]> response = restTemplate.getForEntity(url, Object[].class);
        Object[] records = response.getBody();
        if (records == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return Arrays.stream(records)
                .map(autorizacion -> mapper.convertValue(autorizacion, JustificativoModel.class))
                .collect(Collectors.toList());
    }

    public boolean estaJustificado(LocalDate fechaInasistencia, String rutEmpleado){
        List<JustificativoModel> justificativoModels = obtenerJustificativos();
        for (int i = 0; i < justificativoModels.size(); i++){
            if (justificativoModels.get(i).getRutEmpleado().equals(rutEmpleado) &&
                    justificativoModels.get(i).getFechaInasistencia().equals(fechaInasistencia)){
                return true;
            }
        }
        return false;
    }
}
