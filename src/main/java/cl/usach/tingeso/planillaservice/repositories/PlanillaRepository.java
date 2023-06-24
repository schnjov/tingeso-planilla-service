package cl.usach.tingeso.planillaservice.repositories;

import cl.usach.tingeso.planillaservice.entities.PlanillaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanillaRepository extends JpaRepository<PlanillaEntity,Long> {

}
