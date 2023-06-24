package cl.usach.tingeso.planillaservice.REST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class PlanillaREST {
    Logger logger = LoggerFactory.getLogger(PlanillaREST.class);

    @GetMapping
    public String verifyData(){
        return "Carga de valores correcta";
    }
}
