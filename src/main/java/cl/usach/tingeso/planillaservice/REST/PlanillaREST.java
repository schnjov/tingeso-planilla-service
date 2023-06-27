package cl.usach.tingeso.planillaservice.REST;

import cl.usach.tingeso.planillaservice.models.AcopioDaysModel;
import cl.usach.tingeso.planillaservice.services.PlanillaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping
public class PlanillaREST {
    Logger logger = LoggerFactory.getLogger(PlanillaREST.class);
    @Autowired
    private PlanillaService planillaService;

    @GetMapping
    public String verifyData(){
        return "Carga de valores correcta";
    }

    @GetMapping("planilla/days")
    public ResponseEntity<AcopioDaysModel> getAcopiosFromDateByCodigoProveedor(@RequestParam Date date, @RequestParam String codigo_proveedor) {
        try{
            AcopioDaysModel dias = planillaService.obtenerDiasDeAcopio(date,codigo_proveedor);
            return ResponseEntity.ok(dias);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("planilla/pagos")
    public ResponseEntity<List<String>> calcularPagos(@RequestParam Integer quincena){
        List<String> pagos = planillaService.calcularPagos(quincena);
        return ResponseEntity.ok(pagos);
    }
}
