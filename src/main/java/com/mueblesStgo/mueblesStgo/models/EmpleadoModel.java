package com.mueblesStgo.mueblesStgo.models;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmpleadoModel {
    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento; // Ejemplo de tipo de dato LocalDate: 2022-10-10
    private LocalDate fechaIngresoEmpresa;
    private char categoria;
}
