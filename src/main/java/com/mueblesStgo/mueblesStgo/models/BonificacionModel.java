package com.mueblesStgo.mueblesStgo.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BonificacionModel {
    private Long id;
    private float aniosServicio;
    private float bono;
}
