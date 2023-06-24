package cl.usach.tingeso.planillaservice.utils;

import java.util.ArrayList;

public class Data {
    private static final ArrayList <Double[]>rangoVariacionKls = new ArrayList<Double[]>();
    private static final ArrayList <Double>descuentoKls = new ArrayList<Double>();
    private static final ArrayList <Double[]>rangoVariacionGrasa = new ArrayList<Double[]>();
    private static final ArrayList <Double>descuentoGrasa = new ArrayList<Double>();
    private static final ArrayList <Double[]>rangoVariacionSt = new ArrayList<Double[]>();
    private static final ArrayList <Double>descuentoSt = new ArrayList<Double>();
    private static final ArrayList <String> CategoriasDelProveedor = new ArrayList<String>();
    private static final ArrayList <Double> PagoPorKls = new ArrayList<Double>();
    private static final ArrayList <Double[]> PorcentajeGrasa = new ArrayList<Double[]>();
    private static final ArrayList <Double> PagoPorGrasa = new ArrayList<Double>();
    private static final ArrayList <Double[]> PorcentajeSolidos = new ArrayList<Double[]>();
    private static final ArrayList <Double> PagoPorSolidos = new ArrayList<Double>();
    private static final ArrayList <Double> bonificacionPorFrecuencia = new ArrayList<Double>();
    private static final Double porcentajeRetencion = 0.13;

    public Data() {
        // Categorías del proveedor
        CategoriasDelProveedor.add("A");
        CategoriasDelProveedor.add("B");
        CategoriasDelProveedor.add("C");
        CategoriasDelProveedor.add("D");

        // Pago por kilo de leche (en pesos)
        PagoPorKls.add(700.0);
        PagoPorKls.add(550.0);
        PagoPorKls.add(400.0);
        PagoPorKls.add(250.0);

        // Porcentaje de grasa y pago por kilo de leche (en pesos)
        PorcentajeGrasa.add(new Double[]{0.0, 20.0});
        PorcentajeGrasa.add(new Double[]{21.0, 45.0});
        PorcentajeGrasa.add(new Double[]{46.0, Double.MAX_VALUE});
        PagoPorGrasa.add(30.0);
        PagoPorGrasa.add(80.0);
        PagoPorGrasa.add(120.0);

        // Porcentaje de sólidos totales (ST) y pago por kilo de leche (en pesos)
        PorcentajeSolidos.add(new Double[]{0.0, 7.0});
        PorcentajeSolidos.add(new Double[]{8.0, 18.0});
        PorcentajeSolidos.add(new Double[]{19.0, 35.0});
        PorcentajeSolidos.add(new Double[]{36.0, Double.MAX_VALUE});
        PagoPorSolidos.add(-130.0);
        PagoPorSolidos.add(-90.0);
        PagoPorSolidos.add(95.0);
        PagoPorSolidos.add(150.0);

        //Bonificación por frecuencia de entrega mas de 10 días por quincena
        bonificacionPorFrecuencia.add(0.2);  // Mañana y tarde 20%
        bonificacionPorFrecuencia.add(0.12); // Mañana 12%
        bonificacionPorFrecuencia.add(0.08); // Tarde 8%

        // % Variación negativa KLS Leche y % Descuento sobre el Pago Acopio Leche
        rangoVariacionKls.add(new Double[]{0.0, 8.0});
        rangoVariacionKls.add(new Double[]{9.0, 25.0});
        rangoVariacionKls.add(new Double[]{26.0, 45.0});
        rangoVariacionKls.add(new Double[]{46.0, Double.MAX_VALUE});
        descuentoKls.add(0.0);
        descuentoKls.add(7.0);
        descuentoKls.add(15.0);
        descuentoKls.add(30.0);

        // % Variación negativa Grasa y % Descuento sobre el Pago Acopio Leche
        rangoVariacionGrasa.add(new Double[]{0.0, 15.0});
        rangoVariacionGrasa.add(new Double[]{16.0, 25.0});
        rangoVariacionGrasa.add(new Double[]{26.0, 40.0});
        rangoVariacionGrasa.add(new Double[]{41.0, Double.MAX_VALUE});
        descuentoGrasa.add(0.0);
        descuentoGrasa.add(12.0);
        descuentoGrasa.add(20.0);
        descuentoGrasa.add(30.0);

        // % Variación negativa ST y % Descuento sobre el Pago Acopio Leche
        rangoVariacionSt.add(new Double[]{0.0, 6.0});
        rangoVariacionSt.add(new Double[]{7.0, 12.0});
        rangoVariacionSt.add(new Double[]{13.0, 35.0});
        rangoVariacionSt.add(new Double[]{36.0, Double.MAX_VALUE});
        descuentoSt.add(0.0);
        descuentoSt.add(18.0);
        descuentoSt.add(27.0);
        descuentoSt.add(45.0);
    }

    public Double getPagoByCategoria(String categoria) {
        for (int i = 0; i < CategoriasDelProveedor.size(); i++) {
            if (CategoriasDelProveedor.get(i).equals(categoria)) {
                return PagoPorKls.get(i);
            }
        }
        return 0.0;
    }

    public Double getPagoByGrasa(Double grasa) {
        int index = 0;
        for (Double[] rango : PorcentajeGrasa) {
            if (grasa >= rango[0] && grasa <= rango[1]) {
                return PagoPorGrasa.get(index);
            }
            index++;
        }
        return 0.0;
    }

    public Double getPagoBySolidos(Double solidos) {
        int index = 0;
        for (Double[] rango : PorcentajeSolidos) {
            if (solidos >= rango[0] && solidos <= rango[1]) {
                return PagoPorSolidos.get(index);
            }
            index++;
        }
        return 0.0;
    }

    public Double getBonificacionByFrecuencia(String frecuencia) {
        int index = 0;
        if (frecuencia.equals(""))
            return 0.0;
        if (frecuencia.equals("MT")) {
            index = 0;
        } else if (frecuencia.equals("M")) {
            index = 1;
        } else if (frecuencia.equals("T")) {
            index = 2;
        }
        return bonificacionPorFrecuencia.get(index);
    }

    public Double getDescuentoByVariacionKls(Double variacion) {
        int index = 0;
        for (Double[] rango : rangoVariacionKls) {
            if (variacion >= rango[0] && variacion <= rango[1]) {
                return descuentoKls.get(index);
            }
            index++;
        }
        return 0.0;
    }

    public Double getDescuentoByVariacionGrasa(Double variacion) {
        int index = 0;
        for (Double[] rango : rangoVariacionGrasa) {
            if (variacion >= rango[0] && variacion <= rango[1]) {
                return descuentoGrasa.get(index);
            }
            index++;
        }
        return 0.0;
    }

    public Double getDescuentoByVariacionSt(Double variacion) {
        int index = 0;
        for (Double[] rango : rangoVariacionSt) {
            if (variacion >= rango[0] && variacion <= rango[1]) {
                return descuentoSt.get(index);
            }
            index++;
        }
        return 0.0;
    }

    public Double getPorcentajeRetencion(){
        return porcentajeRetencion;
    }

}
