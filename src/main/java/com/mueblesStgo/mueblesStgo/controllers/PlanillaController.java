package com.mueblesStgo.mueblesStgo.controllers;

import com.mueblesStgo.mueblesStgo.models.AutorizacionModel;
import com.mueblesStgo.mueblesStgo.models.JustificativoModel;
import com.mueblesStgo.mueblesStgo.services.AutorizacionService;
import com.mueblesStgo.mueblesStgo.services.JustificativoService;
import com.mueblesStgo.mueblesStgo.services.PlanillaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    PlanillaService planillaService;

    @GetMapping("/autorizaciones")
    public List<AutorizacionModel> exportarAutorizaciones(HttpServletResponse response) throws IOException {
        List<AutorizacionModel> autorizaciones = autorizacionService.obtenerAutorizaciones();
        if (autorizaciones == null) {
            autorizaciones = new ArrayList<>();
        }
        return autorizaciones;
    }

    @GetMapping("/justificativos")
    public List<JustificativoModel> exportarJustificativos(HttpServletResponse response) throws IOException {
        List<JustificativoModel> justificaciones = justificativoService.obtenerJustificativos();
        if (justificaciones == null) {
            justificaciones = new ArrayList<>();
        }
        return justificaciones;
    }
}
