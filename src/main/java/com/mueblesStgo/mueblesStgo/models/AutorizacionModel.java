package com.mueblesStgo.mueblesStgo.models;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AutorizacionModel {
    private Long id;
    private LocalDate fechaHoraExtra; // Ejemplo de tipo de dato LocalDate: 2022-09-11
    private String rutEmpleado;
    private float horasExtra;
}
