package cl.usach.tingeso.planillaservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorModel {
    @JsonProperty("codigo")
    private String codigo;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("categoria")
    private String categoria;

    @JsonProperty("afectoARetencion")
    private Boolean afectoARetencion;
}
