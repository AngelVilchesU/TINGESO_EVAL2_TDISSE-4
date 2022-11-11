package com.mueblesStgo.mueblesStgo.models;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JustificativoModel {
    private Long id;
    private LocalDate fechaInasistencia;
    private String rutEmpleado;
}
