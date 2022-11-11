package com.mueblesStgo.mueblesStgo.models;

import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DescuentoModel {
    private Long id;
    private LocalTime tiempoTrabajo;
    private LocalTime tiempoRetraso;
    private float montoDescuento;
    private float cotizacionPrevisional;
    private float cotizacionPlanSalud;
}
