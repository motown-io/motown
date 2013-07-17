package io.motown.operatorapi.viewmodel.persistence.repositories;

import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargingStationRepository extends JpaRepository<ChargingStation, String> {
    ChargingStation findById(String chargingStationId);
}
