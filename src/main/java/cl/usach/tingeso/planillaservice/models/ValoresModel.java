package cl.usach.tingeso.planillaservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ValoresModel {

    @JsonProperty("codigoProveedor")
    private String codigoProveedor;

    @JsonProperty("porcentajeGrasa")
    private Integer porcentajeGrasa;

    @JsonProperty("porcentajeSolidos")
    private Integer porcentajeSolidos;
}

