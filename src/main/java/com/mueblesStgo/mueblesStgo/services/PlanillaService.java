package com.mueblesStgo.mueblesStgo.services;

import com.mueblesStgo.mueblesStgo.entities.PlanillaEntity;
import com.mueblesStgo.mueblesStgo.models.*;
import com.mueblesStgo.mueblesStgo.repositories.PlanillaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class PlanillaService {

    @Autowired
    PlanillaRepository planillaRepository;
    @Autowired
    AutorizacionService autorizacionService;
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    BonificacionService bonificacionService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    JustificativoService justificativoService;
    @Autowired
    DescuentoService descuentoService;
    @Autowired
    MarcaService marcaService;

    private List<MarcaModel> marcaModelList = new ArrayList<>(); // Arreglo por concepto de lectura de contenido
    private int diasDelMes;
    private int mesEvaluado;
    private int anioEvaluado;

    public List<PlanillaEntity> obtenerSueldo(){
        return (List<PlanillaEntity>) planillaRepository.findAll();
    }

    public PlanillaEntity guardarSueldo(PlanillaEntity sueldo){
        return planillaRepository.save(sueldo);
    }

    public boolean esDiaDeSemana(int dia, int mes, int anio){
        DayOfWeek diaSemana = LocalDate.of(anio, mes, dia).getDayOfWeek();
        new Locale("es", "ES");
        String diaSemanaStr = diaSemana.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-ES"));
        if (diaSemanaStr.equals("sábado") || diaSemanaStr.equals("domingo")){
            return false;
        }
        return true;
    }

    public boolean esBisiesto(float anio){
        anio = anio / 4;
        if ((anio % 1) == 0){
            return true;
        }
        return false;
    }

    public int diasDelMes(int mes, int anio){
        if ((mes == 1) || (mes == 3) || (mes == 5) || (mes == 7) || (mes == 8) || (mes == 10) || (mes == 12)){
            return 31;
        }
        else if (mes == 2){
            if (esBisiesto(anio)){
                return 29;
            }
            else {
                return 28;
            }
        }
        else {
            return 30;
        }
    }

    public String calculoPlanillas(){
        marcaModelList = marcaService.obtenerMarcas();
        List<EmpleadoModel> empleados = empleadoService.obtenerEmpleados(); // Se obtienen todos los empleados
        List<CategoriaModel> categorias = categoriaService.obtenerCategorias();
        List<BonificacionModel> bonificaciones = bonificacionService.obtenerBonificaciones();
        int lastElementMarcas = marcaModelList.size();
        diasDelMes = diasDelMes(marcaModelList.get(lastElementMarcas - 1).getFecha().getMonthValue(), marcaModelList.get(lastElementMarcas - 1).getFecha().getYear());
        mesEvaluado = marcaModelList.get(lastElementMarcas - 1).getFecha().getMonthValue();
        anioEvaluado = marcaModelList.get(lastElementMarcas - 1).getFecha().getYear();
        for (int j = 0; j < empleados.size(); j++){ // Mientras no se haya evaluado a cada usuario
            String rutEmpleado = empleados.get(j).getRut(); // Se extrae el rut del empleado "actual"
            String nombreApellido = empleados.get(j).getNombre().concat(" " + empleados.get(j).getApellido()); // Se extrae y concatena nombre y apellido del empleado "actual"
            char categoria = empleados.get(j).getCategoria(); // Se extrae la categoria del empleado "actual"
            float anioIngreso = empleados.get(j).getFechaIngresoEmpresa().getYear();
            float aniosServicio = anioEvaluado - anioIngreso; // Se extrae el año de ingreso a la empresa calculando los años trabajados
            float sueldoFijoMensual = 0;
            for (int h = 0; h < categorias.size(); h++) {
                if (categorias.get(h).getCategoria() == empleados.get(j).getCategoria()) {
                    sueldoFijoMensual = categorias.get(h).getSueldoFijoMensual(); // Se extrae el sueldo fijo mensual de acuerdo con la categoria
                }
            }
            float montoBonificacionAniosServicio = bonificacionService.bonificacionAniosServicio(aniosServicio); // Se extrae la bonificación de años de servicio
            float sueldoBruto = sueldoFijoMensual; // Se define tempranamente el sueldo bruto (sin considerar beneficios aún)
            float cotizacionPrevisional = descuentoService.obtenerCotizaciones()[0]; // Se extrar el porcentaje de descuento figurado por la cotización previsional
            float cotizacionSalud = descuentoService.obtenerCotizaciones()[1]; // Se extrar el porcentaje de descuento figurado por la cotización salud
            float montoSueldoFinal = 0; // Se define tempranamente el monto del sueldo final
            float descuentos = 0; // Porcentaje de descuento inicializado en 0
            float horasExtra = 0; // Horas extras inicializadas en 0
            float pagoHorasExtra = 0; // Pago por concepto de horas extra inicializado en 0
            float pagoAniosServicio = 0; // Pago por años de servicio inicializado en 0
            int i = 1;
            for (i = 1; i <= diasDelMes; i++){ // Mientras no se evaluen los dias respectivos del mes considerado
                if(esDiaDeSemana(i, mesEvaluado, anioEvaluado)){ // Si la fecha a evaluar responde a un día de semana (laboral)
                LocalDate fechaEvaluada = LocalDate.of(anioEvaluado, mesEvaluado, i);
                LocalTime horaInicio = marcaModelList.get(0).getHoraIngresoSalida(); // Se inicializa la variable
                LocalTime horaSalida;
                int finTurno = 0; // su maximo es 2, la entrada (1) y salida (2)
                for (int k = 0; k < marcaModelList.size(); k++){ // Mientras no se evaluen todas las marcas de reloj
                    if(marcaModelList.get(k).getRutEmpleado().equals(empleados.get(j).getRut())
                        && marcaModelList.get(k).getFecha().equals(fechaEvaluada)){ // Si el rut y fecha evaluadas responde a la marca de reloj "actual"
                        finTurno = finTurno + 1; //
                        if(finTurno == 1) { // Si responde a horario de entrada
                            horaInicio = marcaModelList.get(k).getHoraIngresoSalida(); // Se extrae la primera hora de la marca de acuerdo al rut y fecha "actual"
                        }
                        else if (finTurno == 2){ // Si responde a horario de salida/se completa el horario
                            horaSalida = marcaModelList.get(k).getHoraIngresoSalida(); // Se extrae la segunda hora de la marca de acuerdo al rut y fecha "actual"
                            horaSalida = horaSalida.minusHours(horaInicio.getHour()); // Se resta la hora de salida con la de entrada (solo horas)
                            horaSalida= horaSalida.minusMinutes(horaInicio.getMinute()); // Se resta la hora de salida con la de entrada (solo minutos)
                            LocalTime tiempoFaltanteTrabajo = descuentoService.tiempoNoTrabajo(horaSalida); // Se define el tiempo (horas) de trabajo faltante
                            // Referente a descuentos
                            if(descuentoService.descuento(tiempoFaltanteTrabajo).get(1) != 1.0){ // No puede justificar su ausencia
                                descuentos = descuentos + descuentoService.descuento(tiempoFaltanteTrabajo).get(0); // Figura un descuento
                            }
                            else { // Puede justificar su ausencia
                                if(!justificativoService.estaJustificado(fechaEvaluada, rutEmpleado)){ // No está justificado
                                    descuentos = descuentos + descuentoService.descuento(tiempoFaltanteTrabajo).get(0); // Figura un descuento
                                }
                            }
                            // Referente a bonificaciones por concepto de horas extras
                            if(autorizacionService.tieneAutorizacion(fechaEvaluada, rutEmpleado)){ // Posee autorización asociada a fecha y rut
                                horasExtra = horasExtra + autorizacionService.horasExtra(fechaEvaluada, rutEmpleado); // Se suman horas extras
                            }
                        }
                    }
                }
            }
        }
        pagoHorasExtra = pagoHorasExtra + categoriaService.pagoHorasExtra(horasExtra, categoria); // Se calcula el pago de horas extra de acuerdo a las horas y categoria
        pagoAniosServicio = bonificacionService.sueldoBonificacionPorcentual(sueldoFijoMensual, montoBonificacionAniosServicio); // Se adiciona la bonificación por años de servicio
        sueldoBruto = descuentoService.aplicacionDescuentos(sueldoBruto, descuentos); // Se calcula el sueldo bruto
        sueldoBruto = sueldoBruto + pagoAniosServicio + pagoHorasExtra; // Se calcula el sueldo bruto
        //descuentos = descuentos + cotizacionPrevisional + cotizacionSalud; // Se adicionan al porcentaje de descuento las cotizaciones
        montoSueldoFinal = descuentoService.aplicacionDescuentos(sueldoBruto, (cotizacionPrevisional + cotizacionSalud)); // Se calcula el sueldo final
        LocalDate fecha = LocalDate.of(anioEvaluado, mesEvaluado, diasDelMes(mesEvaluado, anioEvaluado));
        PlanillaEntity sueldo = new PlanillaEntity(rutEmpleado, nombreApellido, categoria, aniosServicio, sueldoFijoMensual, pagoAniosServicio, pagoHorasExtra, descuentos, sueldoBruto, cotizacionPrevisional, cotizacionSalud, montoSueldoFinal, fecha);
        guardarSueldo(sueldo); // Se guarda el sueldo calculado en la base de datos
        }
        return "El calculo se ha realizado existosamente";
    }

}
