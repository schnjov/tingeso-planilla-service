package cl.usach.tingeso.planillaservice.services;

import cl.usach.tingeso.planillaservice.entities.PlanillaEntity;
import cl.usach.tingeso.planillaservice.repositories.PlanillaRepository;
import cl.usach.tingeso.planillaservice.utils.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PlanillaService {
    @Autowired
    private PlanillaRepository planillaPagosRepository;

    @Autowired
    private Calculator calculator;

    private static final Logger LOGGER = Logger.getLogger(PlanillaService.class.getName());

    public PlanillaEntity crearPlanilla(Integer quincena, String codigo_proveedor) {
        PlanillaEntity planillaPagos = new PlanillaEntity();
        int mes = obtenerMesEnCurso();
        int year = obtenerYearEnCurso();
        String quincenaDate = String.valueOf(year +'/'+ mes + '/' + quincena);
        planillaPagos.setQuincena(quincenaDate);
        planillaPagos.setDiasDeAcopioTotal(quincena.getDiasDeAcopioM()+quincena.getDiasDeAcopioT()+
                quincena.getDiasDeAcopioMT());
        if(planillaPagos.getDiasDeAcopioTotal() == 0){
            planillaPagos.setPromedioDiarioKilos(0.0);
        }else
            planillaPagos.setPromedioDiarioKilos((double) quincena.getKilos() / planillaPagos.getDiasDeAcopioTotal());
        planillaPagos.setPorcentajeVariacionGrasa(quincenaService.obtenerVariacionGrasa(quincena.getProveedor()));
        planillaPagos.setPorcentajeVariacionLeche(quincenaService.obtenerVariacionKilos(quincena.getProveedor()));
        planillaPagos.setPorcentajeVariacionSolidos(quincenaService.obtenerVariacionSolidos(quincena.getProveedor()));
        planillaPagos.setPagoPorLeche(calculator.calcularPagoPorKilos(quincena.getProveedor().getCategoria(),
                quincena.getKilos()));
        planillaPagos.setPagoPorGrasa(calculator.calcularPagoPorGrasa(quincena.getPorcentajeGrasa(),
                quincena.getKilos()));
        planillaPagos.setPagoPorSolidos(calculator.calcularPagoPorSt(quincena.getPorcentajeSolidos(),
                quincena.getKilos()));
        planillaPagos.setBonificacionPorFrecuencia(calculator.calcularBonificacionPorFrecuencia(quincena.getDiasDeAcopioMT()
                , quincena.getDiasDeAcopioM(), quincena.getDiasDeAcopioT()));
        planillaPagos.setDescuentoPorVariacionGrasa(calculator.calcularDescuentoByGrasa(quincena.getPorcentajeGrasa()));
        planillaPagos.setDescuentoPorVariacionLeche(calculator.calcularDescuentoByKls(quincena.getKilos()));
        planillaPagos.setDescuentoPorVariacionSolidos(calculator.calcularDescuentoBySt(quincena.getPorcentajeSolidos()));
        planillaPagos.setPagoTotal(calculator.calcularPagoTotal(planillaPagos.getPagoPorLeche(),
                planillaPagos.getPagoPorGrasa(),planillaPagos.getPagoPorSolidos(),planillaPagos.getBonificacionPorFrecuencia(),
                planillaPagos.getDescuentoPorVariacionGrasa(),planillaPagos.getDescuentoPorVariacionLeche(),
                planillaPagos.getDescuentoPorVariacionSolidos()));
        planillaPagos.setMontoRetencion(calculator.calcularRetencion(planillaPagos.getPagoTotal(),
                quincena.getProveedor().getAfectoARetencion()));
        planillaPagos.setMontoFinal(calculator.calcularMontoFinal(planillaPagos.getPagoTotal(),
                planillaPagos.getMontoRetencion()));
        return planillaPagos;
    }

    public void calcularPagos(){
        List< ProveedorEntity > proveedores = proveedorService.findAll();
        Date fechaHoy = new Date();
        for (ProveedorEntity proveedor: proveedores) {
            QuincenaEntity quincena = quincenaService.crearQuincenaActual(fechaHoy, proveedor);
            if (quincena == null) {
                continue;
            }
            PlanillaEntity planillaPagos = crearPlanilla(quincena);
            planillaPagosRepository.save(planillaPagos);
        }
    }

    public PlanillaEntity findByIdProveedor(String idProveedor) {
        ProveedorEntity proveedor = proveedorService.getProveedorByCodigo(idProveedor);
        QuincenaEntity quincena = quincenaService.getQuincena(proveedor);
        return planillaPagosRepository.getTopByQuincena(quincena);
    }

    private int obtenerMesEnCurso() {
        LocalDate fecha = LocalDate.now();
        return fecha.getMonthValue();
    }

    private int obtenerYearEnCurso(){
        LocalDate fecha = LocalDate.now();
        return fecha.getYear();
    }
}
