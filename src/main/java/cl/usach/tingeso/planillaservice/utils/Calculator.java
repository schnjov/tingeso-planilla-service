package cl.usach.tingeso.planillaservice.utils;

import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class Calculator {
    private static final Data data = new Data();
    private static final Logger LOGGER = Logger.getLogger(Calculator.class.getName());

    public Integer calcularPagoPorKilos(String categoria, Integer kls) {
        Double pagoPorKilos = data.getPagoByCategoria(categoria);
        LOGGER.info("Pago por kilos: " + pagoPorKilos);
        return (int) (pagoPorKilos * kls);
    }

    public Integer calcularPagoPorGrasa(Integer grasa, Integer kls) {
        Double pagoPorGrasa = data.getPagoByGrasa((double)grasa);
        return (int) (pagoPorGrasa * kls);
    }

    public Integer calcularPagoPorSt(Integer st, Integer kls) {
        Double pagoPorSt = data.getPagoBySolidos((double)st);
        return (int) (pagoPorSt * kls);
    }

    public Integer calcularPagos(String categoria, Integer kls, Integer grasa, Integer st) {
        Integer pagoPorKilos = calcularPagoPorKilos(categoria, kls);
        Integer pagoPorGrasa = calcularPagoPorGrasa(grasa, kls);
        Integer pagoPorSt = calcularPagoPorSt(st, kls);
        return (pagoPorKilos + pagoPorGrasa + pagoPorSt);
    }
    public Integer calcularDescuentoByGrasa(Integer grasa) {
        Double descuentoGrasa = data.getDescuentoByVariacionGrasa((double)grasa);
        return (int) (descuentoGrasa * 100);
    }
    public Integer calcularDescuentoBySt(Integer st) {
        Double descuentoSt = data.getDescuentoByVariacionSt((double)st);
        return (int) (descuentoSt * 100);
    }
    public Integer calcularDescuentoByKls(Integer kls) {
        Double descuentoKls = data.getDescuentoByVariacionKls((double)kls);
        return (int) (descuentoKls * 100);
    }
    public Integer calcularPagoTotal(Integer pagoPorKls, Integer pagoPorGrasa, Integer pagoPorSt,Double bonificacionPorFr
            , Integer descuentoKls, Integer descuentoGrasa, Integer descuentoSt) {
        int pago = pagoPorKls + pagoPorGrasa + pagoPorSt;
        pago+= (int)(pago*bonificacionPorFr);
        return pago - descuentoKls - descuentoGrasa - descuentoSt;
    }

    public Double calcularRetencion(Integer pagoTotal, boolean retencion) {
        if (retencion)
            return pagoTotal * data.getPorcentajeRetencion();
        else
            return 0.0;
    }

    public Double calcularMontoFinal(Integer pagoTotal, Double retencion) {
        return pagoTotal - retencion;
    }

    public Double calcularBonificacionPorFrecuencia(Integer frecuenciaMT, Integer frecuenciaM, Integer frecuenciaT){
        String frecuenciaDeBonificacion="";
        if (frecuenciaMT>10){
            frecuenciaDeBonificacion = "MT";
        }else if (frecuenciaM>10){
            frecuenciaDeBonificacion = "M";
        }else if (frecuenciaT>10){
            frecuenciaDeBonificacion = "T";
        }
        return data.getBonificacionByFrecuencia(frecuenciaDeBonificacion);
    }

}
