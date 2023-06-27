package cl.usach.tingeso.planillaservice.services;

import cl.usach.tingeso.planillaservice.entities.PlanillaEntity;
import cl.usach.tingeso.planillaservice.models.AcopioDaysModel;
import cl.usach.tingeso.planillaservice.models.ProveedorModel;
import cl.usach.tingeso.planillaservice.models.ValoresModel;
import cl.usach.tingeso.planillaservice.repositories.PlanillaRepository;
import cl.usach.tingeso.planillaservice.utils.Calculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PlanillaService {
    @Autowired
    private PlanillaRepository planillaPagosRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Calculator calculator;

    @Value("${acopio.service.base.url}")
    private String acopioServiceBaseUrl;
    @Value("${proveedor.service.base.url}")
    private String proveedorServiceBaseUrl;
    @Value("${valores.service.base.url}")
    private String valoresServiceBaseUrl;

    private static final Logger LOGGER = Logger.getLogger(PlanillaService.class.getName());

    public PlanillaEntity crearPlanilla(Integer quincena, String codigo_proveedor) throws ParseException {
        PlanillaEntity planillaPagos = new PlanillaEntity();
        ValoresModel valores = this.obtenerValores(codigo_proveedor);
        if(valores!=null) {
            ProveedorModel proveedor = this.obtenerProveedor(codigo_proveedor);
            int dia;
            int mes = obtenerMesEnCurso();
            int year = obtenerYearEnCurso();

            String quincenaDate = year + "/" + mes + "/" + quincena;
            LOGGER.info("Quincena: " + quincenaDate);
            if (quincena == 1)
                dia = 1;
            else
                dia = 16;
            String date = dia + "/" + mes + "/" + year;
            AcopioDaysModel diasDeAcopio = this.obtenerDiasDeAcopio(new SimpleDateFormat("dd/MM/yyyy").parse(date), codigo_proveedor);
            Integer diasAcopioTotal = diasDeAcopio.getM() + diasDeAcopio.getT() + diasDeAcopio.getMT();
            if (diasAcopioTotal == 0) {
                LOGGER.info("No hay dias de acopio para el proveedor: " + codigo_proveedor);
                return null;
            }

            planillaPagos.setCodigoProveedor(codigo_proveedor);
            planillaPagos.setNombreProveedor(proveedor.getNombre());
            planillaPagos.setQuincena(quincenaDate);
            planillaPagos.setDiasDeAcopioTotal(diasAcopioTotal);
            planillaPagos.setTotalKlsLeche(this.obtenerKilosTotales(new SimpleDateFormat("dd/MM/yyyy").parse(date), codigo_proveedor));
            if (planillaPagos.getDiasDeAcopioTotal() == 0) {
                planillaPagos.setPromedioDiarioKilos(0.0);
            } else
                planillaPagos.setPromedioDiarioKilos((double) planillaPagos.getTotalKlsLeche() / planillaPagos.getDiasDeAcopioTotal());
            planillaPagos.setPorcentajeGrasa(Double.valueOf(valores.getPorcentajeGrasa()));
            planillaPagos.setPorcentajeSolidos(Double.valueOf(valores.getPorcentajeSolidos()));
            planillaPagos.setPorcentajeVariacionGrasa(obtenerVariacionGrasa(codigo_proveedor, valores.getPorcentajeGrasa()));
            planillaPagos.setPorcentajeVariacionLeche(obtenerVariacionKilos(codigo_proveedor, planillaPagos.getTotalKlsLeche()));
            planillaPagos.setPorcentajeVariacionSolidos(obtenerVariacionSolidos(codigo_proveedor, valores.getPorcentajeSolidos()));
            planillaPagos.setPagoPorLeche(calculator.calcularPagoPorKilos(proveedor.getCategoria(),
                    planillaPagos.getTotalKlsLeche()));
            planillaPagos.setPagoPorGrasa(calculator.calcularPagoPorGrasa(planillaPagos.getPorcentajeGrasa(),
                    planillaPagos.getTotalKlsLeche()));
            planillaPagos.setPagoPorSolidos(calculator.calcularPagoPorSt(planillaPagos.getPorcentajeSolidos(),
                    planillaPagos.getTotalKlsLeche()));
            planillaPagos.setBonificacionPorFrecuencia(calculator.calcularBonificacionPorFrecuencia(diasDeAcopio));
            planillaPagos.setDescuentoPorVariacionGrasa(calculator.calcularDescuentoByGrasa(planillaPagos.getPorcentajeGrasa()));
            planillaPagos.setDescuentoPorVariacionLeche(calculator.calcularDescuentoByKls(planillaPagos.getTotalKlsLeche()));
            planillaPagos.setDescuentoPorVariacionSolidos(calculator.calcularDescuentoBySt(planillaPagos.getPorcentajeSolidos()));
            planillaPagos.setPagoTotal(calculator.calcularPagoTotal(planillaPagos.getPagoPorLeche(),
                    planillaPagos.getPagoPorGrasa(), planillaPagos.getPagoPorSolidos(), planillaPagos.getBonificacionPorFrecuencia(),
                    planillaPagos.getDescuentoPorVariacionGrasa(), planillaPagos.getDescuentoPorVariacionLeche(),
                    planillaPagos.getDescuentoPorVariacionSolidos()));
            planillaPagos.setMontoRetencion(calculator.calcularRetencion(planillaPagos.getPagoTotal(),
                    proveedor.getAfectoARetencion()));
            planillaPagos.setMontoFinal(calculator.calcularMontoFinal(planillaPagos.getPagoTotal(),
                    planillaPagos.getMontoRetencion()));
            return planillaPagos;
        }else
            return null;
    }


    public List<String> calcularPagos(Integer quincena){
        String url = proveedorServiceBaseUrl;
        ProveedorModel[] proveedores = Objects.requireNonNull(restTemplate.getForObject(url, ProveedorModel[].class));
        List<String> planillasCreadas = new ArrayList<>();
        for (ProveedorModel proveedor: proveedores) {
            try{
                PlanillaEntity planillaPagos = crearPlanilla(quincena, proveedor.getCodigo());
                if (planillaPagos != null) {
                    planillaPagosRepository.save(planillaPagos);
                    planillasCreadas.add(proveedor.getCodigo() + "-" + "Creado");
                }else
                    planillasCreadas.add(proveedor.getCodigo()+"-"+"No creado");
            } catch (ParseException  e) {
                Logger.getLogger(PlanillaService.class.getName()).log(Level.SEVERE,
                        "Error al crear la planilla para el proveedor: "+proveedor, e);
                planillasCreadas.add(proveedor.getCodigo()+"-"+"Error");
            }
        }
        return planillasCreadas;
    }


    public double obtenerVariacionKilos(String codigo_proveedor, double kilosActuales) {
        List<PlanillaEntity> planillas = planillaPagosRepository.findPlanillaEntitiesByCodigoProveedorOrderByQuincenaDesc(codigo_proveedor);
        if (planillas.size() > 1) {
            PlanillaEntity planillaAnterior = planillas.get(0);
            if (planillaAnterior.getTotalKlsLeche() == 0) {
                return 0;
            }
            return (planillaAnterior.getTotalKlsLeche() - kilosActuales) / planillaAnterior.getTotalKlsLeche();
        }
        return 0;
    }

    public double obtenerVariacionGrasa(String codigo_proveedor, double grasaActuales) {
        List<PlanillaEntity> planillas = planillaPagosRepository.findPlanillaEntitiesByCodigoProveedorOrderByQuincenaDesc(codigo_proveedor);
        if (planillas.size() > 1) {
            PlanillaEntity planillaAnterior = planillas.get(0);
            if (planillaAnterior.getPorcentajeGrasa() == 0) {
                return 0;
            }
            return (planillaAnterior.getPorcentajeGrasa() - grasaActuales) / planillaAnterior.getPorcentajeGrasa();
        }
        return 0;
    }

    public double obtenerVariacionSolidos(String codigo_proveedor, double solidosActuales) {
        List<PlanillaEntity> planillas = planillaPagosRepository.findPlanillaEntitiesByCodigoProveedorOrderByQuincenaDesc(codigo_proveedor);
        if (planillas.size() > 1) {
            PlanillaEntity planillaAnterior = planillas.get(0);
            if (planillaAnterior.getPorcentajeSolidos() == 0) {
                return 0;
            }
            return (planillaAnterior.getPorcentajeSolidos() - solidosActuales) / planillaAnterior.getPorcentajeSolidos();
        }
        return 0;
    }

    public ProveedorModel obtenerProveedor(String codigo_proveedor) {
        String acopioServiceUrl = proveedorServiceBaseUrl + "/{codigo_proveedor}";
        return restTemplate.getForObject(acopioServiceUrl, ProveedorModel.class, codigo_proveedor);
    }

    public ValoresModel obtenerValores(String codigo_proveedor){
        String valoresServiceUrl = valoresServiceBaseUrl + "/proveedor/{codigo_proveedor}";
        return restTemplate.getForObject(valoresServiceUrl, ValoresModel.class,codigo_proveedor);
    }

    public AcopioDaysModel obtenerDiasDeAcopio(Date date, String codigo_proveedor) {
        String acopioServiceUrl = acopioServiceBaseUrl + "/days?date={date}&codigo_proveedor={codigo_proveedor}";
        return restTemplate.getForObject(acopioServiceUrl, AcopioDaysModel.class, date, codigo_proveedor);
    }

    public Double obtenerKilosTotales(Date date, String codigo_proveedor) {
        String acopioServiceUrl = acopioServiceBaseUrl + "/proveedor/kilos?date={date}&codigo_proveedor={codigo_proveedor}";
        return restTemplate.getForObject(acopioServiceUrl, Double.class, date, codigo_proveedor);
    }

    private int obtenerMesEnCurso() {
        LocalDate fecha = LocalDate.now();
        return fecha.getMonthValue();
    }

    private int obtenerYearEnCurso() {
        LocalDate fecha = LocalDate.now();
        return fecha.getYear();
    }
}
