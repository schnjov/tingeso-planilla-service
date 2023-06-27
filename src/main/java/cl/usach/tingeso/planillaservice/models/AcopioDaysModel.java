package cl.usach.tingeso.planillaservice.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.formatter.qual.Format;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcopioDaysModel {
    @JsonProperty("MT")
    private Integer MT;
    @JsonProperty("M")
    private Integer M;
    @JsonProperty("T")
    private Integer T;
}
