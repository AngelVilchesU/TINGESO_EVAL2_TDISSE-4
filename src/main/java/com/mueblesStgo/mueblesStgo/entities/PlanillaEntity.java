package com.mueblesStgo.mueblesStgo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity // Indica que corresponde a una entidad de persistencia
@Table(name = "Sueldo") // Nombre que adoptará la base de datos
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanillaEntity {
    @Id // Permite que la BD visualice el ID como tal
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generado automáticamente e incrementable
    @Column(unique = true, nullable = false) // Es único y no puede ser nulo
    private Long id;
    private String rutEmpleado;
    private String nombreApellido;
    private char categoria;
    private float aniosServicio;
    private float sueldoFijoMensual;
    private float montoBonificacionAniosServicio;
    private float pagoHorasExtra;
    private float descuentos;
    private float sueldoBruto;
    private float cotizacionPrevisional;
    private float cotizacionSalud;
    private float montoSueldoFinal;
    private LocalDate fecha;

    public PlanillaEntity(String rutEmpleado, String nombreApellido, char categoria, float aniosServicio, float sueldoFijoMensual, float montoBonificacionAniosServicio, float pagoHorasExtra, float descuentos, float sueldoBruto, float cotizacionPrevisional, float cotizacionSalud, float montoSueldoFinal, LocalDate fecha) {
        this.rutEmpleado = rutEmpleado;
        this.nombreApellido = nombreApellido;
        this.categoria = categoria;
        this.aniosServicio = aniosServicio;
        this.sueldoFijoMensual = sueldoFijoMensual;
        this.montoBonificacionAniosServicio = montoBonificacionAniosServicio;
        this.pagoHorasExtra = pagoHorasExtra;
        this.descuentos = descuentos;
        this.sueldoBruto = sueldoBruto;
        this.cotizacionPrevisional = cotizacionPrevisional;
        this.cotizacionSalud = cotizacionSalud;
        this.montoSueldoFinal = montoSueldoFinal;
        this.fecha = fecha;
    }
}
