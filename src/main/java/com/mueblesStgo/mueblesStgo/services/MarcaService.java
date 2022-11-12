package com.mueblesStgo.mueblesStgo.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mueblesStgo.mueblesStgo.models.MarcaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarcaService {
    @Autowired
    RestTemplate restTemplate;

    public List<MarcaModel> obtenerMarcas() {
        String url = "http://localhost:8082/marca";
        ResponseEntity<Object[]> response = restTemplate.getForEntity(url, Object[].class);
        Object[] records = response.getBody();
        if (records == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return Arrays.stream(records)
                .map(autorizacion -> mapper.convertValue(autorizacion, MarcaModel.class))
                .collect(Collectors.toList());
    }
}
