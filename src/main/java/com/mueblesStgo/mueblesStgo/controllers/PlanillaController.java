package com.mueblesStgo.mueblesStgo.controllers;

import com.mueblesStgo.mueblesStgo.entities.PlanillaEntity;
import com.mueblesStgo.mueblesStgo.models.AutorizacionModel;
import com.mueblesStgo.mueblesStgo.models.EmpleadoModel;
import com.mueblesStgo.mueblesStgo.models.JustificativoModel;
import com.mueblesStgo.mueblesStgo.services.AutorizacionService;
import com.mueblesStgo.mueblesStgo.services.EmpleadoService;
import com.mueblesStgo.mueblesStgo.services.JustificativoService;
import com.mueblesStgo.mueblesStgo.services.PlanillaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/planilla")
@CrossOrigin
public class PlanillaController {
    @Autowired
    AutorizacionService autorizacionService;
    @Autowired
    JustificativoService justificativoService;
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    PlanillaService planillaService;

    @GetMapping()
    public List<PlanillaEntity> obtenerPlanilla(){
        return planillaService.obtenerSueldo();
    }

    @GetMapping("/autorizaciones")
    public List<AutorizacionModel> exportarAutorizaciones(HttpServletResponse response) throws IOException {
        List<AutorizacionModel> autorizaciones = autorizacionService.obtenerAutorizaciones();
        if (autorizaciones == null) {
            autorizaciones = new ArrayList<>();
        }
        return autorizaciones;
    }

    @GetMapping("/empleados")
    public List<EmpleadoModel> exportarEmpleados(HttpServletResponse response) throws IOException {
        List<EmpleadoModel> empleados = empleadoService.obtenerEmpleados();
        if (empleados == null) {
            empleados = new ArrayList<>();
        }
        return empleados;
    }

    @GetMapping("/justificativos")
    public List<JustificativoModel> exportarJustificativos(HttpServletResponse response) throws IOException {
        List<JustificativoModel> justificaciones = justificativoService.obtenerJustificativos();
        if (justificaciones == null) {
            justificaciones = new ArrayList<>();
        }
        return justificaciones;
    }

    @PostMapping("/calcularPlanilla")
    public String calculoPlanillas(@RequestBody String orden){
        planillaService.calculoPlanillas();
        return "Recibido";
    }
}
