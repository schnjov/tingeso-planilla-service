package cl.usach.tingeso.planillaservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanillaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String quincena;

    private String codigo_proveedor;

    private Double totalKlsLeche;

    private Integer diasDeAcopioTotal;

    private Double porcentajeGrasa;

    private Double porcentajeSolidos;

    private Double promedioDiarioKilos;

    private Double porcentajeVariacionLeche;

    private Double porcentajeVariacionGrasa;

    private Double porcentajeVariacionSolidos;

    private Integer pagoPorLeche;

    private Integer pagoPorGrasa;

    private Integer pagoPorSolidos;

    private Integer pagoTotal;

    private Double bonificacionPorFrecuencia;

    private Integer descuentoPorVariacionLeche;

    private Integer descuentoPorVariacionGrasa;

    private Integer descuentoPorVariacionSolidos;

    private Double montoRetencion;

    private Double montoFinal;
}
