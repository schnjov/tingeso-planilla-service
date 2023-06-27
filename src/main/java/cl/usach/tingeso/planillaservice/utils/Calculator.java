package cl.usach.tingeso.planillaservice.utils;

import cl.usach.tingeso.planillaservice.models.AcopioDaysModel;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class Calculator {
    private static final Data data = new Data();
    private static final Logger LOGGER = Logger.getLogger(Calculator.class.getName());

    public Integer calcularPagoPorKilos(String categoria, Double kls) {
        Double pagoPorKilos = data.getPagoByCategoria(categoria);
        LOGGER.info("Pago por kilos: " + pagoPorKilos);
        return (int) (pagoPorKilos * kls);
    }

    public Integer calcularPagoPorGrasa(Double grasa, Double kls) {
        Double pagoPorGrasa = data.getPagoByGrasa(grasa);
        return (int) (pagoPorGrasa * kls);
    }

    public Integer calcularPagoPorSt(Double st, Double kls) {
        Double pagoPorSt = data.getPagoBySolidos(st);
        return (int) (pagoPorSt * kls);
    }

    public Integer calcularPagos(String categoria, Double kls, Double grasa, Double st) {
        Integer pagoPorKilos = calcularPagoPorKilos(categoria, kls);
        Integer pagoPorGrasa = calcularPagoPorGrasa(grasa, kls);
        Integer pagoPorSt = calcularPagoPorSt(st, kls);
        return (pagoPorKilos + pagoPorGrasa + pagoPorSt);
    }
    public Integer calcularDescuentoByGrasa(Double grasa) {
        Double descuentoGrasa = data.getDescuentoByVariacionGrasa(grasa);
        return (int) (descuentoGrasa * 100);
    }
    public Integer calcularDescuentoBySt(Double st) {
        Double descuentoSt = data.getDescuentoByVariacionSt(st);
        return (int) (descuentoSt * 100);
    }
    public Integer calcularDescuentoByKls(Double kls) {
        Double descuentoKls = data.getDescuentoByVariacionKls(kls);
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

    public Double calcularBonificacionPorFrecuencia(AcopioDaysModel acopioDaysModel){
        String frecuenciaDeBonificacion="";
        if (acopioDaysModel.getMT()>10){
            frecuenciaDeBonificacion = "MT";
        }else if (acopioDaysModel.getM()>10){
            frecuenciaDeBonificacion = "M";
        }else if (acopioDaysModel.getT()>10){
            frecuenciaDeBonificacion = "T";
        }
        return data.getBonificacionByFrecuencia(frecuenciaDeBonificacion);
    }

}
