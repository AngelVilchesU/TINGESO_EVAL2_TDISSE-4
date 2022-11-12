package com.mueblesStgo.mueblesStgo.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mueblesStgo.mueblesStgo.models.DescuentoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DescuentoService {
    @Autowired
    RestTemplate restTemplate;

    public List<DescuentoModel> obtenerDescuentos() {
        String url = "http://localhost:8087/descuento";
        ResponseEntity<Object[]> response = restTemplate.getForEntity(url, Object[].class);
        Object[] records = response.getBody();
        if (records == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return Arrays.stream(records)
                .map(autorizacion -> mapper.convertValue(autorizacion, DescuentoModel.class))
                .collect(Collectors.toList());
    }

    public float[] obtenerCotizaciones(){
        List<DescuentoModel> descuentoEntities = obtenerDescuentos();
        float[] cotizaciones = new float[2];
        cotizaciones[0] = descuentoEntities.get(0).getCotizacionPrevisional();
        cotizaciones[1] = descuentoEntities.get(0).getCotizacionPlanSalud();
        return cotizaciones;
    }

    public LocalTime tiempoNoTrabajo(LocalTime tiempoTrabajo){
        List<DescuentoModel> descuentoModelList = obtenerDescuentos();
        LocalTime tiempoFaltante = descuentoModelList.get(0).getTiempoTrabajo().minusHours(tiempoTrabajo.getHour());
        tiempoFaltante = tiempoFaltante.minusMinutes(tiempoTrabajo.getMinute());
        tiempoFaltante = tiempoFaltante.minusSeconds(tiempoTrabajo.getSecond());
        return tiempoFaltante;
    }

    public List<Float> descuento(LocalTime tiempoTrabajoFaltante){
        List<DescuentoModel> descuentoModelList = obtenerDescuentos();
        List<Float> descuentoApelar = new ArrayList<>();
        float porcentajeMinimoDescuento = 0;
        float puedeApelar = 0; // 0 para no, 1 para si
        int aux;
        int i = 0;
        for(i = 0; i < descuentoModelList.size(); i++){
            aux = tiempoTrabajoFaltante.compareTo(descuentoModelList.get(i).getTiempoRetraso());
            if(aux < 0){ // Si tiempoTrabajoFaltante < tiempoRetraso entonces...
                if(i == 0){ // No hay descuento
                    descuentoApelar.add(porcentajeMinimoDescuento);
                    descuentoApelar.add(puedeApelar);
                    return descuentoApelar;
                }
                descuentoApelar.add(descuentoModelList.get(i - 1).getMontoDescuento());
                descuentoApelar.add(puedeApelar);
                return descuentoApelar;
            }
        }
        descuentoApelar.add(descuentoModelList.get(i - 1).getMontoDescuento()); // descuento maximo
        puedeApelar = puedeApelar + 1; // puede apelar
        descuentoApelar.add(puedeApelar);
        return descuentoApelar;
    }

    public float aplicacionDescuentos(float sueldo, float porcentajeDescuento){
        float minimo = 0;
        float maximo = 100;
        if (porcentajeDescuento == minimo){
            return sueldo;
        }
        else if (porcentajeDescuento >= maximo){
            return 0;
        }
        return sueldo - (porcentajeDescuento * sueldo) / 100;
    }
}
