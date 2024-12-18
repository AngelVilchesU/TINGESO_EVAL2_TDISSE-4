package com.mueblesStgo.mueblesStgo.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mueblesStgo.mueblesStgo.models.BonificacionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BonificacionService {
    @Autowired
    RestTemplate restTemplate;

    public List<BonificacionModel> obtenerBonificaciones() {
        String url = "http://localhost:8087/bonificacion";
        ResponseEntity<Object[]> response = restTemplate.getForEntity(url, Object[].class);
        Object[] records = response.getBody();
        if (records == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return Arrays.stream(records)
                .map(autorizacion -> mapper.convertValue(autorizacion, BonificacionModel.class))
                .collect(Collectors.toList());
    }

    public float bonificacionAniosServicio(float aniosServicio){
        int i;
        List<BonificacionModel> bonificacionModelList = obtenerBonificaciones();
        for (i = 0; i < bonificacionModelList.size(); i++){
            if(aniosServicio < bonificacionModelList.get(i).getAniosServicio()){
                if (i == 0){
                    return 0;
                }
                else {
                    return bonificacionModelList.get(i - 1).getBono();
                }
            }
        }
        return bonificacionModelList.get(i - 1).getBono();
    }

    public float sueldoBonificacionPorcentual(float sueldo, float bPorcentual){
        float bPorcentajeDecimal = (bPorcentual + 100) / 100; // Conversión de porcentaje a decimal
        return sueldo * bPorcentajeDecimal - sueldo;
    }

}
