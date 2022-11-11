package com.mueblesStgo.mueblesStgo.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriaModel {
    private Long id;
    private char categoria;
    private float sueldoFijoMensual;
    private float montoPorHora;
}
