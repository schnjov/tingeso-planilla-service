package cl.usach.tingeso.planillaservice.repositories;

import cl.usach.tingeso.planillaservice.entities.PlanillaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanillaRepository extends JpaRepository<PlanillaEntity,Long> {
    public List<PlanillaEntity> findPlanillaEntitiesByCodigoProveedorOrderByQuincenaDesc(String codigo_proveedor);
}
