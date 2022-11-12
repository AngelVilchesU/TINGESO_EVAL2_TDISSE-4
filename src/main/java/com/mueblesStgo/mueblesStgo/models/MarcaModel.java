package com.mueblesStgo.mueblesStgo.models;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarcaModel {
    private Long id;
    private LocalDate fecha; // Ejemplo de tipo de dato LocalDate: 2022-09-10
    private LocalTime horaIngresoSalida; // Ejemplo de tipo de dato LocalTime: 15:40:00
    private String rutEmpleado;

    public MarcaModel(LocalDate fecha, LocalTime horaIngresoSalida, String rutEmpleado) {
        this.fecha = fecha;
        this.horaIngresoSalida = horaIngresoSalida;
        this.rutEmpleado = rutEmpleado;
    }
}
